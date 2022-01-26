package mpdev.compiler.chapter_14_xiv

/**
 * Program parsing - module 0
 * Top Level - program structure
 */

/**
 * parse a program
 * <program> ::= <prog header> [ <var declarations> ] [ <fun declarations> ] <main block> <prog end>
 */
fun parseProgram() {
    parseProgHeader()
    if (inp.lookahead().encToken == Kwd.varDecl)
        parseVarDecl()
    code.funInit()
    if (inp.lookahead().encToken == Kwd.funDecl)
        parseFunDecl()
    parseMainBlock()
    parseProgEnd()
    parseStringConstants()
}

/**
 * parse program header
 * <program header> ::= program <identifier>
 */
fun parseProgHeader() {
    inp.match(Kwd.startOfProgram)
    code.progInit(inp.match(Kwd.identifier).value)
}

/**
 * parse variables declarations
 * <variable declarations> ::= var <identifier> [ = <value> ] [ , <identifier> [ = <value> ] ] *
 */
fun parseVarDecl() {
    while (inp.lookahead().encToken == Kwd.varDecl) {
        do {
            inp.match()
            parseOneVarDecl()
        } while (inp.lookahead().encToken == Kwd.commaToken)
    }
}

/** parse one variable declaration */
fun parseOneVarDecl() {
    val varName = inp.match(Kwd.identifier).value
    inp.match(Kwd.colonToken)
    when (inp.lookahead().encToken) {
        Kwd.intToken -> parseOneIntDecl(varName)
        Kwd.stringToken -> parseOneStringDecl(varName)
        else -> inp.expected("variable type (int or string)")
    }
}

/** parse one int var declaration */
fun parseOneIntDecl(varName: String) {
    var initValue = ""
    inp.match()
    if (inp.lookahead().encToken == Kwd.equalsOp) {
        inp.match()
        initValue = initIntVar()
    }
    declareVar(varName, VarType.int, initValue)
}

/** parse one string var declaration */
fun parseOneStringDecl(varName: String) {
    var initValue = ""
    var varLength = 0
    inp.match()
    if (inp.lookahead().encToken == Kwd.equalsOp) {
        inp.match()
        initValue = initStringVar()
    }
    else {
        inp.match(Kwd.leftParen)
        varLength = inp.match(Kwd.number).value.toInt()
        inp.match(Kwd.rightParen)
    }
    declareVar(varName, VarType.string, initValue, varLength)
}

/** initialisation for int vars */
fun initIntVar(): String {
    var sign = ""
    if (inp.lookahead().type == TokType.addOps) {
        val plusMinus = inp.match().value
        if (plusMinus == "-")
            sign = "-"
    }
    return sign + inp.match(Kwd.number).value
}

/** initialisation for string vars */
fun initStringVar(): String {
    return inp.match(Kwd.string).value
}

/**
 * parse a function declaration
 * <function declaration> ::= fun <identifier> ( ) <block>
 */
fun parseFunDecl() {
    while (inp.lookahead().encToken == Kwd.funDecl) {
        inp.match()
        val functionName = inp.match(Kwd.identifier).value
        labelPrefix = functionName        // set label prefix and label index to function name
        labelIndx = 0
        funName = functionName      // set global var so that we know which funciton we are parsoing
        stackVarOffset = 0  // reset the offset for stack vars for this function
        inp.match(Kwd.leftParen)
        inp.match(Kwd.rightParen)
        inp.match(Kwd.colonToken)
        var funType: VarType = VarType.void
        when (inp.lookahead().encToken) {
            Kwd.intToken -> funType = VarType.int
            Kwd.stringToken -> funType = VarType.string
            Kwd.voidToken -> funType = VarType.void
            else -> inp.expected("function type (int, string or void)")
        }
        inp.match()
        declareFun(functionName, funType)
        parseFunctionBlock()
    }
}

/** parse a function block */
fun parseFunctionBlock() {
    hasReturn = false
    parseBlock()
    if (!hasReturn)
        abort("line ${inp.currentLineNumber}: function $funName has no ${inp.decodeToken(Kwd.retToken)}")
}

/**
 * parse main block
 * <main block> ::= main <block>
 */
fun parseMainBlock() {
    labelPrefix = "main"        // set label prefix and label index
    labelIndx = 0
    inp.match(Kwd.mainToken)
    stackVarOffset = 0  // reset the offset for stack vars for main
    code.mainInit()
    parseBlock()
    code.mainEnd()
}

/**
 * parse program end
 * <program end> ::= endprogram
 */
fun parseProgEnd() {
    inp.match(Kwd.endOfProgram)
    code.progEnd()
    inp.match(Kwd.endOfInput)
}

/** add any string constants at the end of the assembler output */
fun parseStringConstants() {
    code.outputCodeNl()
    code.outputCodeNl(".data")
    code.outputCodeTabNl(".align 8")
    code.outputCommentNl("buffer for string operations - max str length limit")
    code.outputCodeTabNl("$STRING_BUFFER:\t.space $STR_BUF_SIZE")
    if (stringConstants.isEmpty())
        return
    code.outputCommentNl("constant string values go here")
    for (s in stringConstants.keys) {
        stringConstants[s]?.let { code.declareString(s, it) }
    }
}
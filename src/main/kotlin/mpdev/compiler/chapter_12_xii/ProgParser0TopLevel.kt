package mpdev.compiler.chapter_12_xii

/**
 * Program parsing - module 0
 * Top Level - program structure
 */

// global vars

// the identifiers space map
val identifiersSpace = mutableMapOf<String,IdentifierDecl>()

// the offset from base pointer for the next local variable (in the stack)
var stackVarOffset = 0

/////////// support for variables and functions declaration /////////
/** our variable types */
enum class VarType { int }

/** the declaration space (variables and functions) */
class IdentifierDecl(var fv: TokType, var type: VarType, var initialised: Boolean = false,
                     var stackVar: Boolean = false, var stackOffset: Int = 0, var canAssign: Boolean = true)   // default is global var

/** declare a global variable */
fun declareVar(name: String, initValue: String) {
    // check for duplicate var declaration
    if (identifiersSpace[name] != null)
        abort ("line ${inp.currentLineNumber}: identifier $name already declared")
    identifiersSpace[name] = IdentifierDecl(TokType.variable, VarType.int, initValue!="")
    code.declareInt(name, initValue)
}

/** process a function declaration */
fun declareFun(name: String) {
    if (identifiersSpace[name] != null)
        abort ("line ${inp.currentLineNumber}: identifier $name already declared")
    identifiersSpace[name] = IdentifierDecl(TokType.function, VarType.int)
    code.declareAsmFun(name)
}

/////////////////////////////////////////////////////////////////

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
    var initValue = ""
    if (inp.lookahead().encToken == Kwd.equalsOp) {
        inp.match()
        var sign = ""
        if (inp.lookahead().type == TokType.addOps) {
            val plusMinus = inp.match().value
            if (plusMinus == "-")
                sign = "-"
        }
        initValue = sign + inp.match(Kwd.number).value
    }
    declareVar(varName, initValue)
}

/**
 * parse a function declaration
 * <function declaration> ::= fun <identifier> ( ) <block>
 */
fun parseFunDecl() {
    while (inp.lookahead().encToken == Kwd.funDecl) {
        inp.match()
        val funName = inp.match(Kwd.identifier).value
        labelPrefix = funName        // set label prefix and label index
        labelIndx = 0
        inp.match(Kwd.leftParen)
        inp.match(Kwd.rightParen)
        declareFun(funName)
        parseFunctionBlock(funName)
    }
}

/** parse a function block */
fun parseFunctionBlock(funName: String) {
    val hasReturn = parseBlock()
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

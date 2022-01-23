package mpdev.compiler.chapter_14_xiv

/**
 * Program parsing - module 1
 * Control Structures
 */

// global vars
var labelIndx: Int = 0
var labelPrefix = ""

/** create a unique label*/
fun newLabel(): String = "${labelPrefix}_L${labelIndx++}_"

/** post a label to output */
fun postLabel(label: String) = code.outputLabel(label)

////////////////////////////////////////////////////////////

/**
 * parse a block
 * <block> ::= { <statement> * }
 */
fun parseBlock(breakLabel: String = "") {
    inp.match(Kwd.startBlock)
    while (inp.lookahead().type != TokType.endOfBlock && !inp.isEndOfProgram()) {
        parseStatement(breakLabel)
    }
    inp.match(Kwd.endBlock)
}

/**
 * parse a statement
 * <statement> ::= <block> | <if> | <while> | <repeat> | <for> | <break> |
 *                 <return> | <read> | <print> | <assignment> | <function_call> | null [ ; ]
 */
fun parseStatement(breakLabel: String) {
    when (inp.lookahead().encToken) {
        Kwd.startBlock -> parseBlock(breakLabel)
        Kwd.ifToken -> parseIf(breakLabel)
        Kwd.whileToken -> parseWhile()
        Kwd.repeatToken -> parseRepeat()
        Kwd.forToken -> ForParser().parseFor()   // in separate module due to increased complexity
        Kwd.breakToken -> parseBreak(breakLabel)
        Kwd.retToken -> parseReturn()
        Kwd.readToken -> parseRead()
        Kwd.printToken -> parsePrint()
        Kwd.printLnToken -> parsePrintLn()
        Kwd.identifier -> {
            if (inp.lookahead().type == TokType.variable) parseAnyAssignment()
            else if (inp.lookahead().type == TokType.function) parseFunctionCall()
            else abort("line ${inp.currentLineNumber}: identifier ${inp.lookahead().value} not declared")
        }
        Kwd.semiColonToken -> inp.match()   // semicolons are simply ignored
        else -> inp.expected("valid keyword, semicolon or identifier")
    }
}

/**
 * parse if statement
 * <if> ::= if ( <b-expression> ) <block> [ else <block> ]
 */
fun parseIf(breakLabel: String) {
    inp.match()
    inp.match(Kwd.leftParen)
    parseBooleanExpression()
    inp.match(Kwd.rightParen)
    val label1 = newLabel()
    code.branchIfFalse(label1)
    parseBlock(breakLabel)
    if (inp.lookahead().encToken == Kwd.elseToken) {
        inp.match()
        val label2 = newLabel()
        code.branch(label2)
        postLabel(label1)
        parseBlock(breakLabel)
        postLabel(label2)
    }
    else
        postLabel(label1)
}

/**
 * parse while statement
 * <while> ::= while ( <b-expression> ) <block>
 */
fun parseWhile() {
    inp.match()
    val label1 = newLabel()
    val label2 = newLabel()
    postLabel(label1)
    inp.match(Kwd.leftParen)
    parseBooleanExpression()
    inp.match(Kwd.rightParen)
    code.branchIfFalse(label2)
    parseBlock(label2)
    code.branch(label1)
    postLabel(label2)
}

/**
 * parse repeat statement
 * <repeat> ::= repeat <block> until ( <b-expression> )
 */
fun parseRepeat() {
    inp.match()
    val label1 = newLabel()
    val label2 = newLabel()
    postLabel(label1)
    parseBlock(label2)
    inp.match(Kwd.untilToken)
    inp.match(Kwd.leftParen)
    parseBooleanExpression()
    inp.match(Kwd.rightParen)
    code.branchIfFalse(label1)
    postLabel(label2)
}

/**
 * parse break statement
 * <break> ::= break
 */
fun parseBreak(label: String) {
    inp.match()
    if (label == "")
        abort("line ${inp.currentLineNumber}: no loop to break of")
    else
        code.branch(label)
}

/**
 * parse return statement
 * <return> ::= return <b-expression>
 */
fun parseReturn() {
    inp.match()
    hasReturn = true       // set the return flag for this function
    when (getType(funName)) {
        VarType.int -> parseBooleanExpression()
        VarType.string -> parseStringExpression()
        VarType.void -> {}
    }
    code.returnFromCall()
}

/**
 * parse read statement
 * <read> :: = read <identifier> [ , <identifier> ] *
 */
fun parseRead() {
    var varToken: Token
    do {
        inp.match()
        varToken = inp.match(Kwd.identifier)
        if (varToken.type == TokType.none)
            abort("line ${inp.currentLineNumber}: identifier ${varToken.value} not declared")
        if (varToken.type != TokType.variable)
            abort("line ${inp.currentLineNumber}: identifier ${varToken.value} is not a variable")
        code.readInt(varToken.value)
        code.assignment(varToken.value)
    } while (inp.lookahead().encToken == Kwd.commaToken)
}

/**
 * parse print statement
 * <print> ::= print <b-expression> [ , <b-expression> ] *
 */
fun parsePrint() {
    inp.match()
    printExpression()
}
fun parsePrintLn() {
    inp.match()
    if (inp.lookahead().encToken != Kwd.semiColonToken)
        printExpression()
    code.printNewline()
}

fun printExpression() {
    do {
        if (inp.lookahead().encToken == Kwd.commaToken)
            inp.match() // skip the comma
        if (parseAnyExpression() == VarType.string)
            code.printStr()
        else
            code.printInt()
    } while (inp.lookahead().encToken == Kwd.commaToken)
}
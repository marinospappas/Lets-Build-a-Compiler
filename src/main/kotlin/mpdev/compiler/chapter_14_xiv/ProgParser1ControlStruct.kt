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
fun parseBlock(breakLabel: String = "", continueLabel: String = "") {
    inp.match(Kwd.startBlock)
    while (inp.lookahead().type != TokType.endOfBlock && !inp.isEndOfProgram()) {
        parseStatement(breakLabel, continueLabel)
    }
    inp.match(Kwd.endBlock)
}

/**
 * parse a statement
 * <statement> ::= <block> | <if> | <while> | <repeat> | <for> | <break> |
 *                 <return> | <read> | <print> | <assignment> | <function_call> | null [ ; ]
 */
fun parseStatement(breakLabel: String, continueLabel: String) {
    when (inp.lookahead().encToken) {
        Kwd.startBlock -> parseBlock(breakLabel, continueLabel)
        Kwd.ifToken -> parseIf(breakLabel, continueLabel)
        Kwd.whileToken -> parseWhile()
        Kwd.repeatToken -> parseRepeat()
        Kwd.forToken -> ForParser().parseFor()   // in separate module due to increased complexity
        Kwd.breakToken -> parseBreak(breakLabel)
        Kwd.continueToken -> parseContinue(continueLabel)
        Kwd.retToken -> parseReturn()
        Kwd.readToken -> parseRead()
        Kwd.printToken -> parsePrint()
        Kwd.printLnToken -> parsePrintLn()
        Kwd.identifier -> {
            if (inp.lookahead().type == TokType.variable) parseAssignment()
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
fun parseIf(breakLabel: String, continueLabel: String) {
    inp.match()
    inp.match(Kwd.leftParen)
    parseBooleanExpression()
    inp.match(Kwd.rightParen)
    val label1 = newLabel()
    code.jumpIfFalse(label1)
    parseBlock(breakLabel, continueLabel)
    if (inp.lookahead().encToken == Kwd.elseToken) {
        inp.match()
        val label2 = newLabel()
        code.jump(label2)
        postLabel(label1)
        parseBlock(breakLabel, continueLabel)
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
    code.jumpIfFalse(label2)
    parseBlock(label2, label1)
    code.jump(label1)
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
    parseBlock(label2, label1)
    inp.match(Kwd.untilToken)
    inp.match(Kwd.leftParen)
    parseBooleanExpression()
    inp.match(Kwd.rightParen)
    code.jumpIfFalse(label1)
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
    code.jump(label)
}

/**
 * parse break statement
 * <continue> ::= continue
 */
fun parseContinue(label: String) {
    inp.match()
    if (label == "")
        abort("line ${inp.currentLineNumber}: no loop to continue")
    code.jump(label)
}

/**
 * parse return statement
 * <return> ::= return <b-expression>
 */
fun parseReturn() {
    inp.match()
    if (labelPrefix == MAIN_BLOCK)
        abort("line ${inp.currentLineNumber}: return is not allowed in [main]")
    hasReturn = true       // set the return flag for this function
    val funType = getType(funName)
    if (funType != DataType.void) {
        val expType = parseExpression()
        if (expType != funType)
            abort("line ${inp.currentLineNumber}: $funType function cannot return $expType")
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
        val identName = varToken.value
        val strLen = identifiersMap[identName]?.size!!
        when (getType(identName)) {
            DataType.int -> {
                code.readInt(identName)
                code.assignment(identName)
            }
            DataType.string -> code.readString(identName, strLen)
            else -> {}
        }
    } while (inp.lookahead().encToken == Kwd.commaToken)
}

/**
 * parse print statement
 * <print> ::= print <b-expression> [ , <b-expression> ] *
 */
fun parsePrint() {
    inp.match()
    printExpressions()
}
fun parsePrintLn() {
    inp.match()
    if (inp.lookahead().encToken != Kwd.semiColonToken)
        printExpressions()
    code.printNewline()
}

fun printExpressions() {
    do {
        if (inp.lookahead().encToken == Kwd.commaToken)
            inp.match() // skip the comma
        when (parseBooleanExpression()) {
            DataType.int -> code.printInt()
            DataType.string -> code.printStr()
            else -> {}
        }
    } while (inp.lookahead().encToken == Kwd.commaToken)
}
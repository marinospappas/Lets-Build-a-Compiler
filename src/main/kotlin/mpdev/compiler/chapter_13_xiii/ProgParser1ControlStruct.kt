package mpdev.compiler.chapter_13_xiii

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
fun parseBlock(breakLabel: String = "", continueLabel: String = ""): Boolean {
    var foundReturn = false
    inp.match(Kwd.startBlock)
    while (inp.lookahead().type != TokType.endOfBlock && !inp.isEndOfProgram()) {
        if (parseStatement(breakLabel, continueLabel))
            foundReturn = true
    }
    inp.match(Kwd.endBlock)
    return foundReturn
}

/**
 * parse a statement
 * <statement> ::= <block> | <if> | <while> | <repeat> | <for> | <break> |
 *                 <return> | <read> | <print> | <assignment> | <function_call> | null [ ; ]
 */
fun parseStatement(breakLabel: String, continueLabel: String): Boolean {
    when (inp.lookahead().encToken) {
        Kwd.startBlock -> return parseBlock(breakLabel, continueLabel)
        Kwd.ifToken -> return parseIf(breakLabel, continueLabel)
        Kwd.whileToken -> return parseWhile()
        Kwd.repeatToken -> return parseRepeat()
        Kwd.forToken -> return ForParser().parseFor()   // in separate module due to increased complexity
        Kwd.breakToken -> { parseBreak(breakLabel); return false }
        Kwd.continueToken -> { parseContinue(continueLabel); return false }
        Kwd.retToken -> { parseReturn(); return true }  // only this case returns true
        Kwd.readToken -> { parseRead(); return false }
        Kwd.printToken -> { parsePrint(); return false }
        Kwd.identifier -> {
            if (inp.lookahead().type == TokType.variable) parseAssignment()
            else if (inp.lookahead().type == TokType.function) parseFunctionCall()
            else abort("line ${inp.currentLineNumber}: identifier ${inp.lookahead().value} not declared")
            return false
        }
        Kwd.semiColon -> { inp.match(); return false }    // semicolons are simply ignored
        else -> { inp.expected("valid keyword, semicolon or identifier"); return false }
    }
}

/**
 * parse if statement
 * <if> ::= if ( <b-expression> ) <block> [ else <block> ]
 */
fun parseIf(breakLabel: String, continueLabel: String): Boolean {
    inp.match()
    inp.match(Kwd.leftParen)
    parseBooleanExpression()
    inp.match(Kwd.rightParen)
    val label1 = newLabel()
    code.jumpIfFalse(label1)
    var foundReturn = parseBlock(breakLabel, continueLabel)
    if (inp.lookahead().encToken == Kwd.elseToken) {
        inp.match()
        val label2 = newLabel()
        code.jump(label2)
        postLabel(label1)
        if (parseBlock(breakLabel, continueLabel))
            foundReturn = true
        postLabel(label2)
    }
    else
        postLabel(label1)
    return foundReturn
}

/**
 * parse while statement
 * <while> ::= while ( <b-expression> ) <block>
 */
fun parseWhile(): Boolean {
    inp.match()
    val label1 = newLabel()
    val label2 = newLabel()
    postLabel(label1)
    inp.match(Kwd.leftParen)
    parseBooleanExpression()
    inp.match(Kwd.rightParen)
    code.jumpIfFalse(label2)
    val foundReturn = parseBlock(label2, label1)
    code.jump(label1)
    postLabel(label2)
    return foundReturn
}

/**
 * parse repeat statement
 * <repeat> ::= repeat <block> until ( <b-expression> )
 */
fun parseRepeat(): Boolean {
    inp.match()
    val label1 = newLabel()
    val label2 = newLabel()
    postLabel(label1)
    val foundReturn = parseBlock(label2, label1)
    inp.match(Kwd.untilToken)
    inp.match(Kwd.leftParen)
    parseBooleanExpression()
    inp.match(Kwd.rightParen)
    code.jumpIfFalse(label1)
    postLabel(label2)
    return foundReturn
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
    parseBooleanExpression()
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
    do {
        inp.match()
        parseBooleanExpression()
        code.printInt()
        code.printNewline()
    } while (inp.lookahead().encToken == Kwd.commaToken)
}

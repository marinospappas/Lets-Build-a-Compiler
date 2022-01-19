package mpdev.compiler.chapter_12_xii

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
fun parseBlock(breakLabel: String = ""): Boolean {
    var foundReturn = false
    inp.match(Kwd.startBlock)
    while (inp.lookahead().type != TokType.endOfBlock && !inp.isEndOfProgram()) {
        if (parseStatement(breakLabel))
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
fun parseStatement(breakLabel: String): Boolean {
    when (inp.lookahead().encToken) {
        Kwd.startBlock -> return parseBlock(breakLabel)
        Kwd.ifToken -> return parseIf(breakLabel)
        Kwd.whileToken -> return parseWhile()
        Kwd.repeatToken -> return parseRepeat()
        Kwd.forToken -> return parseFor()
        Kwd.breakToken -> { parseBreak(breakLabel); return false }
        Kwd.retTok -> { parseReturn(); return true }
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
fun parseIf(breakLabel: String): Boolean {
    inp.match()
    inp.match(Kwd.leftParen)
    parseBooleanExpression()
    inp.match(Kwd.rightParen)
    val label1 = newLabel()
    code.branchIfFalse(label1)
    var foundReturn = parseBlock(breakLabel)
    if (inp.lookahead().encToken == Kwd.elseToken) {
        inp.match()
        val label2 = newLabel()
        code.branch(label2)
        postLabel(label1)
        if (parseBlock(breakLabel))
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
    code.branchIfFalse(label2)
    val foundReturn = parseBlock(label2)
    code.branch(label1)
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
    val foundReturn = parseBlock(label2)
    inp.match(Kwd.untilToken)
    inp.match(Kwd.leftParen)
    parseBooleanExpression()
    inp.match(Kwd.rightParen)
    code.branchIfFalse(label1)
    postLabel(label2)
    return foundReturn
}

/**
 * parse for statement
 * dummy version using pseudo-code
 * focuses on parsing of the structure only - not on producing code
 * <for> ::= ( <identifier> = <b-expression> to <b-expression> ) <block>
 */
fun parseFor(): Boolean {
    inp.match()
    val counterName = inp.match(Kwd.identifier).value
    inp.match(Kwd.equalsOp)
    code.dummyInstr("Allocate space for $counterName and set value to:")
    parseBooleanExpression() // this is the FROM expression
    code.dummyInstr("Decrease $counterName by 1")
    inp.match(Kwd.toToken)
    code.dummyInstr("Allocate space for TO variable and set value to:")
    parseBooleanExpression() // this is the TO expression
    val label1 = newLabel()
    val label2 = newLabel()
    // actual start of the loop
    postLabel(label1)
    // increase counter and check the condition
    code.dummyInstr("Increase $counterName by 1")
    code.dummyInstr("Is $counterName <= TO?")
    code.branchIfFalse(label2)
    // execute the body of the loop
    val foundReturn = parseBlock(label2)
    // back to the beginning of the loop
    code.branch(label1)
    // exit point of the loop
    postLabel(label2)
    code.dummyInstr("Release space held for $counterName and TO")
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
    else
        code.branch(label)
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

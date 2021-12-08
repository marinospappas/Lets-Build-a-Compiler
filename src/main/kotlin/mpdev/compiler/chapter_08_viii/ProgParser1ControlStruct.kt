package mpdev.compiler.chapter_08_viii

/**
 * Program parsing - module 1
 * Control Structures
 */

// global vars
var labelIndx: Int = 0

/** create a unique label*/
fun newLabel(): String = "L${labelIndx++}"

/** post a label to output */
fun postLabel(label: String) = code.outputLabel(label)

////////////////////////////////////////////////////////////

/**
 * parse a block
 * <block> ::= { [ <statement> ] * }
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
 * <statement> ::= <block> | <if> | <while> | <repeat> | <for> | <break> | <return> | <assignment>
 */
fun parseStatement(breakLabel: String) {
    when (inp.lookahead().encToken) {
        Kwd.startBlock -> parseBlock(breakLabel)
        Kwd.ifToken -> parseIf(breakLabel)
        Kwd.whileToken -> parseWhile()
        Kwd.repeatToken -> parseRepeat()
        Kwd.forToken -> parseFor()
        Kwd.breakToken -> parseBreak(breakLabel)
        Kwd.retTok -> parseReturn()
        else -> parseAssignment()
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
    // inp.match(Kwd.endifToken) - not needed
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
    // inp.match(Kwd.endwhileToken) - not needed
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
 * parse for statement
 * dummy version using pseudo-code
 * focuses on parsing of the structure only - not on producing code
 * <for> ::= ( <identifier> = <b-expression> to <b-expression> ) <block>
 */
fun parseFor() {
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
    parseBlock(label2)
    // inp.match(Kwd.endforToken) -  not needed
    // back to the beginning of the loop
    code.branch(label1)
    // exit point of the loop
    postLabel(label2)
    code.dummyInstr("Release space held for $counterName and TO")
}

/**
 * parse break statement
 * <break> ::= break
 */
fun parseBreak(label: String) {
    inp.match()
    if (label == "")
        abort("no loop to break of")
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

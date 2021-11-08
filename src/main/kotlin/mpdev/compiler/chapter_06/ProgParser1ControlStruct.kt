package mpdev.compiler.chapter_06

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
 * parse a program
 * <program> ::= <block> end
 */
fun parseProgram() {
    parseBlock()
    inp.match(endProg)
    println("\nEND PROGRAM")
}

/**
 * parse a block
 * <block> ::= [ <statement> ] *
 */
fun parseBlock(breakLabel: String = "") {
    while (!inp.isEndBlock(inp.nextChar)) {
        when (inp.nextChar) {
            ifToken -> parseIf(breakLabel)
            whileToken -> parseWhile()
            repeatToken -> parseRepeat()
            forToken -> parseFor()
            breakToken -> parseBreak(breakLabel)
            else -> parseAssignment()
        }
    }
}

/**
 * parse if statement
 * IF <condition> <block> [ ELSE <block> ] ENDIF
 */
fun parseIf(breakLabel: String) {
    inp.match()
    parseBooleanExpression()
    val label1 = newLabel()
    code.branchIfFalse(label1)
    parseBlock(breakLabel)
    if (inp.nextChar == elseToken) {
        inp.match()
        val label2 = newLabel()
        code.branch(label2)
        postLabel(label1)
        parseBlock(breakLabel)
        postLabel(label2)
    }
    else
        postLabel(label1)
    inp.match(endifToken)
}

/**
 * parse while statement
 * WHILE <condition> <block> ENDWHILE
 */
fun parseWhile() {
    inp.match()
    val label1 = newLabel()
    val label2 = newLabel()
    postLabel(label1)
    parseBooleanExpression()
    code.branchIfFalse(label2)
    parseBlock(label2)
    inp.match(endwhileToken)
    code.branch(label1)
    postLabel(label2)
}

/**
 * parse repeat statement
 * REPEAT <block> UNTIL <condition>
 */
fun parseRepeat() {
    inp.match()
    val label1 = newLabel()
    val label2 = newLabel()
    postLabel(label1)
    parseBlock(label2)
    inp.match(untilToken)
    parseBooleanExpression()
    code.branchIfFalse(label1)
    postLabel(label2)
}

/**
 * parse for statement
 * dummy version using pseudo-code
 * focuses on parsing of the structure only - not on producing code
 */
fun parseFor() {
    inp.match()
    val counterName = inp.getName()
    inp.match(equalsOp)
    code.dummyInstr("Allocate space for $counterName and set value to:")
    parseBlock() // this is the FROM expression
    code.dummyInstr("Decrease $counterName by 1")
    inp.match(toToken)
    code.dummyInstr("Allocate space for TO variable and set value to:")
    parseBlock() // this is the TO expression
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
    inp.match(endforToken)
    // back to the beginning of the loop
    code.branch(label1)
    // exit point of the loop
    postLabel(label2)
    code.dummyInstr("Release space held for $counterName and TO")
}

/** parse break statement */
fun parseBreak(label: String) {
    inp.match()
    if (label == "")
        abort("no loop to break of")
    else
        code.branch(label)
}




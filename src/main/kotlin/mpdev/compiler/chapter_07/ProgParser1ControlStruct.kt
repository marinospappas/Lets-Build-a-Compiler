package mpdev.compiler.chapter_07

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
 * <program> ::= PROGRAM <identifier> <block> ENDPROG
 */
fun parseProgram() {
        parseBegin()
        parseBlock()
        parseEnd()
}

/** parse beginning of program */
fun parseBegin() {
        inp.match(Kwd.startOfProgram)
        val progName = inp.match(Kwd.identifier).value
        println("PROGRAM $progName\n")
}

/** parse end of program */
fun parseEnd() {
        inp.match(Kwd.endOfProgram)
        println("\nEND PROGRAM")
}

/**
 * parse a block
 * <block> ::= [ <statement> ] *
 */
fun parseBlock(breakLabel: String = "") {
        while (inp.lookahead().subType != TokSubType.endOfBlock
                && !inp.isEndOfProgram()) {
                when (inp.lookahead().encToken) {
                        Kwd.ifToken -> parseIf(breakLabel)
                        Kwd.whileToken -> parseWhile()
                        Kwd.repeatToken -> parseRepeat()
                        Kwd.forToken -> parseFor()
                        Kwd.breakToken -> parseBreak(breakLabel)
                        Kwd.comment -> parseComment()
                        else -> parseOther()
                }
        }
}

/**
 * parse if statement
 * IF <condition> <block> [ ELSE <block> ] ENDIF
 */
fun parseIf(breakLabel: String) {
        inp.match()
        parseCondition()
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
        inp.match(Kwd.endifToken)
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
        parseCondition()
        code.branchIfFalse(label2)
        parseBlock(label2)
        inp.match(Kwd.endwhileToken)
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
        inp.match(Kwd.untilToken)
        parseCondition()
        code.branchIfFalse(label1)
        postLabel(label2)
}

/**
 * parse for statement
 * FOR <identifier> = <expression1> TO <expression2> <block> ENDFOR
 * dummy version using pseudo-code
 * focuses on parsing of the structure only - not on producing code
 */
fun parseFor() {
        inp.match()
        val counterName = inp.match(Kwd.identifier).value
        inp.match(Kwd.equalsOp)
        code.dummyInstr("Allocate space for $counterName and set value to:")
        parseOther() // this is the FROM expression
        code.dummyInstr("Decrease $counterName by 1")
        inp.match(Kwd.toToken)
        code.dummyInstr("Allocate space for TO variable and set value to:")
        parseOther() // this is the TO expression
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
        inp.match(Kwd.endforToken)
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

/**
 * parse comment
 * STARTCOMMENT <comment_text> ENDCOMMENT
 */
fun parseComment() {
        inp.match()
        print("COMMENT ")
        while (inp.lookahead().encToken != Kwd.endComment
        &&     inp.lookahead().encToken != Kwd.endOfInput)
                print("${inp.match().value} ")
        println()
        inp.match(Kwd.endComment)
}

/** dummy parse condition */
fun parseCondition() {
        println("\t${inp.match(Kwd.identifier).value}")
}

/** dummy parse function for anything else than control statements */
fun parseOther() {
        println("\t${inp.match(Kwd.identifier).value}")
}

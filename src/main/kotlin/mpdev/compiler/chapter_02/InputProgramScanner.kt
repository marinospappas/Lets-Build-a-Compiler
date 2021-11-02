package mpdev.compiler.chapter_02

import java.io.File

/**
 * The input program scanner class
 * Performs the lexical scanner functions
 * Processes the char-by-char input and returns the tokens from the input stream
 */

/////////////////////////////////////////////////////////
// global special characters definitions
/////////////////////////////////////////////////////////
// numeric operators
val addOp: Char = '+'
val subOp: Char = '-'
val mulOp: Char = '*'
val divOp: Char = '/'
// parentheses
val leftParen: Char = '('
val rightParen: Char = ')'
/////////////////////////////////////////////////////////

/** this class implements the lexical scanner */
class InputProgramScanner(inputFile: String = "") {

    // the input program as string
    var inputProgram: String = ""
    var indx = 0

    // the next character from input
    // this is our lookahead character
    var nextChar: Char = ' '

    /** initialisation code */
    init {
        try {
            val f = File(inputFile)
            // read the whole program into a string
            // add a dummy \n at the end
            // this way the lookahead will work properly when we reach the end of input
            inputProgram = f.readText() + "\n"
        } catch (e: Exception) {
            abort("could not open file [$inputFile]")
        }
    }

    /** get next token from input */
    fun getNextChar() {
        if (indx < inputProgram.length)
            nextChar = inputProgram[indx++]
        else
            exit("end of input")
    }

    /** match a specific input char */
    fun match(x: Char) {
        // return a match when the next char matches x
        if (nextChar == x)
            getNextChar()
        else
            expected("'$x'")
    }

    /** get an identifier */
    fun getName(): String {
        var token: String = ""
        if (!isAlpha(nextChar))
            expected("Identifier")
        else {
            token = nextChar.uppercase()
            getNextChar()
        }
        return token
    }

    /**
     * get a number
     * <number ::= [ <digit> | decimal_dot ] +
     */
    fun getNumber(): String {
        var value: String = ""
        if (!isNumeric(nextChar)) {
            expected("Number")
        } else {
            value = nextChar.toString()
            getNextChar()
        }
        return value
    }

    /** check for an alpha char */
    fun isAlpha(c: Char): Boolean = c.uppercaseChar() in 'A'..'Z'

    /** check for a numeric char */
    fun isNumeric(c: Char): Boolean = c in '0'..'9'

    /** check for an "addop" (+,-) */
    fun isAddop(c: Char): Boolean = c == addOp || c == subOp

    /** check for a "mulop" (*,/) */
    fun isMulop(c: Char): Boolean = c == mulOp || c == divOp

    /** check for left parenthesis */
    fun isLeftParen(c: Char): Boolean = c == leftParen

}

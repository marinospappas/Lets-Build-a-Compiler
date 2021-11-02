package mpdev.compiler.chapter_03

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
// equals
val equalsOp = '='
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
            // set the lookahead character to the first input char
            getNextChar()
        } catch (e: Exception) {
            verify.current.abort("could not open file [$inputFile]")
        }
    }

    /** get the next character from input after skipping white spaces */
    fun getNextChar() {
        skipNextChar()
        skipWhite()
    }

    /** set the lookahead character to the next char from input */
    fun skipNextChar() {
        if (indx < inputProgram.length)
            nextChar = inputProgram[indx++]
        else
            verify.current.exit("end of input")
    }

    /** skip white spaces */
    fun skipWhite() {
        while (isWhite(nextChar))
            skipNextChar()
    }

    /** match a specific input char */
    fun match(x: Char) {
        // return a match when the next char matches x
        if (nextChar == x)
            getNextChar()
        else
            verify.current.expected("'$x'")
    }

    /**
     * get an identifier
     * <identifier> ::= <alpha> [ <alphanumeric> | <_> ] *
     */
    fun getName(): String {
        var token: String = ""
        if (!isAlpha(nextChar))
            verify.current.expected("Identifier")
        else {
            while (isAlphanumeric(nextChar)) {
                token += nextChar.uppercase()
                skipNextChar()
            }
            skipWhite()
        }
        return token
    }

    /**
     * get a number
     * <number> ::= [ <digit> ] +
     */
    fun getNumber(): String {
        var value: String = ""
        if (!isNumeric(nextChar)) {
            verify.current.expected("Number")
        } else {
            while (isNumeric(nextChar)) {
                value += nextChar.toString()
                skipNextChar()
            }
            skipWhite()
        }
        return value
    }

    /** check for an alpha char */
    fun isAlpha(c: Char): Boolean = c.uppercaseChar() in 'A'..'Z'

    /** check for a numeric char */
    fun isNumeric(c: Char): Boolean = c in '0'..'9'

    /** check for alphanumeric */
    fun isAlphanumeric(c: Char): Boolean = isAlpha(c) || isNumeric(c) || c == '_'

    /** check for an "addop" (+,-) */
    fun isAddop(c: Char): Boolean = c == verify.current.addOp || c == verify.current.subOp

    /** check for a "mulop" (*,/) */
    fun isMulop(c: Char): Boolean = c == verify.current.mulOp || c == verify.current.divOp

    /** check for left parenthesis */
    fun isLeftParen(c: Char): Boolean = c == verify.current.leftParen

    /** check for end of line */
    fun isEndOfLine(c: Char): Boolean = c == '\n' || c == '\r'

    /** check for a white space */
    fun isWhite(c: Char): Boolean = c == ' ' || c == '\t'

}

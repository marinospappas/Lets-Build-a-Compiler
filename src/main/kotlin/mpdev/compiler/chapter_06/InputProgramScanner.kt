package mpdev.compiler.chapter_06

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
val orOp: Char = '|'
val xorOp: Char = '~'
val andOp: Char = '&'
val notOp: Char = '!'
val isEqual: Char = '?'
val isNotequal: Char = '#'
val isGreater: Char = '>'
val isLess: Char = '<'
// parentheses
val leftParen: Char = '('
val rightParen: Char = ')'
// equals
val equalsOp = '='
// tokens
val endProg = '.'
val ifToken = 'i'
val endifToken = 'e'
val elseToken = 'l'
val whileToken = 'w'
val endwhileToken = 'e'
val repeatToken = 'r'
val untilToken = 'u'
val forToken = 'f'
val toToken = 't'
val endforToken = 'e'
val breakToken = 'b'
// end of input mark
val nullChar = 0
val endOfInput = nullChar.toChar()
/////////////////////////////////////////////////////////

/** this class implements the lexical scanner */
class InputProgramScanner(inputFile: String = "") {

    // the input program as string
    var inputProgram: String = ""
    var indx = 0

    // the next character from input
    // this is our lookahead character
    var nextChar: Char = ' '

    // the set of tokens that are block terminators
    private val blockTerminators = setOf(endifToken, elseToken, endwhileToken, untilToken, endforToken)

    /** initialisation code class InputProgramScanner */
    init {
        try {
            val f = File(inputFile)
            // read the whole program into a string
            inputProgram = f.readText()
            // set the lookahead character to the first input char
            getNextChar()
        } catch (e: Exception) {
            abort("could not open file [$inputFile]")
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
            nextChar = endOfInput
    }

    /** skip white spaces */
    fun skipWhite() {
        while (isWhite(nextChar))
            skipNextChar()
    }

    /**
     * match a specific input char
     * and advance to next character
     * also produce a match if called with no input or if input is null character
     */
    fun match(x: Char = nullChar.toChar()) {
        // return a match when the next char matches x
        // or when x is null character
        if (nextChar == x || x == nullChar.toChar())
            getNextChar()
        else
            expected("'$x'")
    }

    /**
     * get an identifier
     * <identifier> ::= <alpha> [ <alphanumeric> | <_> ] *
     */
    fun getName(): String {
        var token = ""
        if (!isAlpha(nextChar))
            expected("Identifier")
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
        var value = ""
        if (!isNumeric(nextChar)) {
            expected("Number")
        } else {
            while (isNumeric(nextChar)) {
                value += nextChar.toString()
                skipNextChar()
            }
            skipWhite()
        }
        return value
    }

    /** get a boolean literal */
    fun getBoolean(): Boolean {
        var value = false
        if (!isBoolean(nextChar))
            expected("Boolean Literal")
        else
            value = nextChar.uppercaseChar() == 'T'
        skipNextChar()
        skipWhite()
        return value
    }

    /** check for an alpha char */
    fun isAlpha(c: Char): Boolean = c.uppercaseChar() in 'A'..'Z'

    /** check for a numeric char */
    fun isNumeric(c: Char): Boolean = c in '0'..'9'

    /** check for alphanumeric */
    fun isAlphanumeric(c: Char): Boolean = isAlpha(c) || isNumeric(c) || c == '_'

    /** check for a Boolean value (T,F) */
    fun isBoolean(c: Char): Boolean = c.uppercaseChar() == 'T' || c.uppercaseChar() == 'F'

    /** check for an "addop" (+,-) */
    fun isAddop(c: Char): Boolean = c == addOp || c == subOp

    /** check for a "mulop" (*,/) */
    fun isMulop(c: Char): Boolean = c == mulOp || c == divOp

    /** check for left parenthesis */
    fun isLeftParen(c: Char): Boolean = c == leftParen

    /** check for an "orop" (|,~) */
    fun isOrop(c: Char): Boolean = c == orOp || c == xorOp

    /** check for an "andop" (&) */
    fun isAndop(c: Char): Boolean = c == andOp

    /** check for a relop (=, #, <, >) */
    fun isRelop(c: Char): Boolean = c == isEqual || c == isNotequal || c == isLess || c == isGreater

    /** check for end of line */
    fun isEndOfLine(c: Char): Boolean = c == '\n' || c == '\r'

    /** check for a white space */
    fun isWhite(c: Char): Boolean = c == ' ' || c == '\t' || isEndOfLine(c)

    /** check for end of block */
    fun isEndBlock(c: Char) = blockTerminators.contains(c) || isEndProgram(c)

    /** check for end of program */
    fun isEndProgram(c: Char): Boolean = c == endProg || c == endOfInput
}

package mpdev.compiler.chapter_11_xi

import java.io.File
import kotlin.math.min

/**
 * The input program scanner class
 * Performs the lexical scanner functions
 * Processes the char-by-char input and returns the tokens from the input stream
 */

/////////////////////////////////////////////////////////
// global definitions
/////////////////////////////////////////////////////////

// end of input mark
val nullChar = 0

// input program line number
var lineNumber = 1

/////////////////////////////////////////////////////////

class InputProgramScanner(inputFile: String = "") {

    // the input program as string
    private var inputProgram: String = ""
    private var cursor = 0

    // end of input mark
    private val endOfInput = nullChar.toChar()

    // the next character from input
    // this is our lookahead character
     var nextChar: Char = ' '

    // the next token is here so that we can look ahead
    private lateinit var nextToken: Token

    // any comments are kept here so that they can be transferred to the output
    private var commentString = ""

    /** initialisation code - class InputProgramScanner */
    init {
        try {
            val f = File(inputFile)
            // read the whole program into a string
            // add a newline at the end to deal with end of input easier
            inputProgram = f.readText() + '\n'
            // init the list of tokens for our language
            initKeywords()
            initOperators()
            // set the lookahead character to the first input char and skip any white spaces
            nextChar = inputProgram[0]
            // get the first token from input
            nextToken = scan()
            // process any initial comments
            getComment()
        } catch (e: Exception) {
            abort("$e")
        }
    }

    /**
     * get the next token from the input stream and advance the cursor
     * match this token against a specific given token 'x'
     * also produces a match if called with no token or if token is "any"
     * finally it processes any comments in the code
     * returns the token object that has been matched
     * called by the parser functions
     */
    fun match(keyWord: Kwd = Kwd.any): Token {
        // any comments found in the previous call must be printed in the output code now
        if (commentString != "") {
            code.outputCode(commentString)
            commentString = ""
        }
        // check token to match
        if (keyWord != Kwd.any && nextToken.encToken != keyWord)
            expected(decodeToken(keyWord))
        val thisToken = nextToken
        nextToken = scan()
        // process any initial comments
        getComment()
        return thisToken
    }

    /** lookahead function - returns next token without advancing the cursor */
    fun lookahead(): Token = nextToken

    /** get the next token and advance the "cursor" */
    private fun scan(): Token {
        // first skip white spaces
        skipWhite()

        if (nextChar == endOfInput)  // check for end of input
            return Token(END_OF_INPUT, Kwd.endOfInput, TokInfo.none, TokType.none)

        if (isNumeric(nextChar))            // check for number
            return Token(getNumber(), Kwd.number, TokInfo.number, TokType.none)

        val indx: Int
        if (isAlpha(nextChar)) {            // check for name
            val name = getName()
            indx = isKeyword(name)
            return (if (indx >= 0) languageTokens[indx]  // keyword found
                    else Token(name, Kwd.identifier, TokInfo.identifier, TokType.none))  /* identifier found */
        }

        indx = specSeqPresent()            // we have a special character - check for special sequence
        if (indx >= 0)
            return(getSpecSeq(indx))

        val thisChar = nextChar           // at this point we have an invalid token
        getNextChar()
        return Token(thisChar.toString(), Kwd.invalid, TokInfo.invalid, TokType.invalid)
    }

    /** check if a specific name is a keyword */
    private fun isKeyword(name: String): Int {
        if (cursor >= inputProgram.length)         // check for end of input
            return -1
        for (i in languageTokens.indices) {
            if (languageTokens[i].value == name)  // check for keyword match
                return i
        }
        return -1
    }

    /**
     * check the beginning of the remaining input for special sequence (e.g. operator)
     * returns the index in our keywords list if found or -1 if not
     */
    private fun specSeqPresent(): Int {
        if (cursor >= inputProgram.length)         // check for end of input
            return -1
        for (i in languageTokens.indices) {
            val tokenValue = languageTokens[i].value
            if (inputProgram.substring(cursor).startsWith(tokenValue))  // check for keyword match
                return i
        }
        return -1
    }

    /** get a special sequence from input (keyword or operator  */
    private fun getSpecSeq(indx: Int): Token {
        if (indx >= languageTokens.size)
            return Token(NO_TOKEN, Kwd.noToken, TokInfo.none, TokType.none)
        val t = languageTokens[indx]
        cursor = min(cursor+t.value.length, inputProgram.length)
        nextChar = inputProgram[cursor]
        return t
    }

    /**
     * get an identifier
     * <identifier> ::= <alpha> [ <alphanumeric> | <_> ] *
     */
    private fun getName(): String {
        var token = ""
        while (isAlphanumeric(nextChar)) {
            token += nextChar
            getNextChar()
        }
        return token
    }

    /**
     * get a number
     * <number> ::= [ <digit> ] +
     */
    private fun getNumber(): String {
        var value = ""
        while (isNumeric(nextChar)) {
            value += nextChar.toString()
            getNextChar()
        }
        return value
    }

    /** set the lookahead character to the next char from input */
    private fun getNextChar() {
        if (nextChar == '\n')
            ++lineNumber
        nextChar = if (cursor < inputProgram.length-1)
            inputProgram[++cursor]
        else
            endOfInput
    }

    /** skip white spaces */
    private fun skipWhite() {
        while (isWhite(nextChar))
            getNextChar()
    }

    /** get a comment */
    private fun getComment() {
        while (nextToken.type == TokType.commentStart)
            when (nextToken.encToken) {
                Kwd.blockComment -> getCommentBlock()
                Kwd.blockCommentOut -> getCommentBlock(true)
                Kwd.inlineComment -> getCommentInline()
                Kwd.inlineCommentOut -> getCommentInline(true)
                else -> expected("start of comment")
            }
    }

    /** get a block comment */
    private fun getCommentBlock(printToOut: Boolean = false) {
        var localCommentString = code.COMMENT
        val endComment: String = decodeToken(Kwd.commentEnd)
        while (!inputProgram.substring(cursor).startsWith(endComment) && nextChar != endOfInput) {
            localCommentString += nextChar
            if (nextChar == '\n')
                localCommentString += code.COMMENT
            getNextChar()
        }
        localCommentString += '\n'
        nextToken = scan()      // nextToken now points to endComment or endOfInput
        if (nextToken.encToken == Kwd.endOfInput)
            expected(endComment)
        nextToken = scan()      // nextToken now points to the next token after the comment
        if (printToOut)
            commentString += localCommentString
    }

    /** get an in-line comment */
    private fun getCommentInline(printToOut: Boolean = false) {
        var localCommentString = code.COMMENT
        while (nextChar != '\n' && nextChar != endOfInput) {
            localCommentString += nextChar
            getNextChar()
        }
        localCommentString += '\n'
        nextToken = scan()
        if (printToOut)
            commentString += localCommentString
    }

    /** check for an alpha char */
    private fun isAlpha(c: Char): Boolean = c.uppercaseChar() in 'A'..'Z'

    /** check for a numeric char */
    private fun isNumeric(c: Char): Boolean = c in '0'..'9'

    /** check for alphanumeric */
    private fun isAlphanumeric(c: Char): Boolean = isAlpha(c) || isNumeric(c) || c == '_'

    /** check for end of line */
    private fun isEndOfLine(c: Char): Boolean = c == '\n' || c == '\r'

    /** check for a white space */
    private fun isWhite(c: Char): Boolean = c == ' ' || c == '\t' || isEndOfLine(c)

    /** check for end of program - called by parseBlock */
    fun isEndOfProgram(): Boolean = nextToken.encToken == Kwd.endOfProgram ||
        nextToken.encToken == Kwd.endOfInput

    /** decode an encoded token to token name */
    private fun decodeToken(token: Kwd): String {
        for (i in languageTokens.indices)
            if (languageTokens[i].encToken == token)
                return languageTokens[i].value
        return "*******"
    }

    /** report what was expected and abort */
    fun expected(expMsg: String) {
        abort("line $lineNumber: expected [$expMsg] found [${nextToken.value}]")
    }
}

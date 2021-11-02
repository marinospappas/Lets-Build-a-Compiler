package mpdev.compiler.chapter_07

import java.io.File
import kotlin.math.*

/**
 * The input program scanner class
 * Performs the lexical scanner functions
 * Processes the char-by-char input and returns the tokens from the input stream
 */

/////////////////////////////////////////////////////////
// global definitions
/////////////////////////////////////////////////////////

val NO_TOKEN = "No Token"
val END_OF_INPUT = "End of Input"
val nullChar = 0

class Token(val value: String = NO_TOKEN,
                        val encToken: Kwd = Kwd.none,
                        val type: TokType = TokType.none,
                        val subType: TokSubType = TokSubType.none)

var languageTokens = mutableListOf<Token>()

/////////////////////////////////////////////////////////

/** this class implements the lexical scanner */
class InputProgramScanner(inputFile: String = "") {

        // the input program as string
        private var inputProgram: String = ""
        private var cursor = 0

        // end of input mark
        private val endOfInput = nullChar.toChar()

        // the next character from input
        // this is our lookahead character
        private var nextChar: Char = ' '

        // the next token is here so that we can look ahead
        private lateinit var nextToken: Token

        /** initialisation code */
        init {
                try {
                        val f = File(inputFile)
                        // read the whole program into a string
                        inputProgram = f.readText()
                        // init the lists of keywords and operands
                        initKeywords()
                        initOperands()
                        // set the lookahead character to the first input char and skip any white spaces
                        nextChar = inputProgram[0]
                        skipWhite()
                        // get the first token from input
                        nextToken = scan()
                        //println(">>>>end of init, nextToken = ${nextToken.value} ${nextToken.encToken} ${nextToken.type} ${nextToken.subType}, nextChar = $nextChar")
                } catch (e: Exception) {
                        abort("$e")
                }
        }

        /**
         * get the next token from the input stream and advance the cursor
         * match this token against a specific given token 'x'
         * also produce a match if called with no token or if token is "any"
         * also return the token object that has been matched
         * called by the parser functions
         */
        fun match(x: Kwd = Kwd.any): Token {
                if (nextToken.encToken != x && x != Kwd.any)
                        expected(decodeToken(x))
                val thisToken = nextToken
                nextToken = scan()
                //println(">>>>end of match, nextToken = ${nextToken.value} ${nextToken.encToken} ${nextToken.type} ${nextToken.subType}, nextChar = $nextChar")
                return thisToken
        }

        /** lookahead function - returns next token without advancing the cursor */
        fun lookahead(): Token = nextToken

        /** get the next token and advance the "cursor" */
        private fun scan(): Token {
                if (nextChar == nullChar.toChar())  // check for end of input
                        return Token(END_OF_INPUT, Kwd.endOfInput, TokType.none, TokSubType.none)

                if (isNumeric(nextChar))            // check for number
                        return Token(getNumber(), Kwd.number, TokType.number, TokSubType.none)

                val indx: Int
                if (isAlpha(nextChar)) {             // check for name
                        val name = getName()
                        indx = isKeyword(name)
                        if (indx >= 0)
                                return languageTokens[indx]  // keyword found
                        else
                                return Token(name, Kwd.identifier, TokType.identifier, TokSubType.none)  // identifier found
                }
                indx = specSeqPresent(languageTokens)  // we have a special character - check for operand
                if (indx >= 0)
                        return(getSpecSeq(languageTokens, indx))

                // at this point we have an invalid token
                val thisChar = nextChar
                getNextChar()
                return Token(thisChar.toString(), Kwd.invalid, TokType.invalid, TokSubType.invalid)
        }

        /** check if a specific name is a keyword */
        private fun isKeyword(name: String): Int {
                if (cursor >= inputProgram.length)         // check for end of input
                        return -1
                for (i in languageTokens.indices  ) {
                        if (languageTokens[i].value.equals(name, true))  // check for keyword match
                                return i
                }
                return -1
        }

        /**
         * check the beginning of the remaining input for keyword
         * returns the index in our keywords list if found or -1 if not
         */
        private fun specSeqPresent(specSeqList: List<Token>): Int {
                if (cursor >= inputProgram.length)         // check for end of input
                        return -1
                for (i in specSeqList.indices  ) {
                        val tokenValue = specSeqList[i].value
                        if (inputProgram.substring(cursor).startsWith(tokenValue))  // check for keyword match
                                return i
                }
                return -1
        }

        /** get a special sequence from input (keyword or operand  */
        private fun getSpecSeq(specSeqList: List<Token>, indx: Int): Token {
                if (indx >= specSeqList.size)
                        return Token(NO_TOKEN, Kwd.none, TokType.none, TokSubType.none)
                val t = specSeqList[indx]
                cursor = min(cursor+t.value.length, inputProgram.length)
                nextChar = inputProgram[cursor]
                skipWhite()
                return t
        }

        /**
         * get an identifier
         * <identifier> ::= <alpha> [ <alphanumeric> | <_> ] *
         */
        private fun getName(): String {
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
        private fun getNumber(): String {
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

        /** get the next character from input after skipping white spaces */
        private fun getNextChar() {
                skipNextChar()
                skipWhite()
        }

        /** set the lookahead character to the next char from input */
        private fun skipNextChar() {
                if (cursor < inputProgram.length-1)
                        nextChar = inputProgram[++cursor]
                else
                        nextChar = endOfInput
        }

        /** skip white spaces */
        private fun skipWhite() {
                while (isWhite(nextChar))
                        skipNextChar()
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

        /**
         * check for end of program
         * called by parseBlock
         */
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
        private fun expected(expMsg: String) {
                abort("Expected $expMsg \n found [${nextToken.value}]")
        }
}

package mpdev.compiler.chapter_07_vii

///////////////////////////////////////////////////////////
// define all the keywords used in our programming language
///////////////////////////////////////////////////////////

val NO_TOKEN = "No Token"
val END_OF_INPUT = "End of Input"

class Token(val value: String = NO_TOKEN,
            val encToken: Kwd = Kwd.none,
            val info: TokInfo = TokInfo.none,
            val type: TokType = TokType.none)

var languageTokens = mutableListOf<Token>()

// our language list of keywords and operands and any other special character sequence
enum class Kwd {
    addOp, subOp, mulOp, divOp,
    equalsOp,
    leftParen, rightParen,
    ifToken, elseToken, endifToken,
    whileToken, endwhileToken,
    repeatToken, untilToken,
    forToken, toToken, endforToken,
    breakToken,
    identifier, number,
    boolTrue, boolFalse,
    startOfProgram, endOfProgram,
    endOfInput, any, invalid, none }

enum class TokType { addOps, mulOps, booleanLit,
    endOfBlock, endOfPRogram, endOfInput, invalid, none }
enum class TokInfo { identifier, keyword, operator, number, invalid, none }


// initialise our lists (= define the language)

fun initKeywords() {
    languageTokens.add(
        Token("PROGRAM",  Kwd.startOfProgram, TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("ENDPROG",  Kwd.endOfProgram,   TokInfo.keyword,      TokType.endOfPRogram))
    languageTokens.add(
        Token("IF",       Kwd.ifToken,        TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("ELSE",     Kwd.elseToken,      TokInfo.keyword,      TokType.endOfBlock))
    languageTokens.add(
        Token("ENDIF",    Kwd.endifToken,     TokInfo.keyword,      TokType.endOfBlock))
    languageTokens.add(
        Token("WHILE",    Kwd.whileToken,     TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("ENDWHILE", Kwd.endwhileToken,  TokInfo.keyword,      TokType.endOfBlock))
    languageTokens.add(
        Token("FOR",      Kwd.forToken,       TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("TO",       Kwd.toToken,        TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("ENDFOR",   Kwd.endforToken,    TokInfo.keyword,      TokType.endOfBlock))
    languageTokens.add(
        Token("REPEAT",   Kwd.repeatToken,    TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("UNTIL",    Kwd.untilToken,     TokInfo.keyword,      TokType.endOfBlock))
    languageTokens.add(
        Token("BREAK",    Kwd.breakToken,     TokInfo.keyword,      TokType.none))

    languageTokens.add(
        Token("TRUE",     Kwd.boolTrue,       TokInfo.keyword,      TokType.booleanLit))
    languageTokens.add(
        Token("FALSE",    Kwd.boolTrue,       TokInfo.keyword,      TokType.booleanLit))

    languageTokens.add(
        Token(nullChar.toString(),  Kwd.endOfInput,  TokInfo.keyword,     TokType.endOfPRogram))

    languageTokens.add(
        Token("Identifier", Kwd.identifier,     TokInfo.identifier, TokType.none))
    languageTokens.add(
        Token("Number",    Kwd.number,          TokInfo.number,     TokType.none))
}

///////////////////////////
// define all the operators
///////////////////////////

fun initOperators() {
    languageTokens.add(
        Token("+",      Kwd.addOp,      TokInfo.operator, TokType.addOps))
    languageTokens.add(
        Token("-",      Kwd.subOp,      TokInfo.operator, TokType.addOps))
    languageTokens.add(
        Token("*",      Kwd.mulOp,      TokInfo.operator, TokType.mulOps))
    languageTokens.add(
        Token("/",      Kwd.divOp,      TokInfo.operator, TokType.mulOps))
    languageTokens.add(
        Token("=",      Kwd.equalsOp,   TokInfo.operator, TokType.mulOps))
    languageTokens.add(
        Token("(",      Kwd.leftParen,  TokInfo.operator, TokType.none))
    languageTokens.add(
        Token(")",      Kwd.rightParen, TokInfo.operator, TokType.none))
}


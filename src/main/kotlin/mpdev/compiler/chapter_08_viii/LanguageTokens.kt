package mpdev.compiler.chapter_08_viii

///////////////////////////////////////////////////////////
// define all the keywords used in our programming language
///////////////////////////////////////////////////////////

val NO_TOKEN = "No Token"
val END_OF_INPUT = "End of Input"

// boolean literals
val BOOLEAN_TRUE = "TRUE"
val BOOLEAN_FALSE = "FALSE"

// main function name
val MAIN_BLOCK = "MAIN"

class Token(val value: String = NO_TOKEN,
            val encToken: Kwd = Kwd.noToken,
            val info: TokInfo = TokInfo.none,
            val type: TokType = TokType.none)

var languageTokens = mutableListOf<Token>()

// our language list of keywords and operands and any other special character sequence
enum class Kwd {
    addOp, subOp, mulOp, divOp,
    equalsOp,
    leftParen, rightParen,

    orOp, xorOp, andOp, notOp,
    isEqual, isNotEqual,
    isGreater, isLess,
    isGreaterOrEq, isLessOrEq,

    startBlock, endBlock,

    ifToken, elseToken, endifToken,
    whileToken, endwhileToken,
    repeatToken, untilToken,
    forToken, toToken, endforToken,
    breakToken,

    identifier, number, booleanLit,

    startOfProgram, endOfProgram,
    varDecl, funDecl, retTok, mainToken,
    endOfInput, any, invalid, noToken }

enum class TokType { addOps, mulOps,
    orOps, andOps, relOps,
    endOfBlock, endOfPRogram, endOfInput,
    invalid, none }

enum class TokInfo { identifier, keyword, operator, number, booleanLit, invalid, none }


// initialise our lists (= define the language)

fun initKeywords() {
    languageTokens.add(
        Token("program",  Kwd.startOfProgram, TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("endprogram", Kwd.endOfProgram, TokInfo.keyword,      TokType.endOfPRogram))

    languageTokens.add(
        Token("var",      Kwd.varDecl,       TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("fun",      Kwd.funDecl,       TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("return",   Kwd.retTok,        TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token(MAIN_BLOCK,      Kwd.mainToken,       TokInfo.keyword,      TokType.none))

    languageTokens.add(
        Token("{",        Kwd.startBlock,    TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("}",        Kwd.endBlock,      TokInfo.keyword,      TokType.endOfBlock))

    languageTokens.add(
        Token("if",       Kwd.ifToken,        TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("else",     Kwd.elseToken,      TokInfo.keyword,      TokType.endOfBlock))
//    languageTokens.add(
//        Token("ENDIF",    Kwd.endifToken,     TokInfo.keyword,      TokType.endOfBlock))
    languageTokens.add(
        Token("while",    Kwd.whileToken,     TokInfo.keyword,      TokType.none))
//    languageTokens.add(
//        Token("ENDWHILE", Kwd.endwhileToken,  TokInfo.keyword,      TokType.endOfBlock))
    languageTokens.add(
        Token("for",      Kwd.forToken,       TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("to",       Kwd.toToken,        TokInfo.keyword,      TokType.none))
//    languageTokens.add(
//        Token("ENDFOR",   Kwd.endforToken,    TokInfo.keyword,      TokType.endOfBlock))
    languageTokens.add(
        Token("repeat",   Kwd.repeatToken,    TokInfo.keyword,      TokType.none))
    languageTokens.add(
        Token("until",    Kwd.untilToken,     TokInfo.keyword,      TokType.endOfBlock))
    languageTokens.add(
        Token("break",    Kwd.breakToken,     TokInfo.keyword,      TokType.none))

    languageTokens.add(
        Token(BOOLEAN_TRUE,    Kwd.booleanLit,     TokInfo.booleanLit,   TokType.none))
    languageTokens.add(
        Token(BOOLEAN_FALSE,   Kwd.booleanLit,     TokInfo.booleanLit,   TokType.none))

    languageTokens.add(
        Token(nullChar.toString(),  Kwd.endOfInput,  TokInfo.keyword,     TokType.none))

    languageTokens.add(
        Token("Identifier", Kwd.identifier,     TokInfo.identifier, TokType.none))
    languageTokens.add(
        Token("Number",    Kwd.number,          TokInfo.number,     TokType.none))
}

///////////////////////////
// define all the operators
///////////////////////////

fun initOperators() {
    // numeric operators
    languageTokens.add(
        Token("+",      Kwd.addOp,      TokInfo.operator, TokType.addOps))
    languageTokens.add(
        Token("-",      Kwd.subOp,      TokInfo.operator, TokType.addOps))
    languageTokens.add(
        Token("*",      Kwd.mulOp,      TokInfo.operator, TokType.mulOps))
    languageTokens.add(
        Token("/",      Kwd.divOp,      TokInfo.operator, TokType.mulOps))

    languageTokens.add(
        Token("(",      Kwd.leftParen,  TokInfo.operator, TokType.none))
    languageTokens.add(
        Token(")",      Kwd.rightParen, TokInfo.operator, TokType.none))
    // boolean operators
    languageTokens.add(
        Token("||",     Kwd.orOp,        TokInfo.operator, TokType.orOps))
    languageTokens.add(
        Token("^^",     Kwd.xorOp,       TokInfo.operator, TokType.orOps))
    languageTokens.add(
        Token("&&",     Kwd.andOp,       TokInfo.operator, TokType.andOps))

    languageTokens.add(
        Token("==",     Kwd.isEqual,     TokInfo.operator, TokType.relOps))
    languageTokens.add(
        Token("!=",     Kwd.isNotEqual,  TokInfo.operator, TokType.relOps))
    languageTokens.add(
        Token("!",      Kwd.notOp,       TokInfo.operator, TokType.none))

    languageTokens.add(
        Token(">=",     Kwd.isGreaterOrEq, TokInfo.operator, TokType.relOps))
    languageTokens.add(
        Token(">",      Kwd.isGreater,   TokInfo.operator, TokType.relOps))

    languageTokens.add(
        Token("<=",     Kwd.isLessOrEq,   TokInfo.operator, TokType.relOps))
    languageTokens.add(
        Token("<",     Kwd.isLess,        TokInfo.operator, TokType.relOps))

    languageTokens.add(
        Token("=",      Kwd.equalsOp,   TokInfo.operator, TokType.mulOps))
}

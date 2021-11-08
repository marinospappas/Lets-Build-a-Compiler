package mpdev.compiler.work_in_progress

///////////////////////////////////////////////////////////
// define all the keywords used in our programming language
///////////////////////////////////////////////////////////

// our language list of keywords and operands and any other special character sequence
enum class Kwd {
    addOp, subOp, mulOp, divOp,
    equalsOp,
    leftParen, rightParen,
    increment, decrement,
    addAssign, subAssign, mulAssign, divAssign,
    ifToken, elseToken, endifToken,
    whileToken, endwhileToken,
    repeatToken, untilToken,
    forToken, toToken, endforToken,
    breakToken,
    identifier, number,
    startOfProgram, endOfProgram,
    comment, endComment,
    endOfInput, any, invalid, none }
enum class TokType { identifier, keyword, operand, number, invalid, none }
enum class TokSubType { addOps, mulOps,
    endOfBlock, endOfPRogram, endOfInput, invalid, none }

// initialise our lists (= define the language)

fun initKeywords() {
    languageTokens.add(
        Token("PROGRAM",  Kwd.startOfProgram, TokType.keyword,      TokSubType.none))
    languageTokens.add(
        Token("ENDPROG",  Kwd.endOfProgram,   TokType.keyword,      TokSubType.endOfPRogram))
    languageTokens.add(
        Token("IF",       Kwd.ifToken,        TokType.keyword,      TokSubType.none))
    languageTokens.add(
        Token("ELSE",     Kwd.elseToken,      TokType.keyword,      TokSubType.endOfBlock))
    languageTokens.add(
        Token("ENDIF",    Kwd.endifToken,     TokType.keyword,      TokSubType.endOfBlock))
    languageTokens.add(
        Token("WHILE",    Kwd.whileToken,     TokType.keyword,      TokSubType.none))
    languageTokens.add(
        Token("ENDWHILE", Kwd.endwhileToken,  TokType.keyword,      TokSubType.endOfBlock))
    languageTokens.add(
        Token("FOR",      Kwd.forToken,       TokType.keyword,      TokSubType.none))
    languageTokens.add(
        Token("TO",       Kwd.toToken,        TokType.keyword,      TokSubType.none))
    languageTokens.add(
        Token("ENDFOR",   Kwd.endforToken,    TokType.keyword,      TokSubType.endOfBlock))
    languageTokens.add(
        Token("REPEAT",   Kwd.repeatToken,    TokType.keyword,      TokSubType.none))
    languageTokens.add(
        Token("UNTIL",    Kwd.untilToken,     TokType.keyword,      TokSubType.endOfBlock))
    languageTokens.add(
        Token("BREAK",    Kwd.breakToken,     TokType.keyword,      TokSubType.none))


    languageTokens.add(
        Token("/*",       Kwd.comment,         TokType.keyword,     TokSubType.none))
    languageTokens.add(
        Token("*/",       Kwd.endComment,      TokType.keyword,     TokSubType.endOfBlock))
    languageTokens.add(
        Token(nullChar.toString(),  Kwd.endOfInput,  TokType.keyword,     TokSubType.endOfPRogram))

    languageTokens.add(
        Token("Identifier", Kwd.identifier,     TokType.identifier, TokSubType.none))
    languageTokens.add(
        Token("Number",    Kwd.number,          TokType.number,     TokSubType.none))
}

//////////////////////////
// define all the operands
//////////////////////////

fun initOperands() {
    languageTokens.add(
        Token("++",     Kwd.increment,  TokType.operand, TokSubType.none))
    languageTokens.add(
        Token("--",     Kwd.decrement,  TokType.operand, TokSubType.none))
    languageTokens.add(
        Token("+=",     Kwd.addAssign,  TokType.operand, TokSubType.none))
    languageTokens.add(
        Token("+=",     Kwd.addAssign,  TokType.operand, TokSubType.none))
    languageTokens.add(
        Token("-=",     Kwd.subAssign,  TokType.operand, TokSubType.none))
    languageTokens.add(
        Token("*=",     Kwd.mulAssign,  TokType.operand, TokSubType.none))
    languageTokens.add(
        Token("/=",     Kwd.divAssign,  TokType.operand, TokSubType.none))
    languageTokens.add(
        Token("+",      Kwd.addOp,      TokType.operand, TokSubType.addOps))
    languageTokens.add(
        Token("-",      Kwd.subOp,      TokType.operand, TokSubType.addOps))
    languageTokens.add(
        Token("*",      Kwd.mulOp,      TokType.operand, TokSubType.mulOps))
    languageTokens.add(
        Token("/",      Kwd.divOp,      TokType.operand, TokSubType.mulOps))
    languageTokens.add(
        Token("=",      Kwd.equalsOp,   TokType.operand, TokSubType.mulOps))
    languageTokens.add(
        Token("(",      Kwd.leftParen,  TokType.operand, TokSubType.none))
    languageTokens.add(
        Token(")",      Kwd.rightParen, TokType.operand, TokSubType.none))
}

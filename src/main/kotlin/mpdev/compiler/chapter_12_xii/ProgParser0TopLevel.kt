package mpdev.compiler.chapter_12_xii

/**
 * Program parsing - module 0
 * Top Level - program structure
 */

// global vars

// the identifiers space map
val identifiersSpace = mutableMapOf<String,IdentifierDecl>()

/////////// support for variables and functions declaration /////////
/** our variable types */
enum class VarType { int }

/** the declaration space (variables and functions) */
class IdentifierDecl(var fv: TokType, var type: VarType, var initialised: Boolean = false)

/** declare a variable */
fun declareVar(name: String, initValue: String) {
    // check for duplicate var declaration
    if (identifiersSpace[name] != null)
        abort ("line ${inp.inLineNumber()}: identifier $name already declared")
    identifiersSpace[name] = IdentifierDecl(TokType.variable, VarType.int, initValue!="")
    code.declareInt(name, initValue)
}

/** process a function declaration */
fun declareFun(name: String) {
    if (identifiersSpace[name] != null)
        abort ("line ${inp.inLineNumber()}: identifier $name already declared")
    identifiersSpace[name] = IdentifierDecl(TokType.function, VarType.int)
    code.declareAsmFun(name)
}

/////////////////////////////////////////////////////////////////

/**
 * parse a program
 * <program> ::= <prog header> [ <var declarations> ] [ <fun declarations> ] <main block> <prog end>
 */
fun parseProgram() {
    parseProgHeader()
    if (inp.lookahead().encToken == Kwd.varDecl)
        parseVarDecl()
    code.funInit()
    if (inp.lookahead().encToken == Kwd.funDecl)
        parseFunDecl()
    parseMainBlock()
    parseProgEnd()
}

/**
 * parse program header
 * <program header> ::= program <identifier>
 */
fun parseProgHeader() {
    inp.match(Kwd.startOfProgram)
    code.progInit(inp.match(Kwd.identifier).value)
}

/**
 * parse variables declarations
 * <variable declarations> ::= var <identifier> [ = <value> ] [ , <identifier> [ = <value> ] ] *
 */
fun parseVarDecl() {
    while (inp.lookahead().encToken == Kwd.varDecl) {
        do {
            inp.match()
            parseOneVarDecl()
        } while (inp.lookahead().encToken == Kwd.commaToken)
    }
}

/** parse one variable declaration */
fun parseOneVarDecl() {
    val varName = inp.match(Kwd.identifier).value
    var initValue = ""
    if (inp.lookahead().encToken == Kwd.equalsOp) {
        inp.match()
        initValue = inp.match(Kwd.number).value
    }
    declareVar(varName, initValue)
}

/**
 * parse functions declarations
 * <function declaration> ::= fun <identifier> ( ) <block>
 */
fun parseFunDecl() {
    while (inp.lookahead().encToken == Kwd.funDecl) {
        inp.match()
        val funName = inp.match(Kwd.identifier).value
        labelPrefix = funName        // set label prefix and label index
        labelIndx = 0
        inp.match(Kwd.leftParen)
        inp.match(Kwd.rightParen)
        declareFun(funName)
        parseBlock()
    }
}

/**
 * parse main block
 * <main block> ::= main <block>
 */
fun parseMainBlock() {
    labelPrefix = "main"        // set label prefix and label index
    labelIndx = 0
    inp.match(Kwd.mainToken)
    code.mainInit()
    parseBlock()
    code.mainEnd()
}

/**
 * parse program end
 * <program end> ::= endprogram
 */
fun parseProgEnd() {
    inp.match(Kwd.endOfProgram)
    code.outputCodeNl()
    code.outputCommentNl("end program")
    inp.match(Kwd.endOfInput)
}

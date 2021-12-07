package mpdev.compiler.chapter_08_viii

/**
 * Program parsing - module 0
 * Top Level - program structure
 */

// global vars

// the variables space
val variablesSpace = mutableMapOf<String,VariableDecl>()

// the functions space
val functionsSpace = mutableMapOf<String,VarType>()

/////////// support for variables declaration /////////
/** our variable types */
enum class VarType { int }

/** our variables declaration */
class VariableDecl(var type: VarType, var initialised: Boolean = false)

/** declare a variable */
fun declareVar(name: String, initValue: String) {
    // check for duplicate var declaration
    if (variablesSpace[name] != null)
        abort ("variable $name already declared")
    variablesSpace[name] = VariableDecl(VarType.int, initValue!="")
    code.dummyInstr("declare variable $name initValue $initValue")
}


/////////// support for functions declaration /////////
/** process a function declaration */
fun declareFun(name: String) {
    if (functionsSpace[name] != null)
        abort ("function $name already declared")
    functionsSpace[name] = VarType.int
    code.dummyInstr("declare function $name")
}

////////////////////////////////////////////////////////////

/**
 * parse a program
 * <program> ::= <prog header> [ <var declarations> ] [ <fun declarations> ] <main block> <prog end>
 */
fun parseProgram() {
    parseProgHeader()
    if (inp.lookahead().encToken == Kwd.varDecl)
        parseVarDecl()
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
    println("program ${inp.match(Kwd.identifier).value}\n")
}

/**
 * parse variables declarations
 * <variable declaration> ::= var <identifier> [ = <value> ]
 */
fun parseVarDecl() {
    while (inp.lookahead().encToken == Kwd.varDecl) {
        inp.match()
        val varName = inp.match(Kwd.identifier).value
        var initValue = ""
        if (inp.lookahead().encToken == Kwd.equalsOp) {
            inp.match()
            initValue = inp.match(Kwd.number).value
        }
        declareVar(varName, initValue)
    }
}

/**
 * parse functions declarations
 * <function declaration> ::= fun <identifier> ( ) <block>
 */
fun parseFunDecl() {
    while (inp.lookahead().encToken == Kwd.funDecl) {
        inp.match()
        val funName = inp.match(Kwd.identifier).value
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
    inp.match(Kwd.mainToken)
    code.outputLabel("main")
    parseBlock()
    code.dummyInstr("stop running")
}
/**
 * parse program end
 * <program end> ::= endprogram
 */
fun parseProgEnd() {
    inp.match(Kwd.endOfProgram)
    println("\nend program")
}

package mpdev.compiler.chapter_14_xiv

// global vars

/** our variable types */
enum class VarType { int, string, void, none }

/** the declaration space (variables and functions) */
class IdentifierDecl(var fv: TokType, var type: VarType, var initialised: Boolean = false,
                     var stackVar: Boolean = false, var stackOffset: Int = 0, var canAssign: Boolean = true)

// the identifiers space map
val identifiersSpace = mutableMapOf<String,IdentifierDecl>()

// the string constants (will be saved to output at the end
val stringConstants = mutableMapOf<String,String>()
var stringCnstIndx = 0
const val STRING_CONST_PREFIX = "STRCNST_"

// the buffer for string operations
const val STRING_BUFFER = "string_buffer_"
const val STR_BUF_SIZE = 1024

/////////// support for variables and functions declaration /////////

var hasReturn: Boolean = false
var funName: String = ""

/** declare a global variable */
fun declareVar(name: String, type: VarType, initValue: String, length: Int = 0) {
    // check for duplicate var declaration
    if (identifiersSpace[name] != null)
        abort ("line ${inp.currentLineNumber}: identifier $name already declared")
    identifiersSpace[name] = IdentifierDecl(TokType.variable, type, initValue!="")
    when (type) {
        VarType.int -> code.declareInt(name, initValue)
        VarType.string -> code.declareString(name, initValue, length)
        else -> return
    }
}

/** process a function declaration */
fun declareFun(name: String, type: VarType) {
    if (identifiersSpace[name] != null)
        abort ("line ${inp.currentLineNumber}: identifier $name already declared")
    identifiersSpace[name] = IdentifierDecl(TokType.function, type)
    code.declareAsmFun(name)
}

/** return the type of a var/fun */
fun getType(identifier: String): VarType = identifiersSpace[identifier]?.type?:VarType.none

/** return the canAssign flag */
fun getCanAssign(identifier: String): Boolean = identifiersSpace[identifier]?.canAssign!!


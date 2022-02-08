package mpdev.compiler.chapter_14_xiv

// global vars

/** our variable types */
enum class DataType { int, string, void, none }

/** the declaration space (variables and functions) */
class IdentifierDecl(var fv: TokType, var type: DataType, var initialised: Boolean = false, var size: Int = 0,
                     var stackVar: Boolean = false, var stackOffset: Int = 0, var canAssign: Boolean = true)

// sizes of various types
val INT_SIZE = 8    // 64-bit integers

// the identifiers space map
val identifiersMap = mutableMapOf<String,IdentifierDecl>()

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
fun declareVar(name: String, type: DataType, initValue: String, length: Int = 0) {
    // check for duplicate var declaration
    if (identifiersMap[name] != null)
        abort ("line ${inp.currentLineNumber}: identifier $name already declared")
    identifiersMap[name] = IdentifierDecl(TokType.variable, type, initValue!="", length)
    when (type) {
        DataType.int -> code.declareInt(name, initValue)
        DataType.string -> code.declareString(name, initValue, length)
        else -> return
    }
}

/** process a function declaration */
fun declareFun(name: String, type: DataType) {
    if (identifiersMap[name] != null)
        abort ("line ${inp.currentLineNumber}: identifier $name already declared")
    identifiersMap[name] = IdentifierDecl(TokType.function, type)
    code.declareAsmFun(name)
}

/** return the type of a var/fun */
fun getType(identifier: String): DataType = identifiersMap[identifier]?.type?:DataType.none

/** return the canAssign flag */
fun getCanAssign(identifier: String): Boolean = identifiersMap[identifier]?.canAssign?:false


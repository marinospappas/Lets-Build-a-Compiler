package mpdev.compiler.chapter_14_xiv

import java.io.File
import java.io.PrintStream
import java.lang.System.err
import java.lang.System.out
import java.util.Date

/** this class implements all the instructions for the target machine */
class X86_64Instructions(outFile: String = "") {

    private val CODE_ID = "x86-64 Assembly Code - AT&T format"
    val COMMENT = "#"
    private val MAIN_ENTRYPOINT = "_start"

    private var outStream: PrintStream = out

    private var outputLines = 0

    // the offset from base pointer for the next local variable (in the stack)
    var stackVarOffset = 0

    /** initialisation code - class InputProgramScanner */
    init {
        if (outFile != "") {
            try {
                outStream = PrintStream(File(outFile))
            } catch (e: Exception) {
                err.println("could not create output file - $e")
                err.println("output code will be sent to stdout")
            }
        }
    }

    /** output lines */
    fun getOutputLines() = outputLines

    /** output code */
    fun outputCode(s: String) {
        outStream.print(s)
        outputLines += s.count { it == '\n' }
    }
    /** output code with newline */
    fun outputCodeNl(s: String = "") = outputCode("$s\n")
    /** output code with tab */
    fun outputCodeTab(s: String) = outputCode("\t$s")
    /** output code with tab and newline */
    fun outputCodeTabNl(s: String) = outputCodeTab("$s\n")
    /** output comment */
    fun outputComment(s: String) = outputCode("$COMMENT $s")
    /** output comment with newline*/
    fun outputCommentNl(s: String) = outputComment("$s\n")
    /** output a label */
    fun outputLabel(s: String) = outputCodeNl("$s:")

    /** initialisation code for assembler */
    fun progInit(progName: String) {
        outputCommentNl(CODE_ID)
        outputCommentNl("program $progName")
        outputCommentNl("compiled on ${Date()}")
        outputCodeNl(".data")
        outputCodeNl(".align 8")
        // copyright message
        outputCodeTabNl("tinsel_msg_: .string \"TINSEL version 2.0 for x86-84 (Linux) Feb 2022 (c) M.Pappas\\n\"")
        // newline string
        outputCodeTabNl("newline_: .string \"\\n\"")
    }

    /** declare int variable (64bit) */
    fun declareInt(varName: String, initValue: String) {
        if (initValue == "")
            outputCodeTabNl("$varName:\t.quad 0")       // uninitialised global int vars default to 0
        else
            outputCodeTabNl("$varName:\t.quad $initValue")
    }

    /** declare string variable */
    fun declareString(varName: String, initValue: String, length: Int = 0) {
        if (length > 0)
            outputCodeTabNl("$varName:\t.space $length") // uninitialised string vars must have length
        else
            outputCodeTabNl("$varName:\t.string \"$initValue\"")
    }

    /** initial code for functions */
    fun funInit() {
        outputCodeNl()
        outputCodeNl(".text")
        outputCodeNl(".align 8")
        outputCodeNl(".global $MAIN_ENTRYPOINT")
    }

    /** declare function */
    fun declareAsmFun(name: String) {
        outputCodeNl()
        outputCommentNl("function $name")
        outputLabel(name)
        outputCodeTab("pushq\t%rbx\t\t")
        outputCommentNl("save \"callee\"-save registers")
        newStackFrame()
    }

    /** end of function - tidy up stack */
    private fun funEnd() {
        restoreStackFrame()
        outputCodeTab("popq\t%rbx\t\t")
        outputCommentNl("restore \"callee\"-save registers")
    }

    /** initial code for main */
    fun mainInit() {
        outputCodeNl()
        outputCommentNl("main program")
        outputLabel(MAIN_ENTRYPOINT)
        outputCodeTab("pushq\t%rbx\t\t")
        outputCommentNl("save \"callee\"-save registers")
        newStackFrame()
        outputCommentNl("print hello message")
        outputCodeTabNl("lea\ttinsel_msg_(%rip), %rdi")
        outputCodeTabNl("call\twrite_s_")
        outputCodeNl()
    }

    /** termination code for assembler */
    fun mainEnd() {
        outputCodeNl()
        outputCommentNl("end of main")
        restoreStackFrame()
        outputCodeTab("popq\t%rbx\t\t")
        outputCommentNl("restore \"callee\"-save registers")
        outputCodeTab("movq\t$60, %rax\t\t")
        outputCommentNl("exit system call")
        outputCodeTab("xorq\t%rdi, %rdi\t\t")
        outputCommentNl("exit code 0")
        outputCodeTabNl("syscall")
    }

    /** set new stack frame */
    private fun newStackFrame() {
        outputCodeTab("pushq\t%rbp\t\t")
        outputCommentNl("new stack frame")
        outputCodeTabNl("movq\t%rsp, %rbp")
        stackVarOffset = 0  // reset the offset for stack vars in this new frame
    }

    /** restore stack frame */
    private fun restoreStackFrame() {
        outputCodeTab("movq\t%rbp, %rsp\t\t")
        outputCommentNl("restore stack frame")
        outputCodeTabNl("popq\t%rbp")
    }

    /**
     * allocate variable space in the stack
     * returns the new stack offset for this new variable
     */
    fun allocateStackVar(size: Int): Int {
        outputCodeTabNl("subq\t$${size}, %rsp")
        stackVarOffset -= INT_SIZE
        return stackVarOffset
    }

    /** release variable space in the stack */
    fun releaseStackVar(size: Int) {
        outputCodeTabNl("addq\t$${size}, %rsp")
        stackVarOffset += INT_SIZE
    }

    //////////////////////////////////////////////////////////////

    /** set accumulator to a value */
    fun setAccumulator(value: String) {
        outputCodeTabNl("movq\t$${value}, %rax")
        outputCodeTabNl("testq\t%rax, %rax")    // also set flags - Z flag set = FALSE
    }

    /** clear accumulator */
    fun clearAccumulator() = outputCodeTabNl("xorq\t%rax, %rax")

    /** increment accumulator */
    fun incAccumulator() = outputCodeTabNl("incq\t%rax")

    /** decrement accumulator */
    fun decAccumulator() = outputCodeTabNl("decq\t%rax")

    /** push accumulator to the stack */
    fun saveAccumulator() = outputCodeTabNl("pushq\t%rax")

    /** add top of stack to accumulator */
    fun addToAccumulator() {
        outputCodeTabNl("popq\t%rbx")
        outputCodeTabNl("addq\t%rbx, %rax")
    }

    /** subtract top of stack from accumulator */
    fun subFromAccumulator() {
        outputCodeTabNl("movq\t%rax, %rbx")
        outputCodeTabNl("popq\t%rax")
        outputCodeTabNl("subq\t%rbx, %rax")
    }

    /** negate accumulator */
    fun negateAccumulator() = outputCodeTabNl("negq\t%rax")

    /** multiply accumulator by top of stack */
    fun multiplyAccumulator() {
        outputCodeTabNl("popq\t%rbx")
        outputCodeTabNl("imulq\t%rbx, %rax")
    }

    /** divide accumulator by top of stack */
    fun divideAccumulator() {
        outputCodeTabNl("movq\t%rax, %rbx")
        outputCodeTabNl("popq\t%rax")
        outputCodeTab("cqto\t\t")
        outputCommentNl("sign extend to rdx")
        outputCodeTabNl("idivq\t%rbx, %rax")
    }

    /** set accumulator to variable */
    fun setAccumulatorToVar(identifier: String) {
        outputCodeTabNl("movq\t${identifier}(%rip), %rax")
        outputCodeTabNl("testq\t%rax, %rax")    // also set flags - Z flag set = FALSE
    }

    /** set accumulator to local variable */
    fun setAccumulatorToLocalVar(offset: Int) {
        outputCodeTab("movq\t")
        if (offset != 0)
            outputCode("$offset")
        outputCodeNl("(%rbp), %rax")
        outputCodeTabNl("testq\t%rax, %rax")    // also set flags - Z flag set = FALSE
    }

    /** call a function */
    fun callFunction(subroutine: String) = outputCodeTabNl("call\t${subroutine}")

    /** return from function */
    fun returnFromCall() {
        funEnd()
        outputCodeTabNl("ret")
    }

    /** set variable to accumulator */
    fun assignment(identifier: String) = outputCodeTabNl("movq\t%rax, ${identifier}(%rip)")

    /** set stack variable to accumulator */
    fun assignmentLocalVar(offset: Int) {
        outputCodeTab("movq\t%rax, ")
        if (offset != 0)
            outputCode("$offset")
        outputCodeNl("(%rbp)")
    }

    /** branch if false */
    fun branchIfFalse(label: String) = outputCodeTabNl("jz\t$label")    // Z flag set = FALSE

    /** branch */
    fun branch(label: String) = outputCodeTabNl("jmp\t$label")

    /** boolean not accumulator */
    fun booleanNotAccumulator() = outputCodeTabNl("xorq\t$1, %rax")

    /** or top of stack with accumulator */
    fun orAccumulator() {
        outputCodeTabNl("popq\t%rbx")
        outputCodeTabNl("orq\t%rbx, %rax")
    }

    /** exclusive or top of stack with accumulator */
    fun xorAccumulator() {
        outputCodeTabNl("popq\t%rbx")
        outputCodeTabNl("xorq\t%rbx, %rax")
    }

    /** and top of stack with accumulator */
    fun andAccumulator() {
        outputCodeTabNl("popq\t%rbx")
        outputCodeTabNl("andq\t%rbx, %rax")
    }

    /** compare and set accumulator and flags - is equal to */
    fun compareEquals() {
        outputCodeTabNl("popq\t%rbx")
        outputCodeTabNl("cmp\t%rax, %rbx")
        outputCodeTabNl("sete\t%al")        // set AL to 1 if comparison is ==
        outputCodeTabNl("andq\t$1, %rax")   // zero the rest of rax and set flags - Z flag set = FALSE
    }

    /** compare and set accumulator and flags - is not equal to */
    fun compareNotEquals() {
        outputCodeTabNl("popq\t%rbx")
        outputCodeTabNl("cmp\t%rax, %rbx")
        outputCodeTabNl("setne\t%al")       // set AL to 1 if comparison is !=
        outputCodeTabNl("andq\t$1, %rax")   // zero the rest of rax and set flags - Z flag set = FALSE
    }

    /** compare and set accumulator and flags - is less than */
    fun compareLess() {
        outputCodeTabNl("popq\t%rbx")
        outputCodeTabNl("cmp\t%rax, %rbx")
        outputCodeTabNl("setl\t%al")        // set AL to 1 if comparison is rbx < rax
        outputCodeTabNl("andq\t$1, %rax")   // zero the rest of rax and set flags - Z flag set = FALSE
    }

    /** compare and set accumulator and flags - is less than */
    fun compareLessEqual() {
        outputCodeTabNl("popq\t%rbx")
        outputCodeTabNl("cmp\t%rax, %rbx")
        outputCodeTabNl("setle\t%al")       // set AL to 1 if comparison is <=
        outputCodeTabNl("andq\t$1, %rax")   // zero the rest of rax and set flags - Z flag set = FALSE
    }

    /** compare and set accumulator and flags - is greater than */
    fun compareGreater() {
        outputCodeTabNl("popq\t%rbx")
        outputCodeTabNl("cmp\t%rax, %rbx")
        outputCodeTabNl("setg\t%al")       // set AL to 1 if comparison is >
        outputCodeTabNl("andq\t$1, %rax")   // zero the rest of rax and set flags - Z flag set = FALSE
    }

    /** compare and set accumulator and flags - is greater than */
    fun compareGreaterEqual() {
        outputCodeTabNl("popq\t%rbx")
        outputCodeTabNl("cmp\t%rax, %rbx")
        outputCodeTabNl("setge\t%al")       // set AL to 1 if comparison is >=
        outputCodeTabNl("andq\t$1, %rax")   // zero the rest of rax and set flags - Z flag set = FALSE
    }

    /** print a newline */
    fun printNewline() {
        outputCodeTabNl("lea\tnewline_(%rip), %rdi")
        outputCodeTabNl("call\twrite_s_")
    }

    /** print accumulator as integer */
    fun printInt() {
        outputCodeTabNl("movq\t%rax, %rdi\t\t# value to be printed in rdi")
        outputCodeTabNl("call\twrite_i_")
    }

    /** read int into variable */
    fun readInt(identifier: String) {
        outputCodeTabNl("lea\t$identifier(%rip), %rdi\t\t# address of the variable to be read")
        outputCodeTabNl("call\tread_i_")
    }

    /** end of program */
    fun progEnd() {
        outputCodeNl()
        outputCommentNl("end program")
    }

    ////////// string operations ///////////////////////

    /** get string address in accumulator */
    fun getStringAddress(identifier: String) = outputCodeTabNl("lea\t${identifier}(%rip), %rax")

    /** save acc string to buffer and address in stack - acc is pointer */
    fun saveString() {
        outputCodeTab("movq\t%rax, %rsi\t\t")
        outputCommentNl("save string - strcpy_(string_buffer, %rax)")
        outputCodeTabNl("lea\t$STRING_BUFFER(%rip), %rdi")
        outputCodeTabNl("call\tstrcpy_")
        outputCodeTabNl("pushq\t%rax")
    }

    /** add acc string to buf string - both are pointers*/
    fun addString() {
        outputCodeTab("popq\t%rdi\t\t")
        outputCommentNl("add string - strcat_(top-of-stack, %rax)")
        outputCodeTabNl("movq\t%rax, %rsi")
        outputCodeTabNl("call\tstrcat_")
    }

    /** set string variable from accumulator (var and acc are pointers */
    fun assignmentString(identifier: String) {
        outputCodeTab("movq\t%rax, %rsi\t\t")
        outputCommentNl("assign string - strcpy_(identifier, %rax)")
        outputCodeTabNl("lea\t${identifier}(%rip), %rdi")
        outputCodeTabNl("call\tstrcpy_")
    }

    /** print string - address in accumulator */
    fun printStr() {
        outputCodeTabNl("movq\t%rax, %rdi\t\t# string pointer to be printed in rdi")
        outputCodeTabNl("call\twrite_s_")
    }

    //////////////////////////////////////////////////////////
    /** dummy instruction */
    fun dummyInstr(cmd: String) = outputCodeTabNl(cmd)

}

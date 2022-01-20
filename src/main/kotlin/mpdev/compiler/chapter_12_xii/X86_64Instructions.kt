package mpdev.compiler.chapter_12_xii

import java.io.File
import java.io.PrintStream
import java.lang.System.err
import java.lang.System.out
import java.util.Date

/** this class implements all the instructions for the target machine */
class X86_64Instructions(outFile: String = "") {

    val COMMENT = "#"
    private val MAIN_ENTRYPOINT = "_start"

    private var outStream: PrintStream = out

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

    /** output code */
    fun outputCode(s: String) = outStream.print(s)
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
        outputCommentNl("program $progName")
        outputCommentNl("compiled on ${Date()}")
        outputCodeNl(".data")
        outputCodeNl(".align 8")
        // copyright message
        outputCodeTabNl("tinsel_msg_: .string \"TINSEL version 1.1 for x86-84 (Linux) Jan 2022 (c) M.Pappas\\n\"")
        // newline string
        outputCodeTabNl("newline_: .string \"\\n\"")
    }

    /** declare int variable (64bit) */
    fun declareInt (varName: String, initValue: String) {
        if (initValue == "")
            outputCodeTabNl("$varName:\t.quad 0")       // uninitialised global vars default to 0
        else
            outputCodeTabNl("$varName:\t.quad $initValue")
    }

    /** initial code for functions */
    fun funInit() {
        outputCodeNl("\n.text")
        outputCodeNl(".align 8")
        outputCodeNl(".global $MAIN_ENTRYPOINT")
    }

    /** declare function */
    fun declareAsmFun (name: String) {
        outputCodeNl()
        outputCommentNl("function $name")
        outputLabel(name)
    }

    /** initial code for main */
    fun mainInit() {
        outputCodeNl()
        outputCommentNl("main program")
        outputLabel(MAIN_ENTRYPOINT)
        newStackFrame()
        outputCodeTabNl("pushq\t%rbx")
        outputCommentNl("print hello message")
        outputCodeTabNl("lea\ttinsel_msg_(%rip), %rdi")
        outputCodeTabNl("call\twrite_s_\n")
    }

    /** termination code for assembler */
    fun mainEnd() {
        outputCodeNl()
        outputCommentNl("end of main")
        outputCodeTabNl("movq\t(%rbp), %rbx")
        restoreStackFrame()
        outputCodeTab("movq\t$60, %rax\t\t")
        outputCommentNl("exit system call")
        outputCodeTab("xorq\t%rdi, %rdi\t\t")
        outputCommentNl("exit code 0")
        outputCodeTabNl("syscall")
    }

    /** set new stack frame */
    fun newStackFrame() {
        outputCodeTabNl("pushq\t%rbp")
        outputCodeTabNl("movq\t%rsp, %rbp")
    }

    /** restore stack frame */
    fun restoreStackFrame() {
        outputCodeTabNl("movq\t%rbp, %rsp")
        outputCodeTabNl("popq\t%rbp")
    }

    /** allocate variable space in the stack */
    fun allocateStackVar(size: Int) = outputCodeTabNl("subq\t$${size}, %rsp")

    /** release variable space in the stack */
    fun releaeStackVar(size: Int) = outputCodeTabNl("addq\t$${size}, %rsp")

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
            outputCode("${offset}")
        outputCodeNl("(%rbp), %rax")
        outputCodeTabNl("testq\t%rax, %rax")    // also set flags - Z flag set = FALSE
    }

    /** call a function */
    fun callFunction(subroutine: String) = outputCodeTabNl("call\t${subroutine}")

    /** return from function */
    fun returnFromCall() = outputCodeTabNl("ret")

    /** set variable to accumulator */
    fun assignment(identifier: String) = outputCodeTabNl("movq\t%rax, ${identifier}(%rip)")

    /** set stack variable to accumulator */
    fun assignmentLocalVar(offset: Int) {
        outputCodeTab("movq\t%rax, ")
        if (offset != 0)
            outputCode("${offset}")
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

    /** print accumulator as integer (with newline for now) */
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

    /** dummy instruction */
    fun dummyInstr(cmd: String) = outputCodeTabNl(cmd)

}

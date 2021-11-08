package mpdev.compiler.chapter_06

class M68000Instructions {

    /** output code */
    fun outputCode(s: String) = print("\t$s")

    /** output code with newline */
    fun outputCodeNl(s: String) = outputCode("$s\n")

    /** output a label */
    fun outputLabel(s: String) = println("$s:")

    /** set accumulator to a value */
    fun setAccumulator(value: String) = outputCodeNl("MOVE #${value},D0")

    /** clear accumulator */
    fun clearAccumulator() = outputCodeNl("CLR D0")

    /** push accumulator to the stack */
    fun saveAccumulator() = outputCodeNl("MOVE D0,-(SP)")

    /** add top of stack to accumulator */
    fun addToAccumulator() = outputCodeNl("ADD (SP)+,D0")

    /** subtract top of stack from accumulator */
    fun subFromAccumulator() {
        outputCodeNl("SUB (SP)+,D0")
        // negate accumulator as the result is the wrong way round
        outputCodeNl("NEG D0")
    }

    /** multiply accumulator by top of stack */
    fun multiplyAccumulator() = outputCodeNl("MULS (SP)+,D0")

    /** divide accumulator by top of stack */
    fun divideAccumulator() {
        outputCodeNl("MOVE (SP)+,D1")
        outputCodeNl("DIVS D1,D0")
    }

    /** set accumulator to variable */
    fun setAccumulatorToVar(identifier: String) = outputCodeNl("MOVE ${identifier}(PC),D0")

    /** call a subroutine */
    fun callSubroutine(subroutine: String) = outputCodeNl("BSR ${subroutine}")

    /** assignment var to value */
    fun assignment(identifier: String) {
        outputCodeNl("LEA $identifier(PC),A0")
        outputCodeNl("MOVE D0,(A0)")
    }

    /** branch if false */
    fun branchIfFalse(label: String) = outputCodeNl("BEQ $label")

    /** branch */
    fun branch(label: String) = outputCodeNl("BRA $label")

    /** boolean not accumulator */
    fun booleanNotAccumulator() = outputCodeNl("EOR #1,D0")

    /** or top of stack with accumulator */
    fun orAccumulator() = outputCodeNl("OR (SP)+,D0")

    /** exclusive or top of stack with accumulator */
    fun xorAccumulator() = outputCodeNl("EOR (SP)+,D0")

    /** and top of stack with accumulator */
    fun andAccumulator() = outputCodeNl("AND (SP)+,D0")

    /** compare and set accumulator and flags - is equal to */
    fun compareEquals() {
        outputCodeNl("CMP (SP)+,D0")
        outputCodeNl("SEQ D0")
        outputCodeNl("TST D0")
    }

    /** compare and set accumulator and flags - is not equal to */
    fun compareNotEquals() {
        outputCodeNl("CMP (SP)+,D0")
        outputCodeNl("SNE D0")
        outputCodeNl("TST D0")
    }

    /** compare and set accumulator and flags - is less than */
    fun compareLess() {
        outputCodeNl("CMP (SP)+,D0")
        outputCodeNl("SGE D0")
        outputCodeNl("TST D0")
    }

    /** compare and set accumulator and flags - is greater than */
    fun compareGreater() {
        outputCodeNl("CMP (SP)+,D0")
        outputCodeNl("SLE D0")
        outputCodeNl("TST D0")
    }

    /** dummy instruction */
    fun dummyInstr(cmd: String) = outputCodeNl(cmd)

}
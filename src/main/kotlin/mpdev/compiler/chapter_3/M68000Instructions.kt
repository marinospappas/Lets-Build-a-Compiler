package mpdev.compiler.chapter_3

class M68000Instructions {

    /** output code */
    fun outputCode(s: String) = print("\t$s")

    /** output code with newline */
    fun outputCodeNl(s: String) = outputCode("\t$s\n")

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

}
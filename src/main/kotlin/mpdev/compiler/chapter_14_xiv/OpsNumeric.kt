package mpdev.compiler.chapter_14_xiv

/** process a number */
fun parseNumber(): DataType {
    code.setAccumulator(inp.match(Kwd.number).value)
    return DataType.int
}

/** process a numeric variable */
fun parseNumVariable(): DataType {
    val varName = inp.match(Kwd.identifier).value
    val localVar = identifiersMap[varName]?.stackVar
    if (localVar == true)
        identifiersMap[varName]?.stackOffset?.let { code.setAccumulatorToLocalVar(it) }
    else
        code.setAccumulatorToVar(varName)
    return DataType.int
}

/** process assignment to numeric var (int for now) */
fun parseNumAssignment(varName: String) {
    code.assignment(varName)
}

/** process a numeric addition */
fun addNumber() {
    code.addToAccumulator()
}

/** process a numeric subtraction */
fun subtractNumber() {
    code.subFromAccumulator()
}

/** process a numeric multiplication */
fun multiplyNumber() {
    code.multiplyAccumulator()
}

/** process a numeric division */
fun divideNumber() {
    code.divideAccumulator()
}

package mpdev.compiler.chapter_14_xiv

/** parse string literal */
fun parseStringLiteral(): DataType {
    val stringValue = inp.match(Kwd.string).value
    // save the string in the map of constant strings
    val strAddr = STRING_CONST_PREFIX + (++stringCnstIndx).toString()
    stringConstants[strAddr] = stringValue
    code.getStringAddress(strAddr)
    return DataType.string
}

/** parse string variable */
fun parseStringVariable(): DataType {
    val strVarName = inp.match(Kwd.identifier).value
    code.getStringAddress(strVarName)
    return DataType.string
}

/** parse string assignment */
fun parseStringAssignment(identName: String) {
    code.assignmentString(identName)
}

/** add strings */
fun addString() {
    code.addString()
}
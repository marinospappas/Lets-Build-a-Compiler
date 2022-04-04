package mpdev.compiler.chapter_14_xiv

/** parse string literal */
fun parseStringLiteral(): DataType {
    val stringValue = inp.match(Kwd.string).value
    // check if this string exists already in our map of constant strings and add it if not
    var stringAddress = ""
    stringConstants.forEach { (k, v) -> if (v == stringValue) stringAddress = k }
    if (stringAddress == "") {  // if not found
        // save the string in the map of constant strings
        stringAddress = STRING_CONST_PREFIX + (++stringCnstIndx).toString()
        stringConstants[stringAddress] = stringValue
    }
    code.getStringAddress(stringAddress)
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
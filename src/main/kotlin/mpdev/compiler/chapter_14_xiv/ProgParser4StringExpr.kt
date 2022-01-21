package mpdev.compiler.chapter_14_xiv

/** parse an expression of any type and branch to boolean/string or other appropriate expression */
fun parseAnyExpression(): VarType {
    if (inp.lookahead().encToken == Kwd.string    // string literal
        ||
        (inp.lookahead().encToken == Kwd.identifier
                && identifiersSpace[inp.lookahead().value]?.type == VarType.string)) {    // string variable
        parseStringExpression()
        return VarType.string
    }
    else {
        parseBooleanExpression()
        return VarType.int
    }
}

/**
 * parse a string expression
 * <s-expression> ::= <s-term> [ + <s-term> ]
 */
fun parseStringExpression() {
    parseStringTerm()
    while (inp.lookahead().type == TokType.addOps) {
        code.saveAccumulator()
        when (inp.lookahead().encToken) {
            Kwd.addOp -> addString()
            else -> inp.expected("add string operator")
        }
    }
}

/**
 * parse a string term
 * <s-term> ::= <string-literal> | <string-variable>
 */
fun parseStringTerm() {
    when (inp.lookahead().encToken) {
        Kwd.string -> parseStringLiteral()
        Kwd.identifier -> parseStringIdentifier()
        else -> inp.expected("string literal or string identifier")
    }
}

/** parse string literal */
fun parseStringLiteral() {
    val stringValue = inp.match(Kwd.string).value
    // save the string in the map of constant strings
    val strAddr = stringCnstPrfx + (++stringCnstIndx).toString()
    stringConstants[strAddr] = stringValue
    code.getVarAddress(strAddr)
}

/** parse string identifier */
fun parseStringIdentifier() {
    when (inp.lookahead().type) {
        TokType.variable -> parseStringVariable()
        TokType.function -> parseStringFunctionCall()
        else -> abort("line ${inp.currentLineNumber}: identifier ${inp.lookahead().value} not declared")
    }
}

/** parse string function call */
fun parseStringFunctionCall() {}

/** parse string varaible */
fun parseStringVariable() {
    val strVarName = inp.match(Kwd.identifier).value
    code.getVarAddress(strVarName)
}

/** add strings */
fun addString() {

}

/**
 * parse string assignment
 * <s-assignment> ::= <s-variable> = <s-expression>
 */
fun parseStringAssignment() {
    val identName: String = inp.match(Kwd.identifier).value
    if (identifiersSpace[identName]?.canAssign == false)
        abort ("line ${inp.currentLineNumber}: variable $identName cannot be assigned a value")
    inp.match(Kwd.equalsOp)
    parseBooleanExpression()
    code.assignment(identName)
}
package mpdev.compiler.chapter_14_xiv

/** parse an expression of any type and branch to boolean/string or other appropriate expression */
fun parseAnyExpression(): VarType {
    if (inp.lookahead().encToken == Kwd.string) {    // string literal
        parseStringExpression()
        return VarType.string
    }
    if (inp.lookahead().encToken == Kwd.number || inp.lookahead().encToken == Kwd.leftParen) {    // numeric expression
        parseBooleanExpression()
        return VarType.int
    }
    if (inp.lookahead().encToken == Kwd.identifier) {
        when (getType(inp.lookahead().value)) {
            VarType.string -> {
                parseStringExpression()
                return VarType.string
            }
            VarType.int -> {
                parseBooleanExpression()
                return VarType.int
            }
            VarType.void -> abort("line ${inp.currentLineNumber}: function cannot return void")
            else -> abort("line ${inp.currentLineNumber}: identifier [${inp.lookahead().value}] not declared")
        }
    }
    else
        inp.expected("valid expression")

    return VarType.none     // dummy return
}


/**
 * parse a string expression
 * <s-expression> ::= <s-term> [ + <s-term> ]
 */
fun parseStringExpression() {
    parseStringTerm()
    while (inp.lookahead().type == TokType.addOps) {
        code.saveString()
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
    val strAddr = STRING_CONST_PREFIX + (++stringCnstIndx).toString()
    stringConstants[strAddr] = stringValue
    code.getStringAddress(strAddr)
}

/** parse string identifier */
fun parseStringIdentifier() {
    if (getType(inp.lookahead().value) != VarType.string)
        inp.expected("identifier of type string")
    when (inp.lookahead().type) {
        TokType.variable -> parseStringVariable()
        TokType.function -> parseStringFunctionCall()
        else -> abort("line ${inp.currentLineNumber}: identifier ${inp.lookahead().value} not declared")
    }
}

/** parse string function call */
fun parseStringFunctionCall() {
    val functionName = inp.match(Kwd.identifier).value
    inp.match(Kwd.leftParen)
    inp.match(Kwd.rightParen)
    code.callFunction(functionName)
}

/** parse string variable */
fun parseStringVariable() {
    val strVarName = inp.match(Kwd.identifier).value
    code.getStringAddress(strVarName)
}

/** add strings */
fun addString() {
    inp.match()
    parseStringTerm()
    code.addString()
}

/** parse assignment of any type */
fun parseAnyAssignment() {
    val varName = inp.lookahead().value
    if (identifiersSpace[varName]?.type == VarType.string)
        parseStringAssignment()
    else
        parseAssignment()
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
    parseStringExpression()
    code.assignmentString(identName)
}
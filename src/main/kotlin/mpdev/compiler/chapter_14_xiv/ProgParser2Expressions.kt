package mpdev.compiler.chapter_14_xiv

/**
 * Program parsing - module
 * Numerical Expressions
 */

/**
 * parse assignment
 * <assignment> ::= <identifier> = <b-expression>
 */
fun parseAssignment() {
    val identName: String = inp.match(Kwd.identifier).value
    checkCanAssign(identName)
    val typeVar = getType(identName)
    inp.match(Kwd.equalsOp)
    //parseBooleanExpression()
    val typeExp = parseExpression()
    if (incompatibleTypes(typeVar, typeExp))
        abort ("line ${inp.currentLineNumber}: $typeVar variable cannot be assigned $typeExp value")
    when (typeVar) {
        DataType.int -> parseNumAssignment(identName)
        DataType.string -> parseStringAssignment(identName)
        else -> {}
    }
}

/** check if variable can be assigned a value */
fun checkCanAssign(identName: String) {
    if (identifiersMap[identName]?.canAssign == false)
        abort ("line ${inp.currentLineNumber}: variable $identName cannot be assigned a value")
}

/**
 * parse a numeric expression
 * <expression> ::= <term> [ <addop> <term> ] *
 * returns the data type of the expression
 */
fun parseExpression(): DataType {
    val typeT1 = parseTerm()
    while (inp.lookahead().type == TokType.addOps) {
        if (typeT1 == DataType.string)
            code.saveString()
        else
            code.saveAccumulator()
        when (inp.lookahead().encToken) {
            Kwd.addOp -> add(typeT1)
            Kwd.subOp -> subtract(typeT1)
            else -> inp.expected("add or subtract operator")
        }
    }
    return typeT1
}

/**
 * parse a term
 * <term> ::= <signed factor> [ <mulop> <factor> ] *
 * returns the data type of the term
 */
fun parseTerm(): DataType {
    val typeF1 = parseSignedFactor()
    while (inp.lookahead().type == TokType.mulOps) {
        if (typeF1 == DataType.string)
            code.saveString()
        else
            code.saveAccumulator()
        when (inp.lookahead().encToken) {
            Kwd.mulOp -> multiply(typeF1)
            Kwd.divOp -> divide(typeF1)
            else -> inp.expected("multiply or divide operator")
        }
    }
    return typeF1
}

/**
 * parse a signed factor
 * this can be only the first factor in a term
 * <signed factor> ::= [ addop ] <factor>
 */
fun parseSignedFactor(): DataType {
    var factType = DataType.void
    if (inp.lookahead().encToken == Kwd.addOp)
        inp.match()
    if (inp.lookahead().encToken == Kwd.subOp) {
        inp.match()
        if (inp.lookahead().encToken == Kwd.number)
            code.setAccumulator("-${inp.match(Kwd.number).value}")
        else {
            factType = parseFactor()
            code.negateAccumulator()
        }
    }
    else
        factType = parseFactor()
    return factType
}

/**
 * parse a factor
 * <factor> ::= ( <expression> ) | <integer> | <identifier>
 * returns the data type of factor
 */
fun parseFactor(): DataType {
    when (inp.lookahead().encToken) {
        Kwd.leftParen -> return parseParenExpression()
        Kwd.identifier -> return parseIdentifier()
        Kwd.number -> return parseNumber()
        Kwd.string -> return parseStringLiteral()
        else -> inp.expected("valid factor (num or string)")
    }
    return DataType.void    // dummy instruction
}

/**
 * parse a parenthesised expression
 * returns the data type of the parenth. expression
 */
fun parseParenExpression(): DataType {
    inp.match()
    val expType = parseExpression()
    inp.match(Kwd.rightParen)
    return expType
}

/**
 * parse an identifier
 * <identifier> ::= <variable> | <function>
 * returns the data type of the identifier
 */
fun parseIdentifier(): DataType {
    when (inp.lookahead().type) {
        TokType.variable -> return parseVariable()
        TokType.function -> return parseFunctionCall()
        else -> abort("line ${inp.currentLineNumber}: undeclared identifier [${inp.lookahead().value}]")
    }
    return DataType.void    // dummy instruction
}

/**
 * parse a function call
 * <function_call> ::= <function_name> ( )
 * returns the data type of the function
 */
fun parseFunctionCall(): DataType {
    val funcName = inp.match(Kwd.identifier).value
    inp.match(Kwd.leftParen)
    inp.match(Kwd.rightParen)
    code.callFunction(funcName)
    return getType(funcName)
}

/**
 * parse a reference to a variable
 * different code generated for local or global variable
 * returns the data type of the variable
 */
fun parseVariable(): DataType {
    when (getType(inp.lookahead().value)) {
        DataType.int -> return parseNumVariable()
        DataType.string -> return parseStringVariable()
        else -> {return DataType.void}
    }
}

/** parse an addition */
fun add(typeT1: DataType) {
    inp.match()
    val typeT2 = parseTerm()
    if (incompatibleTypes(typeT1, typeT2))
        abort ("line ${inp.currentLineNumber}: cannot add $typeT2 to $typeT1")
    when (typeT1) {
        DataType.int -> addNumber()
        DataType.string -> addString()
        else -> {}
    }
}

/** parse a subtraction */
fun subtract(typeT1: DataType) {
    inp.match()
    val typeT2 = parseTerm()
    if (incompatibleTypes(typeT1, typeT2))
        abort ("line ${inp.currentLineNumber}: cannot subtract $typeT2 from $typeT1")
    when (typeT1) {
        DataType.int -> subtractNumber()
        DataType.string -> abort ("line ${inp.currentLineNumber}: subtraction not supported for strings")
        else -> {}
    }
}

/** parse a multiplication */
fun multiply(typeF1: DataType) {
    inp.match()
    val typeF2 = parseFactor()
    if (incompatibleTypes(typeF1, typeF2))
        abort ("line ${inp.currentLineNumber}: cannot multiply $typeF1 by $typeF2")
    when (typeF1) {
        DataType.int -> multiplyNumber()
        DataType.string -> abort ("line ${inp.currentLineNumber}: multiplication not supported for strings")
        else -> {}
    }
}

/** parse a division */
fun divide(typeF1: DataType) {
    inp.match()
    val typeF2 = parseFactor()
    if (incompatibleTypes(typeF1, typeF2))
        abort ("line ${inp.currentLineNumber}: cannot divide $typeF1 by $typeF2")
    when (typeF1) {
        DataType.int -> divideNumber()
        DataType.string -> abort ("line ${inp.currentLineNumber}: division not supported for strings")
        else -> {}
    }
}

/**
 * check for compatible data types
 * at this stage string is not compatible with intare
 * while string with string and int with int
 */
fun incompatibleTypes(t1: DataType, t2: DataType): Boolean {
    return t1 == DataType.string && t2 != DataType.string
            || t2 == DataType.string && t1 != DataType.string
}
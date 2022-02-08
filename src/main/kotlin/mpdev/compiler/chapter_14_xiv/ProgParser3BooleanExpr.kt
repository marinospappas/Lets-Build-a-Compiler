package mpdev.compiler.chapter_14_xiv

/**
 * Program parsing - module 3
 *
 * parse a boolean expression
 * following is the whole grammar - note how <relation> links to <expression>
 *     which is the link between Boolean and Numerical expressions
 * with this grammar any integer can be boolean and vice-versa
 * any assignment will look for a boolean expression to start with
 * and can settle for a numeric one as it goes down the grammar
 *
 * <b-expression> ::= <b-term> [ <orop> <b-term> ] *
 * <b-term> ::= <not-factor> [ <andop> <not-factor> ] *
 * <not-factor> ::= [ <notop> ] <b-factor>
 * <b-factor> ::= <b-literal> | <b-variable> | <relation>
 * <relation> ::= <expression> [ <relop> <expression ]
 * <expression> ::= <term> [ <addop> <term> ] *
 * <term> ::= <signed factor> [ <mulop> factor ] *
 * <signed factor> ::= [ <addop> ] <factor>
 * <factor> ::= <integer> | <identifier> | ( <expression> )
 *
 */

/** parse a Boolean expression */
fun parseBooleanExpression(): DataType {
    val typeT1 = parseBooleanTerm()
    while (inp.lookahead().type == TokType.orOps) {
        code.saveAccumulator()
        when (inp.lookahead().encToken) {
            Kwd.orOp -> boolOr(typeT1)
            Kwd.xorOp -> boolXor(typeT1)
            else -> inp.expected("boolean or or xor operator")
        }
    }
    return typeT1
}

/** parse a boolean term */
fun parseBooleanTerm(): DataType {
    val typeF1 = parseNotFactor()
    while (inp.lookahead().type == TokType.andOps) {
        code.saveAccumulator()
        when (inp.lookahead().encToken) {
            Kwd.andOp -> boolAnd(typeF1)
            else -> inp.expected("boolean and operator")
        }
    }
    return typeF1
}

/** parse a not factor */
fun parseNotFactor(): DataType {
    val typeF: DataType
    if (inp.lookahead().encToken == Kwd.notOp) {
        inp.match()
        typeF = parseBooleanFactor()
        code.booleanNotAccumulator()
    }
    else
        typeF = parseBooleanFactor()
    return typeF
}

/** parse a boolean factor */
fun parseBooleanFactor(): DataType {
    if (inp.lookahead().encToken == Kwd.booleanLit) {
        if (inp.match(Kwd.booleanLit).value == BOOLEAN_TRUE)
            code.setAccumulator("1")
        else
            code.clearAccumulator()
        return DataType.int
    }
    else
        return parseRelation()
}

/** parse a relation */
fun parseRelation(): DataType {
    val typeE1 = parseExpression()
    if (inp.lookahead().type == TokType.relOps) {
        if (typeE1 == DataType.string)
            code.saveString()
        else
            code.saveAccumulator()
        when (inp.lookahead().encToken) {
            Kwd.isEqual -> parseEquals(typeE1)
            Kwd.isNotEqual -> parseNotEquals(typeE1)
            Kwd.isLess -> parseLess(typeE1)
            Kwd.isLessOrEq -> parseLessEqual(typeE1)
            Kwd.isGreater -> parseGreater(typeE1)
            Kwd.isGreaterOrEq -> parseGreaterEqual(typeE1)
            else -> inp.expected("relational operator")
        }
        return DataType.int
    }
    else
        return typeE1
}

/** parse boolean or */
fun boolOr(typeE1: DataType) {
    inp.match()
    val typeE2 = parseBooleanTerm()
    if (incompatibleTypes(typeE1, typeE2))
        abort ("line ${inp.currentLineNumber}: cannot 'or' $typeE1 with $typeE2")
    code.orAccumulator()
}

/** parse boolean xor */
fun boolXor(typeE1: DataType) {
    inp.match()
    val typeE2 = parseBooleanTerm()
    if (incompatibleTypes(typeE1, typeE2))
        abort ("line ${inp.currentLineNumber}: cannot 'xor' $typeE1 with $typeE2")
    code.xorAccumulator()
}

/** parse boolean and */
fun boolAnd(typeF1: DataType) {
    inp.match()
    val typeF2 = parseNotFactor()
    if (incompatibleTypes(typeF1, typeF2))
        abort ("line ${inp.currentLineNumber}: cannot 'and' $typeF1 with $typeF2")
    code.andAccumulator()
}

/** parse is equal to */
fun parseEquals(typeE1: DataType) {
    inp.match()
    val typeE2 = parseExpression()
    checkCompareTypeCompatibility(typeE1, typeE2)
    when (typeE1) {
        DataType.int -> code.compareEquals()
        DataType.string -> code.compareStringEquals()
        else -> {}
    }
}

/** parse is not equal to */
fun parseNotEquals(typeE1: DataType) {
    inp.match()
    val typeE2 = parseExpression()
    checkCompareTypeCompatibility(typeE1, typeE2)
    when (typeE1) {
        DataType.int -> code.compareNotEquals()
        DataType.string -> code.compareStringNotEquals()
        else -> {}
    }
}

/** parse is less than */
fun parseLess(typeE1: DataType) {
    inp.match()
    val typeE2 = parseExpression()
    checkCompareTypeCompatibility(typeE1, typeE2)
    when (typeE1) {
        DataType.int -> code.compareLess()
        DataType.string -> abort ("line ${inp.currentLineNumber}: compare 'less than' not supported for strings")
        else -> {}
    }
}

/** parse is less than or equal to */
fun parseLessEqual(typeE1: DataType) {
    inp.match()
    val typeE2 = parseExpression()
    checkCompareTypeCompatibility(typeE1, typeE2)
    when (typeE1) {
        DataType.int -> code.compareLessEqual()
        DataType.string -> abort ("line ${inp.currentLineNumber}: compare 'less than or equal to' not supported for strings")
        else -> {}
    }
}

/** parse is greater than */
fun parseGreater(typeE1: DataType) {
    inp.match()
    val typeE2 = parseExpression()
    checkCompareTypeCompatibility(typeE1, typeE2)
    when (typeE1) {
        DataType.int -> code.compareGreater()
        DataType.string -> abort ("line ${inp.currentLineNumber}: compare 'greater than' not supported for strings")
        else -> {}
    }
}

/** parse is greater than or equal to */
fun parseGreaterEqual(typeE1: DataType) {
    inp.match()
    val typeE2 = parseExpression()
    checkCompareTypeCompatibility(typeE1, typeE2)
    when (typeE1) {
        DataType.int -> code.compareGreaterEqual()
        DataType.string -> abort ("line ${inp.currentLineNumber}: compare 'greater than or equal to' not supported for strings")
        else -> {}
    }
}

/** check type compatibility */
fun checkCompareTypeCompatibility(t1: DataType, t2: DataType) {
    if (incompatibleTypes(t1, t2))
        abort("line ${inp.currentLineNumber}: cannot compare $t1 with $t2")
}
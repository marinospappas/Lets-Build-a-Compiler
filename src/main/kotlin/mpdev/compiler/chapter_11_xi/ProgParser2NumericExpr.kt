package mpdev.compiler.chapter_11_xi

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
    inp.match(Kwd.equalsOp)
    parseBooleanExpression()
    code.assignment(identName)
}

/**
 * parse a numeric expression
 * <expression> ::= <term> [ <addop> <term> ] *
 */
fun parseExpression() {
    parseTerm();
    while (inp.lookahead().type == TokType.addOps) {
        code.saveAccumulator()
        when (inp.lookahead().encToken) {
            Kwd.addOp -> add()
            Kwd.subOp -> subtract()
            else -> inp.expected("add or subtract operator")
        }
    }
}

/**
 * parse a term
 * <term> ::= <signed factor> [ <mulop> <factor> ] *
 */
fun parseTerm() {
    parseSignedFactor()
    while (inp.lookahead().type == TokType.mulOps) {
        code.saveAccumulator()
        when (inp.lookahead().encToken) {
            Kwd.mulOp -> multiply()
            Kwd.divOp -> divide()
            else -> inp.expected("multiply or divide operator")
        }
    }
}

/**
 * parse a signed factor
 * this can be only the first factor in a term
 * <signed factor> ::= [ addop ] <factor>
 */
fun parseSignedFactor() {
    if (inp.lookahead().encToken == Kwd.addOp)
        inp.match()
    if (inp.lookahead().encToken == Kwd.subOp) {
        inp.match()
        if (inp.lookahead().encToken == Kwd.number)
            code.setAccumulator("-${inp.match(Kwd.number).value}")
        else {
            parseFactor()
            code.negateAccumulator()
        }
    }
    else
        parseFactor()
}

/**
 * parse a factor
 * <factor> ::= ( <expression> ) | <integer> | <identifier>
 */
fun parseFactor() {
    if (inp.lookahead().encToken == Kwd.leftParen) {
        // ( Expression )
        inp.match()
        parseExpression()
        inp.match(Kwd.rightParen)
    }
    else
    if (inp.lookahead().encToken == Kwd.identifier)
        // Identifier
        parseIdentifier()
    else
        // Number
        code.setAccumulator(inp.match(Kwd.number).value)
}

/**
 * parse an identifier
 * <identifier> ::= <variable> | <function>
 */
fun parseIdentifier() {
    val identName: String = inp.match(Kwd.identifier).value
    if (inp.lookahead().encToken == Kwd.leftParen) {
        // function
        inp.match()
        inp.match(Kwd.rightParen)
        code.callFunction(identName)
    }
    else
        // variable
        code.setAccumulatorToVar(identName)
}

/** parse an addition */
fun add() {
    inp.match()
    parseTerm()
    code.addToAccumulator()
}

/** parse a subtraction */
fun subtract() {
    inp.match()
    parseTerm()
    code.subFromAccumulator()
}

/** parse a multiplication */
fun multiply() {
    inp.match()
    parseFactor()
    code.multiplyAccumulator()
}

/** parse a division */
fun divide() {
    inp.match()
    parseFactor()
    code.divideAccumulator()
}
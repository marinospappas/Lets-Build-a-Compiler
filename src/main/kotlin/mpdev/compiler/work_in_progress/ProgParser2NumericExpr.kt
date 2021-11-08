package mpdev.compiler.work_in_progress

/**
 * Program parsing - module
 * Numerical Expressions
 */

/**
 *
 * parse assignment
 * <assignment> ::= <identifier> = <expression>
 */
fun parseAssignment() {
        val identName: String = inp.match(Kwd.identifier).value
        inp.match(Kwd.equalsOp)
        parseExpression()
        code.assignment(identName)
}

/**
 * parse a numeric expression
 * <expression> ::= <term> [ <addop> <term> ] *
 */
fun parseExpression() {
        if (inp.lookahead().subType == TokSubType.addOps)
        // "trick" to deal with -Expression or +Expression
                code.clearAccumulator()
        else
                parseTerm()
        while (inp.lookahead().subType == TokSubType.addOps) {
                code.saveAccumulator()
                when (inp.lookahead().encToken) {
                        Kwd.addOp -> add()
                        Kwd.subOp -> subtract()
                        else -> {}
                }
        }
}

/**
 * parse a term
 * <term> ::= <factor> [ <mulop> <factor> ] *
 */
fun parseTerm() {
        parseFactor()
        while (inp.lookahead().subType == TokSubType.mulOps) {
                code.saveAccumulator()
                when (inp.lookahead().encToken) {
                        Kwd.mulOp -> multiply()
                        Kwd.divOp -> divide()
                        else -> {}
                }
        }
}

/**
 * parse a factor
 * <factor> ::= ( <expression> ) | <number> | <identifier>
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
        if (inp.lookahead().encToken == Kwd.number) {
                // Number
                code.setAccumulator(inp.match(Kwd.number).value)
        }
        else
                abort("unrecognised token ${inp.lookahead().value}")
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
                code.callSubroutine(identName)
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
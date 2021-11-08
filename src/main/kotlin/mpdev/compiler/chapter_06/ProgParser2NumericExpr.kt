package mpdev.compiler.chapter_06

/**
 * Program parsing - module
 * Numerical Expressions
 */

/**
 * parse assignment
 * <assignment> ::= <identifier> = <b-expression>
 */
fun parseAssignment() {
        val identName: String = inp.getName()
        inp.match(equalsOp)
        parseBooleanExpression()
        code.assignment(identName)
}

/**
 * parse a numeric expression
 * <expression> ::= <term> [ <addop> <term> ] *
 */
fun parseExpression() {
        parseTerm()
        while (inp.isAddop(inp.nextChar)) {
                code.saveAccumulator()
                when (inp.nextChar) {
                        addOp -> add()
                        subOp -> subtract()
                }
        }
}

/**
 * parse a term
 * <term> ::= <signed factor> [ <mulop> <factor> ] *
 */
fun parseTerm() {
        parseSignedFactor()
        while (inp.isMulop(inp.nextChar)) {
                code.saveAccumulator()
                when (inp.nextChar) {
                        mulOp -> multiply()
                        divOp -> divide()
                }
        }
}

/**
 * parse a signed factor
 * this can be only the first factor in a term
 * <signed factor> ::= [ addop ] <factor>
 */
fun parseSignedFactor() {
        if (inp.nextChar == addOp)
                inp.match()
        if (inp.nextChar == subOp) {
                inp.match()
                if (inp.isNumeric(inp.nextChar))
                        code.setAccumulator("-${inp.getNumber()}")
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
 * <factor> ::= ( <expression> ) | <number> | <identifier>
 */
fun parseFactor() {
        if (inp.isLeftParen(inp.nextChar)) {
                // ( Expression )
                inp.match()
                parseExpression()
                inp.match(rightParen)
        }
        else
        if (inp.isAlpha(inp.nextChar))
                // Identifier
                parseIdentifier()
        else
                // Number
                code.setAccumulator(inp.getNumber())
}

/**
 * parse an identifier
 * <identifier> ::= <variable> | <function>
 */
fun parseIdentifier() {
        val identName: String = inp.getName()
        if (inp.isLeftParen(inp.nextChar)) {
                // function
                inp.match()
                inp.match(rightParen)
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
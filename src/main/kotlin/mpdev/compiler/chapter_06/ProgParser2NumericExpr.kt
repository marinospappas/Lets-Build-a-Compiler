package mpdev.compiler.chapter_06

/**
 * Program parsing - module
 * Numerical Expressions
 */

/**
 * parse assignment
 * <assignment> ::= <identifier> = <expression>
 */
fun parseAssignment() {
        var identName: String = inp.getName()
        inp.match(equalsOp)
        parseExpression()
        code.assignment(identName)
}

/**
 * parse a numeric expression
 * <expression> ::= <term> [ <addop> <term> ] *
 */
fun parseExpression() {
        if (inp.isAddop(inp.nextChar))
        // "trick" to deal with -Expression or +Expression
                code.clearAccumulator()
        else
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
 * <term> ::= <factor> [ <mulop> <factor> ] *
 */
fun parseTerm() {
        parseFactor()
        while (inp.isMulop(inp.nextChar)) {
                code.saveAccumulator()
                when (inp.nextChar) {
                        mulOp -> multiply()
                        divOp -> divide()
                }
        }
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
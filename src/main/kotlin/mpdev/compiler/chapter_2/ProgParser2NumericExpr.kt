package mpdev.compiler.chapter_2

/**
 * Program parsing - module
 *
 */

/**
 * parse a factor
 * <factor> ::= ( <expression> ) | <number>
 */
fun parseFactor() {
        if (inp.isLeftParen(inp.nextChar)) {
              inp.getNextChar()
              parseExpression()
              inp.match(rightParen)
        }
        else
              code.setAccumulator(inp.getNumber())
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

/** parse an addition */
fun add() {
        inp.getNextChar()
        parseTerm()
        code.addToAccumulator()
}

/** parse a subtraction */
fun subtract() {
        inp.getNextChar()
        parseTerm()
        code.subFromAccumulator()
}

/** parse a multiplication */
fun multiply() {
        inp.getNextChar()
        parseFactor()
        code.multiplyAccumulator()
}

/** parse a division */
fun divide() {
        inp.getNextChar()
        parseFactor()
        code.divideAccumulator()
}
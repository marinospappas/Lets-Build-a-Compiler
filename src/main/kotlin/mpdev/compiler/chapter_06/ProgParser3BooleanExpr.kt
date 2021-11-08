package mpdev.compiler.chapter_06

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
 * <b-term> ::= <not-factor> [ AND <not-factor> ] *
 * <not-factor> ::= [ NOT ] <b-factor>
 * <b-factor> ::= <b-literal> | <b-variable> | <relation>
 * <relation> ::= <expression> [ <relop> <expression ]
 * <expression> ::= <term> [ <addop> <term> ] *
 * <term> ::= <signed factor> [ <mulop> factor ] *
 * <signed factor>::= [ <addop> ] <factor>
 * <factor> ::= <integer> | <variable> | ( <expression> )
 *
 */

/** parse a Boolean expression */
fun parseBooleanExpression() {
    parseBooleanTerm()
    while (inp.isOrop(inp.nextChar)) {
        code.saveAccumulator()
        when (inp.nextChar) {
            orOp -> boolOr()
            xorOp -> boolXor()
        }
    }
}

/** parse a boolean term */
fun parseBooleanTerm() {
    parseNotFactor()
    while (inp.isAndop(inp.nextChar)) {
        code.saveAccumulator()
        when (inp.nextChar) {
            andOp -> boolAnd()
        }
    }
}

/** parse a not factor */
fun parseNotFactor() {
    if (inp.nextChar == notOp) {
        inp.match()
        parseBooleanFactor()
        code.booleanNotAccumulator()
    }
    else
        parseBooleanFactor()
}

/** parse a boolean factor */
fun parseBooleanFactor() {
    if (inp.isBoolean(inp.nextChar)) {
        if (inp.getBoolean())
            code.setAccumulator("1")
        else
            code.clearAccumulator()
    }
    else
        parseRelation()
}

/** parse a relation */
fun parseRelation() {
    parseExpression()
    if (inp.isRelop(inp.nextChar)) {
        code.saveAccumulator()
        when (inp.nextChar) {
            isEqual -> parseEquals()
            isNotequal -> parseNotEquals()
            isLess -> parseLess()
            isGreater -> parseGreater()
        }
    }
}

/** parse boolean or */
fun boolOr() {
    inp.match()
    parseBooleanTerm()
    code.orAccumulator()
}

/** parse boolean xor */
fun boolXor() {
    inp.match()
    parseBooleanTerm()
    code.xorAccumulator()
}

/** parse boolean and */
fun boolAnd() {
    inp.match()
    parseNotFactor()
    code.andAccumulator()
}

/** parse is equal to */
fun parseEquals() {
    inp.match()
    parseExpression()
    code.compareEquals()
}

/** parse is not equal to */
fun parseNotEquals() {
    inp.match()
    parseExpression()
    code.compareNotEquals()
}

/** parse is less than */
fun parseLess() {
    inp.match()
    parseExpression()
    code.compareLess()
}

/** parse is greater than */
fun parseGreater() {
    inp.match()
    parseExpression()
    code.compareGreater()
}
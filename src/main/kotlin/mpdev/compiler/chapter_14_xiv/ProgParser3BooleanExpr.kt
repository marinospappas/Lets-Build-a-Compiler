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
fun parseBooleanExpression() {
    parseBooleanTerm()
    while (inp.lookahead().type == TokType.orOps) {
        code.saveAccumulator()
        when (inp.lookahead().encToken) {
            Kwd.orOp -> boolOr()
            Kwd.xorOp -> boolXor()
            else -> inp.expected("boolean or or xor operator")
        }
    }
}

/** parse a boolean term */
fun parseBooleanTerm() {
    parseNotFactor()
    while (inp.lookahead().type == TokType.andOps) {
        code.saveAccumulator()
        when (inp.lookahead().encToken) {
            Kwd.andOp -> boolAnd()
            else -> inp.expected("boolean and operator")
        }
    }
}

/** parse a not factor */
fun parseNotFactor() {
    if (inp.lookahead().encToken == Kwd.notOp) {
        inp.match()
        parseBooleanFactor()
        code.booleanNotAccumulator()
    }
    else
        parseBooleanFactor()
}

/** parse a boolean factor */
fun parseBooleanFactor() {
    if (inp.lookahead().encToken == Kwd.booleanLit) {
        if (inp.match(Kwd.booleanLit).value == BOOLEAN_TRUE)
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
    if (inp.lookahead().type == TokType.relOps) {
        code.saveAccumulator()
        when (inp.lookahead().encToken) {
            Kwd.isEqual -> parseEquals()
            Kwd.isNotEqual -> parseNotEquals()
            Kwd.isLess -> parseLess()
            Kwd.isLessOrEq -> parseLessEqual()
            Kwd.isGreater -> parseGreater()
            Kwd.isGreaterOrEq -> parseGreaterEqual()
            else -> inp.expected("relational operator")
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

/** parse is less than or equal to */
fun parseLessEqual() {
    inp.match()
    parseExpression()
    code.compareLessEqual()
}

/** parse is greater than */
fun parseGreater() {
    inp.match()
    parseExpression()
    code.compareGreater()
}

/** parse is greater than or equal to */
fun parseGreaterEqual() {
    inp.match()
    parseExpression()
    code.compareGreaterEqual()
}
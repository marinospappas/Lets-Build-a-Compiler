package mpdev.compiler.chapter_4

// the variables' space
var variable: MutableMap<String, Int> = mutableMapOf()

/** parse an assignment */
fun parseAssignment(): Int {
    val identifier = inp.getName()
    inp.match(equalsOp)
    variable[identifier] = parseExpression()
    // return value used for testing purposes only
    return variable[identifier] ?: 0
}

/** parse a numeric expression */
fun parseExpression(): Int {
    var value: Int
    // provision for -Expression and +Expression
    if (inp.isAddop(inp.nextChar))
        value = 0
    else
        value = parseTerm()
    while (inp.isAddop(inp.nextChar)) {
        when (inp.nextChar) {
            addOp -> {
                inp.getNextChar()
                value += parseTerm()
            }
            subOp -> {
                inp.getNextChar()
                value -= parseTerm()
            }
        }
    }
    return value
}

/** parse e term */
fun parseTerm(): Int {
    var value: Int = parseFactor()
    while (inp.isMulop(inp.nextChar)) {
        when (inp.nextChar) {
            mulOp -> {
                inp.getNextChar()
                value *= parseFactor()
            }
            divOp -> {
                inp.getNextChar()
                value /= parseFactor()
            }
        }
    }
    return value
}

/** parse a factor */
fun parseFactor(): Int {
    val value: Int
    if (inp.isLeftParen(inp.nextChar)) {
        inp.getNextChar()
        value = parseExpression()
        inp.match(rightParen)
    }
    else if (inp.isAlpha(inp.nextChar)) {
        val varName = inp.getName()
        value = variable[varName] ?: abort("variable ${varName} not initialised")
    }
    else
        value = inp.getNumber()
    return value
}

/** input function */
fun input() {
    inp.getNextChar()
    val varName = inp.getName()
    println("enter value for [${varName}]")
    val s: String? = readLine()
    variable[varName] = s?.toInt() ?: 0
}

/** output function */
fun output() {
    inp.getNextChar()
    val varName = inp.getName()
    println("value of [${varName}] = ${variable[varName] ?: abort("variable ${varName} not initialised")}")
}
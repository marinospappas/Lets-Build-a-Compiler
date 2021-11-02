package mpdev.compiler.chapter_04

import kotlin.system.exitProcess

/**
 * A Simple Interpreter
 * Based on the Let's Build a Compiler! series by Jack Crenshaw
 * Version 1.0 21.10.2021
 */

// the input program scanner
lateinit var inp: InputProgramScanner

/** report an error */
fun error(errMsg: String) {
    System.err.println("Error: $errMsg")
}

/** abort compilation */
fun abort(errMsg: String): Int {
    error(errMsg)
    exitProcess(1)
}

/** print message and exit */
fun exit(msg: String) {
    System.err.println(msg)
    exitProcess(0)
}

/** report what was expected and abort */
fun expected(expMsg: String) = abort("Expected $expMsg \n found [${inp.nextChar}]")

/** compiler initialisation */
fun initInterpreter(args: Array<String>) {
    if (args.isEmpty())
        abort("no input file specified")
    // get a new input program scanner object - initialise from input file
    inp = InputProgramScanner(args[0])
}

/** main function */
fun main(args: Array<String>) {
    initInterpreter(args)
    while (true) {
        when (inp.nextChar) {
            inToken -> input();
            outToken -> output()
            else -> parseAssignment()
        }
        inp.skipNewline()
    }
}

package mpdev.compiler.chapter_1

import kotlin.system.exitProcess

/**
 * A Simple Compiler
 * Based on the Let's Build a Compiler! series by Jack Crenshaw
 * This version produces Assembly language code for the 68000 microprocessor
 * A future version will produce code for the TI59 Calculator
 * It will produce output that uses the TI59 mnemonics that can be keyed directly to the calculator
 * Version 1.0 01.10.2021
 */

// the input program scanner
lateinit var inp: InputProgramScanner

/** report an error */
fun error(errMsg: String) {
    System.err.println("Error: $errMsg")
}

/** abort compilation */
fun abort(errMsg: String) {
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
fun initCompiler(args: Array<String>) {
    if (args.isEmpty())
        abort("no input file specified")
    // get a new input program scanner object - initialise from input file
    inp = InputProgramScanner(args[0])
    inp.getNextChar()
}

/** main function */
fun main(args: Array<String>) {
    initCompiler(args)
}
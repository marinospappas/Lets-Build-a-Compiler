package mpdev.compiler.chapter_14_xiv

import java.io.File

// the pass and fail strings
const val PASS_STRING = "PASS"
const val FAIL_STRING = "FAIL"

// the directory that contains the test source programs
var testDir = ""

/** get expected result - compiler errors */
fun getExpectedErr(testName: String): String {
    var expResult = ""
    val testDirName = "testresources/$testDir"
    val errFile = "$testDirName.results/$testName.err"
    try {
        expResult = File(errFile).readText()
    } catch (ignored: Exception) {}
    return expResult
}

/** run compiler and get error message */
fun getActualError(testName: String): String {
    val srcFile = "$testDir/$testName.tnsl"
    return ExecuteCompiler().run(srcFile)
}

/** check for assembler output */
fun checkAsmOutput(testName: String): String {
    val expOutFile = "testresources/$testDir.results/$testName.out"
    val actualOutFile = "testresources/testout.s"
    if (File(expOutFile).exists())
        return compareFiles(expOutFile, actualOutFile)
    else
        return ""
}

/** compare two files line by line */
fun compareFiles(expFile: String, outFile: String): String {
    val expList = mutableListOf<String>()
    File(expFile).forEachLine { expList.add(it) }
    val outList = mutableListOf<String>()
    File(outFile).forEachLine { outList.add(it) }
    val upperLimit = Integer.min(expList.size, outList.size)
    for (i in 0 until upperLimit) {
        if (expList[i].startsWith("# compiled on"))
            continue
        if (expList[i] != outList[i]) {
            return "Assembly Line ${(i + 1)} - EXPECTED: ${expList[i]} - RECEIVED: ${outList[i]}"
        }
    }
    if (expList.size < outList.size) {
        return "Assembly Line ${upperLimit+1} - EXPECTED: end-of-file - RECEIVED: ${outList[upperLimit]}"
    }
    if (expList.size > outList.size) {
        return "Assembly Line ${upperLimit+1} - EXPECTED: ${expList[upperLimit]} - RECEIVED: end-of-file"
    }
    return ""
}
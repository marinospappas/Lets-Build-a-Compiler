package mpdev.compiler.chapter_14_xiv

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

val INTELLIJ_DIR = "/snap/intellij-idea-community/345/"
val COMPILER = mutableListOf("java",
    /* "-javaagent:/snap/intellij-idea-community/342/lib/idea_rt.jar=45231:/snap/intellij-idea-community/342/bin", */
    "-Dfile.encoding=UTF-8",
    "-classpath", "${PROJECT_DIR}out/production/CompilerV2:${INTELLIJ_DIR}plugins/Kotlin/kotlinc/lib/kotlin-stdlib.jar:${INTELLIJ_DIR}plugins/Kotlin/kotlinc/lib/kotlin-reflect.jar:${INTELLIJ_DIR}plugins/Kotlin/kotlinc/lib/kotlin-test.jar",
    "mpdev.compiler.chapter_14_xiv.CompilerMainKt",
    "-o")    // need to add outfile and sourcefile

class ExecuteCompiler {

    fun run(workingDir: String, srcFilename: String, outFilename: String): String {
        val pb = ProcessBuilder(COMPILER + outFilename + srcFilename)
        pb.directory(File(workingDir))
        val proc = pb.start()
        val reader = BufferedReader(InputStreamReader(proc.errorStream))
        val errMsg = reader.readLine()
        /*
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            println(line)
        } */
        val exitCode = proc.waitFor()
        return errMsg?:""
    }

}

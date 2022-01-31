package mpdev.compiler.chapter_14_xiv

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

val COMPILER = mutableListOf("/home/marinos/.jdks/openjdk-17.0.1/bin/java",
    "-javaagent:/snap/intellij-idea-community/345/lib/idea_rt.jar=40081:/snap/intellij-idea-community/345/bin",
    "-Dfile.encoding=UTF-8",
    "-classpath", "/home/marinos/IdeaProjects/CompilerV2/out/production/CompilerV2:/home/marinos/IdeaProjects/CompilerV2/lib/kotlin-stdlib.jar:/home/marinos/IdeaProjects/CompilerV2/lib/kotlin-reflect.jar:/home/marinos/IdeaProjects/CompilerV2/lib/kotlin-test.jar:/home/marinos/IdeaProjects/CompilerV2/lib/kotlin-stdlib-jdk7.jar:/home/marinos/IdeaProjects/CompilerV2/lib/kotlin-stdlib-jdk8.jar",
    "mpdev.compiler.chapter_14_xiv.CompilerMainKt",
    "-o")    // need to add outfile and sourcefile

class ExecuteCompiler {

    fun run(srcFilename: String, outFilename: String): String {
        val pb = ProcessBuilder(COMPILER + outFilename + srcFilename)
        pb.directory(File("testresources"))
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

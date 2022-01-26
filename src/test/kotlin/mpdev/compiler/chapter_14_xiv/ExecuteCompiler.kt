package mpdev.compiler.chapter_14_xiv

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

val COMPILER = mutableListOf("/home/marinos/.jdks/azul-13.0.9/bin/java",
    "-javaagent:/snap/intellij-idea-community/342/lib/idea_rt.jar=45231:/snap/intellij-idea-community/342/bin",
    "-Dfile.encoding=UTF-8",
    "-classpath", "/home/marinos/IdeaProjects/CompilerV1/out/production/CompilerV1:/home/marinos/IdeaProjects/CompilerV1/lib/kotlin-stdlib.jar:/home/marinos/IdeaProjects/CompilerV1/lib/kotlin-reflect.jar:/home/marinos/IdeaProjects/CompilerV1/lib/kotlin-test.jar:/home/marinos/IdeaProjects/CompilerV1/lib/kotlin-stdlib-jdk7.jar:/home/marinos/IdeaProjects/CompilerV1/lib/kotlin-stdlib-jdk8.jar",
    "mpdev.compiler.chapter_14_xiv.CompilerMainKt",
    "-o", "testout.s")

class ExecuteCompiler {

    fun run(filename: String): String {
        val pb = ProcessBuilder(COMPILER+filename)
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

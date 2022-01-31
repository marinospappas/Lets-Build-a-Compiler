package mpdev.compiler.chapter_14_xiv

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

val COMPILER = mutableListOf("C:\\Program Files\\Java\\jdk-14.0.1\\bin\\java.exe",
    "-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2020.1.2\\lib\\idea_rt.jar=62429:C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2020.1.2\\bin",
    "-Dfile.encoding=UTF-8",
    "-classpath", "C:\\Users\\marin\\java-projects\\CompilerV1\\build\\classes\\kotlin\\main;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.jetbrains.kotlin\\kotlin-stdlib-jdk8\\1.5.31\\ff5d99aecd328872494e8921b72bf6e3af97af3e\\kotlin-stdlib-jdk8-1.5.31.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.junit.jupiter\\junit-jupiter\\5.7.0\\3152d152da916ccbb0715f89f7f873f45362ad7f\\junit-jupiter-5.7.0.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.jetbrains.kotlin\\kotlin-stdlib-jdk7\\1.5.31\\77e0f2568912e45d26c31fd417a332458508acdf\\kotlin-stdlib-jdk7-1.5.31.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.jetbrains.kotlin\\kotlin-stdlib\\1.5.31\\6628d61d0f5603568e72d2d5915d2c034b4f1c55\\kotlin-stdlib-1.5.31.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.junit.jupiter\\junit-jupiter-params\\5.7.0\\521dbecace93d5d7ef13a74aab231befd7954424\\junit-jupiter-params-5.7.0.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.junit.jupiter\\junit-jupiter-api\\5.7.0\\b25f3815c4c1860a73041e733a14a0379d00c4d5\\junit-jupiter-api-5.7.0.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.jetbrains\\annotations\\13.0\\919f0dfe192fb4e063e7dacadee7f8bb9a2672a9\\annotations-13.0.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.jetbrains.kotlin\\kotlin-stdlib-common\\1.5.31\\43331609c7de811fed085e0dfd150874b157c32\\kotlin-stdlib-common-1.5.31.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.apiguardian\\apiguardian-api\\1.1.0\\fc9dff4bb36d627bdc553de77e1f17efd790876c\\apiguardian-api-1.1.0.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.junit.platform\\junit-platform-commons\\1.7.0\\84e309fbf21d857aac079a3c1fffd84284e1114d\\junit-platform-commons-1.7.0.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.opentest4j\\opentest4j\\1.2.0\\28c11eb91f9b6d8e200631d46e20a7f407f2a046\\opentest4j-1.2.0.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.junit.jupiter\\junit-jupiter-engine\\5.7.0\\d9044d6b45e2232ddd53fa56c15333e43d1749fd\\junit-jupiter-engine-5.7.0.jar;C:\\Users\\marin\\.gradle\\caches\\modules-2\\files-2.1\\org.junit.platform\\junit-platform-engine\\1.7.0\\eadb73c5074a4ac71061defd00fc176152a4d12c\\junit-platform-engine-1.7.0.jar",
    "mpdev.compiler.chapter_14_xiv.CompilerMainKt",
    "-o")    // need to add outfile and sourcefile

class ExecuteCompiler {

    fun run(srcFilename: String, outFilename: String): String {
        val pb = ProcessBuilder(COMPILER + outFilename + srcFilename)
        pb.directory(File("src/test/resources"))
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

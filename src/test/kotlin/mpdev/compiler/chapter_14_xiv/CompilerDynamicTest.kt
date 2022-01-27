package mpdev.compiler.chapter_14_xiv

import org.junit.jupiter.api.*
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import java.io.File
import java.util.stream.Stream
import kotlin.test.assertEquals

@DisplayName("Compiler Full Test")
@TestMethodOrder(OrderAnnotation::class)

class CompilerDynamicTest {


    @TestFactory
    fun `Overall Program Test`(): Stream<DynamicTest> {
        val tests = mutableListOf<DynamicTest>()
        testDir = "programtest"
        val filesList = getFilesList()
        for (file in filesList)
            tests.add(
                dynamicTest(file) {
                    val expError = getExpectedErr(file)
                    val actualError = getActualError(file)
                    assertEquals(expError, actualError, "Compiler Error Check")
                    val asmOut = checkAsmOutput(file)
                    assertEquals("", asmOut, "Compiler Output Check")
                }
            )
        return tests.stream()
    }

    fun getFilesList(): Stream<String> {
        val filesList = mutableListOf<String>()
        File("testresources/$testDir").walk().forEach { file ->
            if (file.isFile)
                filesList.add(file.nameWithoutExtension)
        }
        return filesList.stream()
    }

}

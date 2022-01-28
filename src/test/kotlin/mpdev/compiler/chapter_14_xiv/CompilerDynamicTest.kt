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

    private lateinit var h: ParameterizedTestHelper

    @TestFactory
    fun `Overall Program Test`(): Stream<DynamicTest> {
        val tests = mutableListOf<DynamicTest>()
        h = ParameterizedTestHelper("programtest")
        val filesList = getFilesList()
        for (file in filesList)
            tests.add(
                dynamicTest(file) {
                    val expError = h.getExpectedErr(file)
                    val actualError = h.getActualError(file)
                    assertEquals(expError, actualError, "Compiler Error Check")
                    val asmOut = h.checkAsmOutput(file)
                    assertEquals("", asmOut, "Compiler Output Check")
                }
            )
        return tests.stream()
    }

    private fun getFilesList(): Stream<String> {
        val filesList = mutableListOf<String>()
        File("testresources/${h.testDir}").walk().forEach { file ->
            if (file.isFile)
                filesList.add(file.nameWithoutExtension)
        }
        return filesList.stream()
    }

}

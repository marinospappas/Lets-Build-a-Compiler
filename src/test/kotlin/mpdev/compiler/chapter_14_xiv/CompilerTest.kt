package mpdev.compiler.chapter_14_xiv

import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.io.File
import java.util.stream.Stream
import kotlin.test.assertEquals

@DisplayName("Compiler Full Test")
@TestMethodOrder(OrderAnnotation::class)

class CompilerTest {


    @TestFactory
    fun `Overall Program Test`(): Stream<DynamicTest> {
        val tests = mutableListOf<DynamicTest>()
        testDir = "programtest"
        val filesList = getFilesList()
        for (file in filesList)
            tests.add(
                DynamicTest.dynamicTest(file) {
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

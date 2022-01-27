package mpdev.compiler.chapter_14_xiv

import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream
import kotlin.test.assertEquals

@DisplayName("Program Structure Tests")
@TestMethodOrder(OrderAnnotation::class)
@TestInstance(Lifecycle.PER_CLASS)

class ProgParser0Test_a {

    @ParameterizedTest
    @MethodSource("testCaseProvider")
    @Order(4)
    fun `Program Test`(testCase: TestCase, testReporter: TestReporter) {
        testDir = testCase.testDir
        val expError = getExpectedErr(testCase.testName)
        val actualError = getActualError(testCase.testName)
        assertEquals(expError, actualError, "Compiler Error Check")
        val asmOut = checkAsmOutput(testCase.testName)
        assertEquals("", asmOut, "Compiler Output Check")
        testReporter.publishEntry("${testCase.testName}: $PASS_STRING")
    }

    companion object {
        @JvmStatic
        fun testCaseProvider(): Stream<TestCase> {
            val testDirs = listOf("programtest","vartest","funtest","maintest")
            return getFilesList(testDirs)
        }
        @JvmStatic
        fun getFilesList(testDirs: List<String>): Stream<TestCase> {
            val filesList = mutableListOf<TestCase>()
            for (dir in testDirs) {
                File("testresources/$dir").walk().forEach { file ->
                    if (file.isFile)
                        filesList.add(TestCase(dir, file.nameWithoutExtension))
                }
            }
            return filesList.stream()
        }
    }

}

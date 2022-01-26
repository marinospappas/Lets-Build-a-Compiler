package mpdev.compiler.chapter_14_xiv

import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream
import kotlin.test.assertEquals

@DisplayName("Program Structure Tests")
@TestMethodOrder(OrderAnnotation::class)

class ProgParser0Test {

    @ParameterizedTest
    @MethodSource("programTestFileProvider")
    @Order(1)
    fun `Test Overall Program`(testName: String, testReporter: TestReporter) {
        val expError = getExpectedErr(testName)
        val actualError = getActualError(testName)
        assertEquals(expError, actualError, "Compiler Error Check")
        val asmOut = checkAsmOutput(testName)
        assertEquals("", asmOut, "Compiler Output Check")
        testReporter.publishEntry("$testName: $PASS_STRING")
    }

    @ParameterizedTest
    @MethodSource("varTestFileProvider")
    @Order(2)
    fun `Test Variables Declarations`(testName: String, testReporter: TestReporter) {
        val expError = getExpectedErr(testName)
        val actualError = getActualError(testName)
        assertEquals(expError, actualError, "Compiler Error Check")
        val asmOut = checkAsmOutput(testName)
        assertEquals("", asmOut, "Compiler Output Check")
        testReporter.publishEntry("$testName: $PASS_STRING")
    }

    @ParameterizedTest
    @MethodSource("funTestFileProvider")
    @Order(3)
    fun `Test Functions Declarations`(testName: String, testReporter: TestReporter) {
        val expError = getExpectedErr(testName)
        val actualError = getActualError(testName)
        assertEquals(expError, actualError, "Compiler Error Check")
        val asmOut = checkAsmOutput(testName)
        assertEquals("", asmOut, "Compiler Output Check")
        testReporter.publishEntry("$testName: $PASS_STRING")
    }

    @ParameterizedTest
    @MethodSource("mainTestFileProvider")
    @Order(4)
    fun `Test Main Block`(testName: String, testReporter: TestReporter) {
        val expError = getExpectedErr(testName)
        val actualError = getActualError(testName)
        assertEquals(expError, actualError, "Compiler Error Check")
        val asmOut = checkAsmOutput(testName)
        assertEquals("", asmOut, "Compiler Output Check")
        testReporter.publishEntry("$testName: $PASS_STRING")
    }

    companion object {
        @JvmStatic
        fun programTestFileProvider(): Stream<String> {
            testDir = "programtest"
            return getFilesList()
        }
        @JvmStatic
        fun varTestFileProvider(): Stream<String> {
            testDir = "vartest"
            return getFilesList()
        }
        @JvmStatic
        fun funTestFileProvider(): Stream<String> {
            testDir = "funtest"
            return getFilesList()
        }
        @JvmStatic
        fun mainTestFileProvider(): Stream<String> {
            testDir = "maintest"
            return getFilesList()
        }
        /** get the files list from a specific dir */
        @JvmStatic
        fun getFilesList(): Stream<String> {
            val filesList = mutableListOf<String>()
            File("testresources/$testDir").walk().forEach { file ->
                if (file.isFile)
                    filesList.add(file.nameWithoutExtension)
            }
            return filesList.stream()
        }
    }

}

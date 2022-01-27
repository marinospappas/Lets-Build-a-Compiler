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

class ProgParser0Test {

    @ParameterizedTest
    @MethodSource("programTestFileProvider")
    @Order(1)
    fun `Test Overall Program`(testName: String, testReporter: TestReporter) {
        runTest(testName, testReporter)
    }

    @ParameterizedTest
    @MethodSource("varTestFileProvider")
    @Order(2)
    fun `Test Variables Declarations`(testName: String, testReporter: TestReporter) {
        runTest(testName, testReporter)
    }

    @ParameterizedTest
    @MethodSource("funTestFileProvider")
    @Order(3)
    fun `Test Functions Declarations`(testName: String, testReporter: TestReporter) {
        runTest(testName, testReporter)
    }

    @ParameterizedTest
    @MethodSource("mainTestFileProvider")
    @Order(4)
    fun `Test Main Block`(testName: String, testReporter: TestReporter) {
        runTest(testName, testReporter)
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
    }

}

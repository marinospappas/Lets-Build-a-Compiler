package mpdev.compiler.chapter_14_xiv

import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream
import kotlin.test.assertEquals

@DisplayName("Full Compiler Test")
@TestInstance(Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation::class)

class FullCompilerTest {

    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    @Order(1)
    @DisplayName("Program Structure Tests")
    @TestMethodOrder(OrderAnnotation::class)
    inner class ProgParser0Test {

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

        // parameter provider functions
        fun programTestFileProvider(): Stream<String> {
            testDir = "programtest"
            return getFilesList()
        }
        fun varTestFileProvider(): Stream<String> {
            testDir = "vartest"
            return getFilesList()
        }
        fun funTestFileProvider(): Stream<String> {
            testDir = "funtest"
            return getFilesList()
        }
        fun mainTestFileProvider(): Stream<String> {
            testDir = "maintest"
            return getFilesList()
        }
    }

    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    @Order(2)
    @DisplayName("Control Structures Tests")
    @TestMethodOrder(OrderAnnotation::class)
    inner class ProgParser1Test {

        @ParameterizedTest
        @MethodSource("blockTestFileProvider")
        @Order(1)
        fun `Test Block Structure`(testName: String, testReporter: TestReporter) {
            runTest(testName, testReporter)
        }

        @ParameterizedTest
        @MethodSource("ifTestFileProvider")
        @Order(2)
        fun `Test If Structure`(testName: String, testReporter: TestReporter) {
            runTest(testName, testReporter)
        }

        @ParameterizedTest
        @MethodSource("whileTestFileProvider")
        @Order(3)
        fun `Test While Structure`(testName: String, testReporter: TestReporter) {
            runTest(testName, testReporter)
        }

        @ParameterizedTest
        @MethodSource("repeatTestFileProvider")
        @Order(4)
        fun `Test Repeat Structure`(testName: String, testReporter: TestReporter) {
            runTest(testName, testReporter)
        }

        @ParameterizedTest
        @MethodSource("breakTestFileProvider")
        @Order(5)
        fun `Test Break Statement`(testName: String, testReporter: TestReporter) {
            runTest(testName, testReporter)
        }

        @ParameterizedTest
        @MethodSource("readTestFileProvider")
        @Order(6)
        fun `Test Read Statement`(testName: String, testReporter: TestReporter) {
            runTest(testName, testReporter)
        }

        @ParameterizedTest
        @MethodSource("printTestFileProvider")
        @Order(7)
        fun `Test Print Statement`(testName: String, testReporter: TestReporter) {
            runTest(testName, testReporter)
        }

        @ParameterizedTest
        @MethodSource("returnTestFileProvider")
        @Order(8)
        fun `Test Return Statement`(testName: String, testReporter: TestReporter) {
            runTest(testName, testReporter)
        }

        @ParameterizedTest
        @MethodSource("forTestFileProvider")
        @Order(9)
        fun `Test For Structure`(testName: String, testReporter: TestReporter) {
            runTest(testName, testReporter)
        }

        // parameter provider functions
        fun blockTestFileProvider(): Stream<String> {
            testDir = "blocktest"
            return getFilesList()
        }
        fun ifTestFileProvider(): Stream<String> {
            testDir = "iftest"
            return getFilesList()
        }
        fun whileTestFileProvider(): Stream<String> {
            testDir = "whiletest"
            return getFilesList()
        }
        fun repeatTestFileProvider(): Stream<String> {
            testDir = "repeattest"
            return getFilesList()
        }
        fun breakTestFileProvider(): Stream<String> {
            testDir = "breaktest"
            return getFilesList()
        }
        fun readTestFileProvider(): Stream<String> {
            testDir = "readtest"
            return getFilesList()
        }
        fun printTestFileProvider(): Stream<String> {
            testDir = "printtest"
            return getFilesList()
        }
        fun returnTestFileProvider(): Stream<String> {
            testDir = "returntest"
            return getFilesList()
        }
        fun forTestFileProvider(): Stream<String> {
            testDir = "fortest"
            return getFilesList()
        }
    }

    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    @Order(3)
    @DisplayName("Numerical Expressions Tests")
    @TestMethodOrder(OrderAnnotation::class)
    inner class ProgParser2Test {

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

        // parameter provider functions
        fun programTestFileProvider(): Stream<String> {
            testDir = "programtest"
            return getFilesList()
        }
        fun varTestFileProvider(): Stream<String> {
            testDir = "vartest"
            return getFilesList()
        }
        fun funTestFileProvider(): Stream<String> {
            testDir = "funtest"
            return getFilesList()
        }
        fun mainTestFileProvider(): Stream<String> {
            testDir = "maintest"
            return getFilesList()
        }
    }

    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    @Order(4)
    @DisplayName("Boolean Expressions Tests")
    @TestMethodOrder(OrderAnnotation::class)
    inner class ProgParser3Test {

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

        // parameter provider functions
        fun programTestFileProvider(): Stream<String> {
            testDir = "programtest"
            return getFilesList()
        }
        fun varTestFileProvider(): Stream<String> {
            testDir = "vartest"
            return getFilesList()
        }
        fun funTestFileProvider(): Stream<String> {
            testDir = "funtest"
            return getFilesList()
        }
        fun mainTestFileProvider(): Stream<String> {
            testDir = "maintest"
            return getFilesList()
        }
    }

    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    @Order(5)
    @DisplayName("String Expressions Tests")
    @TestMethodOrder(OrderAnnotation::class)
    inner class ProgParser4Test {

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

        // parameter provider functions
        fun programTestFileProvider(): Stream<String> {
            testDir = "programtest"
            return getFilesList()
        }
        fun varTestFileProvider(): Stream<String> {
            testDir = "vartest"
            return getFilesList()
        }
        fun funTestFileProvider(): Stream<String> {
            testDir = "funtest"
            return getFilesList()
        }
        fun mainTestFileProvider(): Stream<String> {
            testDir = "maintest"
            return getFilesList()
        }
    }

}

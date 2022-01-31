package mpdev.compiler.chapter_14_xiv

import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Full Compiler Test")
@TestInstance(Lifecycle.PER_CLASS)

class FullCompilerTestV2 {

    @Disabled
    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Program Structure Tests")
    @TestMethodOrder(OrderAnnotation::class)
    inner class ProgParser0Test {

        private lateinit var h1: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("programTestFileProvider")
        @Order(1)
        fun `Test Overall Program`(testName: String, testReporter: TestReporter) {
            h1.runTest(testName, testReporter)
        }

        private lateinit var h2: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("varTestFileProvider")
        @Order(2)
        fun `Test Variables Declarations`(testName: String, testReporter: TestReporter) {
            h2.runTest(testName, testReporter)
        }

        private lateinit var h3: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("funTestFileProvider")
        @Order(3)
        fun `Test Functions Declarations`(testName: String, testReporter: TestReporter) {
            h3.runTest(testName, testReporter)
        }

        private lateinit var h4: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("mainTestFileProvider")
        @Order(4)
        fun `Test Main Block`(testName: String, testReporter: TestReporter) {
            h4.runTest(testName, testReporter)
        }

        // parameter provider functions
        private fun programTestFileProvider(): Stream<String> {
            h1 = ParameterizedTestHelper("programtest")
            return h1.getFilesList()
        }
        private fun varTestFileProvider(): Stream<String> {
            h2 = ParameterizedTestHelper("vartest")
            return h2.getFilesList()
        }
        private fun funTestFileProvider(): Stream<String> {
            h3 = ParameterizedTestHelper("funtest")
            return h3.getFilesList()
        }
        private fun mainTestFileProvider(): Stream<String> {
            h4 = ParameterizedTestHelper("maintest")
            return h4.getFilesList()
        }

        @AfterAll
        fun checkResults() {
            for (t in ParameterizedTestHelper.threadList) {
                t.join()
                val exc = ParameterizedTestHelper.resultMap[t.id]
                if (exc != null)
                    throw exc
            }
        }
    }

    @Disabled
    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Control Structures Tests")
    @TestMethodOrder(OrderAnnotation::class)
    inner class ProgParser1Test {

        private lateinit var h1: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("blockTestFileProvider")
        @Order(1)
        fun `Test Block Structure`(testName: String, testReporter: TestReporter) {
            h1.runTest(testName, testReporter)
        }

        private lateinit var h2: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("ifTestFileProvider")
        @Order(2)
        fun `Test If Structure`(testName: String, testReporter: TestReporter) {
            h2.runTest(testName, testReporter)
        }

        private lateinit var h3: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("whileTestFileProvider")
        @Order(3)
        fun `Test While Structure`(testName: String, testReporter: TestReporter) {
            h3.runTest(testName, testReporter)
        }

        private lateinit var h4: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("repeatTestFileProvider")
        @Order(4)
        fun `Test Repeat Structure`(testName: String, testReporter: TestReporter) {
            h4.runTest(testName, testReporter)
        }

        private lateinit var h5: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("breakTestFileProvider")
        @Order(5)
        fun `Test Break Statement`(testName: String, testReporter: TestReporter) {
            h5.runTest(testName, testReporter)
        }

        private lateinit var h6: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("readTestFileProvider")
        @Order(6)
        fun `Test Read Statement`(testName: String, testReporter: TestReporter) {
            h6.runTest(testName, testReporter)
        }

        private lateinit var h7: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("printTestFileProvider")
        @Order(7)
        fun `Test Print Statement`(testName: String, testReporter: TestReporter) {
            h7.runTest(testName, testReporter)
        }

        private lateinit var h8: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("returnTestFileProvider")
        @Order(8)
        fun `Test Return Statement`(testName: String, testReporter: TestReporter) {
            h8.runTest(testName, testReporter)
        }

        private lateinit var h9: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("forTestFileProvider")
        @Order(9)
        fun `Test For Structure`(testName: String, testReporter: TestReporter) {
            h9.runTest(testName, testReporter)
        }

        // parameter provider functions
        private fun blockTestFileProvider(): Stream<String> {
            h1 = ParameterizedTestHelper("blocktest")
            return h1.getFilesList()
        }
        private fun ifTestFileProvider(): Stream<String> {
            h2 = ParameterizedTestHelper("iftest")
            return h2.getFilesList()
        }
        private fun whileTestFileProvider(): Stream<String> {
            h3 = ParameterizedTestHelper("whiletest")
            return h3.getFilesList()
        }
        private fun repeatTestFileProvider(): Stream<String> {
            h4 = ParameterizedTestHelper("repeattest")
            return h4.getFilesList()
        }
        private fun breakTestFileProvider(): Stream<String> {
            h5 = ParameterizedTestHelper("breaktest")
            return h5.getFilesList()
        }
        private fun readTestFileProvider(): Stream<String> {
            h6 = ParameterizedTestHelper("readtest")
            return h6.getFilesList()
        }
        private fun printTestFileProvider(): Stream<String> {
            h7 = ParameterizedTestHelper("printtest")
            return h7.getFilesList()
        }
        private fun returnTestFileProvider(): Stream<String> {
            h8 = ParameterizedTestHelper("returntest")
            return h8.getFilesList()
        }
        private fun forTestFileProvider(): Stream<String> {
            h9 = ParameterizedTestHelper("fortest")
            return h9.getFilesList()
        }
    }

    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Numerical Expressions Tests")
    @TestMethodOrder(OrderAnnotation::class)
    inner class ProgParser2Test {

        @Test
        @Order(1)
        fun `Test Integer Numbers`(testReporter: TestReporter) {
            ParameterizedTestHelper("intnumtest").runAllTests(testReporter)
        }

        @Test
        @Order(2)
        fun `Test Integer Variables`(testReporter: TestReporter) {
            ParameterizedTestHelper("intvartest").runAllTests(testReporter)
        }

        @Disabled
        @Test
        @Order(3)
        fun `Test Integer Functions`(testReporter: TestReporter) {
            ParameterizedTestHelper("intfuntest").runAllTests(testReporter)
        }
    }

    @Disabled
    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Boolean Expressions Tests")
    @TestMethodOrder(OrderAnnotation::class)
    inner class ProgParser3Test {

        private lateinit var h1: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("programTestFileProvider")
        @Order(1)
        fun `Test Overall Program`(testName: String, testReporter: TestReporter) {
            h1.runTest(testName, testReporter)
        }

        // parameter provider functions
        private fun programTestFileProvider(): Stream<String> {
            h1 = ParameterizedTestHelper("programtest")
            return h1.getFilesList()
        }
    }

    @Disabled
    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("String Expressions Tests")
    @TestMethodOrder(OrderAnnotation::class)
    inner class ProgParser4Test {

        private lateinit var h1: ParameterizedTestHelper
        @ParameterizedTest
        @MethodSource("programTestFileProvider")
        @Order(1)
        fun `Test Overall Program`(testName: String, testReporter: TestReporter) {
            h1.runTest(testName, testReporter)
        }

        // parameter provider functions
        private fun programTestFileProvider(): Stream<String> {
            h1 = ParameterizedTestHelper("programtest")
            return h1.getFilesList()
        }
    }

}

package mpdev.compiler.chapter_14_xiv

import org.junit.jupiter.api.*

//@DisplayName("Numerical Expressions Tests")
//@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ProgParser2Test {

    @Test
    //@Order(1)
    fun testIntNumbers(testReporter: TestReporter) {
        ParameterizedTestHelper("intnumtest").runAllTests(testReporter)
    }

    @Test
    //@Order(2)
    fun testIntVars(testReporter: TestReporter) {
        ParameterizedTestHelper("intvartest").runAllTests(testReporter)
    }

    @Test
    //@Order(3)
    fun testIntFunc(testReporter: TestReporter) {
        ParameterizedTestHelper("intfuntest").runAllTests(testReporter)
    }

}
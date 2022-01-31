package mpdev.compiler.chapter_14_xiv

import org.junit.jupiter.api.TestInstance
import org.junit.Test
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test1 {

    @Test
    @MethodSource("fun1")
    fun test1() {
        assertEquals("123", "123", "haha")
    }

    fun fun1(): Stream<String> {
        return listOf("123").stream()
    }
}
package util.io

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class OutputWriterTest {

    // Small buffer (it grows to the 64-byte minimum) so tests also cross buffer flushes.
    private fun write(body: (OutputWriter) -> Unit): String {
        val out = ByteArrayOutputStream()
        OutputWriter(out, 8).use(body)
        return out.toString(Charsets.UTF_8)
    }

    @Test
    fun printsNumberLimitsWithOneMinusSign() {
        val text = write {
            it.printLine(Int.MIN_VALUE)
            it.printLine(Int.MAX_VALUE)
            it.printLine(Long.MIN_VALUE)
            it.printLine(0)
        }
        assertEquals("-2147483648\n2147483647\n-9223372036854775808\n0\n", text)
    }

    @Test
    fun printsArraysWithSeparator() {
        val text = write {
            it.printLine(intArrayOf(1, -2, 3))
            it.printLine(longArrayOf(4, 5), sep = ',')
            it.printLine(IntArray(0))
        }
        assertEquals("1 -2 3\n4,5\n\n", text)
    }

    @Test
    fun varargsJoinsMixedValuesWithSpaces() {
        assertEquals("a b 3 4 null 1.5 true\n", write { it.printLine("a", 'b', 3, 4L, null, 1.5, true) })
    }

    @Test
    fun typedOverloadsPrintWithoutSpaces() {
        val text = write {
            it.print(1)
            it.print(2L)
            it.print('c')
            it.print("d")
            it.print(true)
            it.printLine()
        }
        assertEquals("12cdtrue\n", text)
    }

    @Test
    fun writesNonAsciiAsUtf8() {
        val s = "x".repeat(70) + "éö"
        assertEquals("$s\né", write {
            it.printLine(s)
            it.print('é')
        })
    }

    @Test
    fun manyNumbersSurviveBufferFlushes() {
        val expected = buildString { for (i in -5000..5000) append(i * 7919L).append('\n') }
        assertEquals(expected, write { w -> for (i in -5000..5000) w.printLine(i * 7919L) })
    }

    @Test
    fun closeFlushesButKeepsStreamUsable() {
        val out = ByteArrayOutputStream()
        val w = OutputWriter(out)
        w.print(42)
        assertEquals(0, out.size())
        w.close()
        assertEquals("42", out.toString())
        w.print(7)
        w.flush()
        assertEquals("427", out.toString())
    }
}

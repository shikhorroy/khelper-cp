package util.io

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import kotlin.random.Random

class InputReaderTest {

    // A 4-byte buffer forces a refill inside almost every token, which tests the buffer edges.
    private fun reader(s: String) = InputReader(ByteArrayInputStream(s.toByteArray(Charsets.UTF_8)), 4)

    @Test
    fun readsIntLimitsAndSigns() {
        val r = reader("  -2147483648\t2147483647 +7 0 -0")
        assertEquals(Int.MIN_VALUE, r.nextInt())
        assertEquals(Int.MAX_VALUE, r.nextInt())
        assertEquals(7, r.nextInt())
        assertEquals(0, r.nextInt())
        assertEquals(0, r.nextInt())
    }

    @Test
    fun readsLongLimits() {
        val r = reader("-9223372036854775808 9223372036854775807")
        assertEquals(Long.MIN_VALUE, r.nextLong())
        assertEquals(Long.MAX_VALUE, r.nextLong())
    }

    @Test
    fun readsDoubles() {
        val r = reader("0.1 -2.5 1e-3")
        assertEquals(0.1, r.nextDouble())
        assertEquals(-2.5, r.nextDouble())
        assertEquals(1e-3, r.nextDouble())
    }

    @Test
    fun nonAsciiBytesDoNotLookLikeEndOfInput() {
        val r = reader("héllo wörld")
        assertEquals("héllo", r.next())
        assertEquals("wörld", r.next())
        assertFalse(r.hasNext())
    }

    @Test
    fun nextLineAfterTokenWorksWithCrLf() {
        val r = reader("3\r\n  line two\r\nlast")
        assertEquals(3, r.nextInt())
        assertEquals("  line two", r.nextLine())
        assertEquals("last", r.nextLine())
    }

    @Test
    fun emptyLineAfterNextLineIsNotSkipped() {
        val r = reader("3\nabc def\n\nx")
        assertEquals(3, r.nextInt())
        assertEquals("abc def", r.nextLine())
        assertEquals("", r.nextLine())
        assertEquals("x", r.nextLine())
    }

    @Test
    fun nextLineInMiddleOfLineReturnsTheRest() {
        val r = reader("3 abc def\r\n\r\nz")
        assertEquals(3, r.nextInt())
        assertEquals("abc def", r.nextLine())
        assertEquals("", r.nextLine())
        assertEquals("z", r.nextLine())
    }

    @Test
    fun trailingSpacesBeforeLineBreakAreSkipped() {
        val r = reader("3  \t\r\nabc\n4 \nx y \n")
        assertEquals(3, r.nextInt())
        assertEquals("abc", r.nextLine())
        assertEquals(4, r.nextInt())
        assertEquals("x y ", r.nextLine())
    }

    @Test
    fun crBeforeEndOfInputIsHandled() {
        val r = reader("5\r")
        assertEquals(5, r.nextInt())
        assertFalse(r.hasNext())
    }

    @Test
    fun hasNextDoesNotConsumeToken() {
        val r = reader(" 5 \n ")
        assertTrue(r.hasNext())
        assertTrue(r.hasNext())
        assertEquals(5, r.nextInt())
        assertFalse(r.hasNext())
    }

    @Test
    fun readsArrays() {
        val r = reader("3 1 2 3 2 -5 5")
        assertArrayEquals(intArrayOf(1, 2, 3), r.nextIntArray(r.nextInt()))
        assertArrayEquals(longArrayOf(-5, 5), r.nextLongArray(r.nextInt()))
    }

    @Test
    fun longTokenGrowsScratchBuffer() {
        val big = "a".repeat(10_000)
        assertEquals(big, reader("$big b").next())
    }

    @Test
    fun randomNumbersMatchToString() {
        val rnd = Random(42)
        val values = LongArray(5_000) { rnd.nextLong() }
        val text = values.joinToString(separator = "") { "$it" + if (rnd.nextBoolean()) " " else "\r\n" }
        val r = reader(text)
        for (v in values) assertEquals(v, r.nextLong())
        assertFalse(r.hasNext())
    }
}

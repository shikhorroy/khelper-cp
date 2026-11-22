package utils.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class InputReaderTest {

    // A 4-byte buffer forces a refill inside almost every token, which tests the buffer edges.
    private static InputReader reader(String s) {
        return new InputReader(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)), 4);
    }

    @Test
    void readsIntLimitsAndSigns() {
        InputReader r = reader("  -2147483648\t2147483647 +7 0 -0");
        assertEquals(Integer.MIN_VALUE, r.nextInt());
        assertEquals(Integer.MAX_VALUE, r.nextInt());
        assertEquals(7, r.nextInt());
        assertEquals(0, r.nextInt());
        assertEquals(0, r.nextInt());
    }

    @Test
    void readsLongLimits() {
        InputReader r = reader("-9223372036854775808 9223372036854775807");
        assertEquals(Long.MIN_VALUE, r.nextLong());
        assertEquals(Long.MAX_VALUE, r.nextLong());
    }

    @Test
    void readsDoublesExactly() {
        InputReader r = reader("0.1 -2.5 1e-3 3.25E2");
        assertEquals(0.1, r.nextDouble());
        assertEquals(-2.5, r.nextDouble());
        assertEquals(1e-3, r.nextDouble());
        assertEquals(325.0, r.nextDouble());
    }

    @Test
    void nonAsciiBytesDoNotLookLikeEndOfInput() {
        InputReader r = reader("héllo wörld");
        assertEquals("héllo", r.next());
        assertEquals("wörld", r.nextString());
        assertFalse(r.hasNext());
    }

    @Test
    void nextLineAfterTokenWorksWithCrLf() {
        InputReader r = reader("3\r\n  line two\r\nlast");
        assertEquals(3, r.nextInt());
        assertEquals("  line two", r.nextLine());
        assertEquals("last", r.nextLine());
    }

    @Test
    void nextLineAfterTokenWorksWithLf() {
        InputReader r = reader("3\nabc def\n\nx");
        assertEquals(3, r.nextInt());
        assertEquals("abc def", r.nextLine());
        assertEquals("", r.nextLine());
        assertEquals("x", r.nextLine());
    }

    @Test
    void hasNextDoesNotConsumeToken() {
        InputReader r = reader(" 5 \n ");
        assertTrue(r.hasNext());
        assertTrue(r.hasNext());
        assertEquals(5, r.nextInt());
        assertFalse(r.hasNext());
    }

    @Test
    void readsCharactersAndArrays() {
        InputReader r = reader(" x 3 1 2 3 2 -5 5");
        assertEquals('x', r.nextCharacter());
        assertArrayEquals(new int[]{1, 2, 3}, r.nextIntArray(r.nextInt()));
        assertArrayEquals(new long[]{-5, 5}, r.nextLongArray(r.nextInt()));
    }

    @Test
    void longTokenGrowsScratchBuffer() {
        String big = "a".repeat(10_000);
        assertEquals(big, reader(big + " b").next());
    }

    @Test
    void nextLineInMiddleOfLineReturnsTheRest() {
        InputReader r = reader("3 abc def\r\n\r\nz");
        assertEquals(3, r.nextInt());
        assertEquals("abc def", r.nextLine());
        assertEquals("", r.nextLine());
        assertEquals("z", r.nextLine());
    }

    @Test
    void crBeforeEndOfInputIsHandled() {
        InputReader r = reader("5\r");
        assertEquals(5, r.nextInt());
        assertFalse(r.hasNext());
    }

    @Test
    void trailingSpacesBeforeLineBreakAreSkipped() {
        InputReader r = reader("3  \t\r\nabc\n4 \nx y \n");
        assertEquals(3, r.nextInt());
        assertEquals("abc", r.nextLine());
        assertEquals(4, r.nextInt());
        assertEquals("x y ", r.nextLine());
    }
}

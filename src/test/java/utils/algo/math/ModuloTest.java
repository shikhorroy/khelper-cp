package utils.algo.math;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ModuloTest {

    private static final int P = 1_000_000_007;

    @Test
    void resultsAreInRangeForNegativeInput() {
        Modulo m = new Modulo(7);
        assertEquals(4, m.mod(-3));
        assertEquals(5, m.sub(2, 4));
        assertEquals(1, m.add(-3, -3));
        assertEquals(6, m.mult(-1, 1));
    }

    @Test
    void inverseTimesValueIsOneForRandomValues() {
        Modulo m = new Modulo(P);
        Random rnd = new Random(42);
        for (int i = 0; i < 1000; i++) {
            long a = rnd.nextLong() % P;
            if (a == 0) continue;
            long inv = m.inverse(a);
            assertTrue(inv >= 0 && inv < P);
            assertEquals(1, m.mult(a, inv));
        }
    }

    @Test
    void inverseWorksForCompositeModWhenCoprime() {
        Modulo m = new Modulo(1_000_000_000);
        assertEquals(1, m.mult(7, m.inverse(7)));
        assertEquals(1, m.mult(-3, m.inverse(-3)));
    }

    @Test
    void inverseThrowsWhenNotCoprime() {
        assertThrows(ArithmeticException.class, () -> new Modulo(10).inverse(4));
    }

    @Test
    void divisionWithNegativeDividend() {
        assertEquals(5, new Modulo(7).div(-6, 3));
    }

    @Test
    void nonPositiveModIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Modulo(0));
        assertThrows(IllegalArgumentException.class, () -> new Modulo(-5));
    }
}

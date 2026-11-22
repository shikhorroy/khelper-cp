package utils.algo.math;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GCDTest {

    @Test
    void primitiveOverloadsMatchBigIntegerOnRandomInput() {
        Random rnd = new Random(42);
        for (int i = 0; i < 100_000; i++) {
            int a = rnd.nextInt(2_000_001) - 1_000_000, b = rnd.nextInt(2_000_001) - 1_000_000;
            long expected = BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).longValue();
            assertEquals(expected, GCD.of(a, b));
            assertEquals(expected, GCD.of((long) a, (long) b));
        }
    }

    @Test
    void gcdOfNegativeInputIsPositive() {
        assertEquals(6, GCD.of(-12, 18));
        assertEquals(6L, GCD.of(12L, -18L));
    }

    @Test
    void gcdOfZeroAndZeroIsZero() {
        assertEquals(0, GCD.of(0, 0));
    }

    @Test
    void genericVersionKeepsShortType() {
        Object r = GCD.of(Short.valueOf((short) 12), Short.valueOf((short) 18));
        assertTrue(r instanceof Short);
        assertEquals((short) 6, r);
    }

    @Test
    void genericVersionKeepsIntegerLongAndBigIntegerTypes() {
        Integer gi = GCD.of(Integer.valueOf(12), Integer.valueOf(18));
        Long gl = GCD.of(Long.valueOf(12), Long.valueOf(18));
        BigInteger gb = GCD.of(BigInteger.valueOf(12), BigInteger.valueOf(18));
        Byte gby = GCD.of(Byte.valueOf((byte) 12), Byte.valueOf((byte) 18));
        assertEquals(6, gi);
        assertEquals(6L, gl);
        assertEquals(BigInteger.valueOf(6), gb);
        assertEquals((byte) 6, gby);
    }

    @Test
    void genericVersionRejectsDouble() {
        assertThrows(IllegalArgumentException.class, () -> GCD.of(Double.valueOf(2), Double.valueOf(4)));
    }
}

package utils.algo.math;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LCMTest {

    @Test
    void primitiveOverloadsMatchBigIntegerOnRandomInput() {
        Random rnd = new Random(42);
        for (int i = 0; i < 100_000; i++) {
            long a = rnd.nextInt(2_000_001) - 1_000_000, b = rnd.nextInt(2_000_001) - 1_000_000;
            BigInteger ba = BigInteger.valueOf(a), bb = BigInteger.valueOf(b);
            long expected = (a == 0 || b == 0) ? 0 : ba.multiply(bb).abs().divide(ba.gcd(bb)).longValue();
            assertEquals(expected, LCM.of(a, b));
        }
    }

    @Test
    void lcmOfZeroAndZeroIsZero() {
        assertEquals(0, LCM.of(0, 0));
        assertEquals(0L, LCM.of(0L, 9L));
    }

    @Test
    void lcmOfNegativeInputIsPositive() {
        assertEquals(12, LCM.of(-4, 6));
    }

    @Test
    void longLcmOfTwoLargePrimes() {
        assertEquals(1_000_000_007L * 998_244_353L, LCM.of(1_000_000_007L, 998_244_353L));
    }

    @Test
    void intOverflowThrowsInsteadOfTruncating() {
        assertThrows(ArithmeticException.class, () -> LCM.of(100_000, 99_999));
        assertThrows(ArithmeticException.class, () -> LCM.of(Integer.valueOf(100_000), Integer.valueOf(99_999)));
    }

    @Test
    void longOverflowThrows() {
        assertThrows(ArithmeticException.class, () -> LCM.of(Long.MAX_VALUE, Long.MAX_VALUE - 1));
    }

    @Test
    void genericVersionKeepsInputType() {
        Byte b = LCM.of(Byte.valueOf((byte) 4), Byte.valueOf((byte) 6));
        BigInteger big = LCM.of(BigInteger.valueOf(-4), BigInteger.valueOf(6));
        Long l = LCM.of(Long.valueOf(4), Long.valueOf(6));
        assertEquals((byte) 12, b);
        assertEquals(BigInteger.valueOf(12), big);
        assertEquals(12L, l);
    }

    @Test
    void genericVersionRejectsDouble() {
        assertThrows(IllegalArgumentException.class, () -> LCM.of(Double.valueOf(2), Double.valueOf(4)));
    }
}

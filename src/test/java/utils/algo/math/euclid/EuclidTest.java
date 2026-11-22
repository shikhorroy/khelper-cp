package utils.algo.math.euclid;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class EuclidTest {

    private static long bruteGcd(long a, long b) {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).longValue();
    }

    @Test
    void gcdOfNegativeInputIsPositive() {
        assertEquals(2, Euclid.gcd(4, -6));
        assertEquals(2, Euclid.gcd(-4, 6));
        assertEquals(2, Euclid.gcd(-4, -6));
    }

    @Test
    void gcdMatchesBigIntegerForSmallRange() {
        for (long a = -50; a <= 50; a++)
            for (long b = -50; b <= 50; b++)
                assertEquals(bruteGcd(a, b), Euclid.gcd(a, b), "gcd(" + a + ", " + b + ")");
    }

    @Test
    void gcdWithZero() {
        assertEquals(0, Euclid.gcd(0, 0));
        assertEquals(7, Euclid.gcd(0, -7));
        assertEquals(7, Euclid.gcd(7, 0));
    }

    @Test
    void lcmOfZeroIsZero() {
        assertEquals(0, Euclid.lcm(0, 0));
        assertEquals(0, Euclid.lcm(5, 0));
    }

    @Test
    void lcmOfNegativeInputIsPositive() {
        assertEquals(12, Euclid.lcm(-4, 6));
        assertEquals(12, Euclid.lcm(4, -6));
    }

    @Test
    void extendedGcdSatisfiesBezoutForAllSigns() {
        for (long a = -30; a <= 30; a++)
            for (long b = -30; b <= 30; b++) {
                Euclid.Tuple t = Euclid.extendedGCD(a, b);
                assertEquals(bruteGcd(a, b), t.g, "g for (" + a + ", " + b + ")");
                assertEquals(t.g, a * t.x + b * t.y, "Bezout for (" + a + ", " + b + ")");
            }
    }

    @Test
    void moduloInverseIsNormalizedIntoRange() {
        assertEquals(5, Euclid.moduloInverse(3, 7));
    }

    @Test
    void moduloInverseWorksForEveryUnitModPrime() {
        for (long a = 1; a < 13; a++) {
            long inv = Euclid.moduloInverse(a, 13);
            assertTrue(inv >= 0 && inv < 13);
            assertEquals(1, a * inv % 13);
        }
    }

    @Test
    void moduloInverseOfNegativeValue() {
        long inv = Euclid.moduloInverse(-3, 7);
        assertEquals(1, Math.floorMod(-3 * inv, 7));
    }

    @Test
    void moduloInverseThrowsWhenNotCoprime() {
        assertThrows(ArithmeticException.class, () -> Euclid.moduloInverse(2, 4));
    }

    @Test
    void moduloInverseThrowsForNonPositiveMod() {
        assertThrows(ArithmeticException.class, () -> Euclid.moduloInverse(3, 0));
    }
}

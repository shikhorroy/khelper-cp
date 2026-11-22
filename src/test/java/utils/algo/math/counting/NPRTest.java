package utils.algo.math.counting;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class NPRTest {

    private static final int P = 1_000_000_007;

    private static long brute(int n, int r, long mod) {
        if (r < 0 || r > n) return 0;
        long e = 1 % mod;
        for (int i = 0; i < r; i++) e = e * (n - i) % mod;
        return e;
    }

    @Test
    void matchesBruteForceForSmallN() {
        NPR npr = new NPR(20, P);
        for (int n = 0; n <= 20; n++)
            for (int r = 0; r <= 21; r++)
                assertEquals(brute(n, r, P), npr.calculate(n, r), "P(" + n + ", " + r + ")");
    }

    @Test
    void negativeRIsZero() {
        assertEquals(0, new NPR(10, P).calculate(5, -1));
    }

    @Test
    void smallPrimeModWithNAtLeastMod() {
        NPR npr = new NPR(20, 7);
        for (int n = 0; n <= 20; n++)
            for (int r = 0; r <= n; r++)
                assertEquals(brute(n, r, 7), npr.calculate(n, r), "P(" + n + ", " + r + ") mod 7");
    }

    @Test
    void matchesBigIntegerForPrime13UpTo200() {
        NPR npr = new NPR(200, 13);
        for (int n = 0; n <= 200; n++) {
            BigInteger p = BigInteger.ONE;
            for (int r = 0; r <= n; r++) {
                assertEquals(p.mod(BigInteger.valueOf(13)).intValue(), npr.calculate(n, r));
                p = p.multiply(BigInteger.valueOf(n - r));
            }
        }
    }

    @Test
    void compositeModFallsBackToOneInversePerQuery() {
        NPR npr = new NPR(10, 1_000_000_000);
        assertEquals(1, npr.calculate(1, 1));
        assertThrows(ArithmeticException.class, () -> npr.calculate(5, 2));
    }

    @Test
    void singleArgumentUsesMaxN() {
        assertEquals(20, new NPR(5, P).calculate(2));
    }

    @Test
    void invalidConstructorArgumentsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new NPR(-1, P));
        assertThrows(IllegalArgumentException.class, () -> new NPR(5, -7));
    }
}

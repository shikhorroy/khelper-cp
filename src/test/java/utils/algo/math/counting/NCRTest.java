package utils.algo.math.counting;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NCRTest {

    private static final int P = 1_000_000_007;

    @Test
    void matchesPascalTriangleForSmallN() {
        NCR ncr = new NCR(30, P);
        long[][] c = BinomialCoefficient.calculate(30);
        for (int n = 0; n <= 30; n++)
            for (int r = 0; r <= 31; r++)
                assertEquals(r > n ? 0 : c[n][r] % P, ncr.calculate(n, r), "C(" + n + ", " + r + ")");
    }

    @Test
    void negativeRIsZero() {
        assertEquals(0, new NCR(10, P).calculate(5, -1));
    }

    @Test
    void negativeNIsZero() {
        assertEquals(0, new NCR(10, P).calculate(-1, 0));
    }

    @Test
    void largeTableMatchesBigIntegerOnRandomQueries() {
        NCR ncr = new NCR(1_000_000, P);
        Random rnd = new Random(42);
        for (int it = 0; it < 20; it++) {
            int n = rnd.nextInt(2000), r = rnd.nextInt(n + 1);
            BigInteger c = BigInteger.ONE;
            for (int i = 0; i < r; i++) c = c.multiply(BigInteger.valueOf(n - i)).divide(BigInteger.valueOf(i + 1));
            assertEquals(c.mod(BigInteger.valueOf(P)).intValue(), ncr.calculate(n, r));
        }
        assertEquals(1, ncr.calculate(0));
        assertEquals(1_000_000, ncr.calculate(1));
    }

    @Test
    void smallPrimeModWithNAtLeastModUsesLucas() {
        NCR ncr = new NCR(30, 7);
        long[][] c = BinomialCoefficient.calculate(30);
        for (int n = 0; n <= 30; n++)
            for (int r = 0; r <= n; r++)
                assertEquals(c[n][r] % 7, ncr.calculate(n, r), "C(" + n + ", " + r + ") mod 7");
    }

    @Test
    void lucasMatchesBigIntegerForPrime13UpTo200() {
        NCR ncr = new NCR(200, 13);
        for (int n = 0; n <= 200; n++) {
            BigInteger c = BigInteger.ONE;
            for (int r = 0; r <= n; r++) {
                assertEquals(c.mod(BigInteger.valueOf(13)).intValue(), ncr.calculate(n, r));
                c = c.multiply(BigInteger.valueOf(n - r)).divide(BigInteger.valueOf(r + 1));
            }
        }
    }

    @Test
    void compositeModFallsBackToOneInversePerQuery() {
        NCR ncr = new NCR(10, 1_000_000_000);
        assertEquals(1, ncr.calculate(1, 1));
        assertThrows(ArithmeticException.class, () -> ncr.calculate(5, 2));
    }

    @Test
    void modOneIsAlwaysZero() {
        NCR ncr = new NCR(10, 1);
        for (int n = 0; n <= 10; n++)
            for (int r = 0; r <= n; r++)
                assertEquals(0, ncr.calculate(n, r));
    }

    @Test
    void nAboveMaxNThrows() {
        assertThrows(Error.class, () -> new NCR(5, P).calculate(6, 1));
    }

    @Test
    void invalidConstructorArgumentsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new NCR(-1, P));
        assertThrows(IllegalArgumentException.class, () -> new NCR(5, 0));
    }
}

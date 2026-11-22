package utils.algo.math.counting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinomialCoefficientTest {

    @Test
    void zeroRowHasOne() {
        assertEquals(1, BinomialCoefficient.calculate(0)[0][0]);
    }

    @Test
    void largestExactValueFitsInLong() {
        long[][] c = BinomialCoefficient.calculate(BinomialCoefficient.MAX_EXACT_N);
        assertEquals(7219428434016265740L, c[66][33]);
    }

    @Test
    void rowsSumToPowerOfTwo() {
        long[][] c = BinomialCoefficient.calculate(40);
        for (int n = 0; n <= 40; n++) {
            long sum = 0;
            for (int k = 0; k <= n; k++) sum += c[n][k];
            assertEquals(1L << n, sum);
        }
    }

    @Test
    void negativeMaxnIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> BinomialCoefficient.calculate(-1));
        assertThrows(IllegalArgumentException.class, () -> BinomialCoefficient.calculate(-1, 7));
    }

    @Test
    void modVersionMatchesExactValues() {
        long mod = 1_000_000_007L;
        long[][] exact = BinomialCoefficient.calculate(60);
        long[][] m = BinomialCoefficient.calculate(60, mod);
        for (int n = 0; n <= 60; n++)
            for (int k = 0; k <= n; k++)
                assertEquals(exact[n][k] % mod, m[n][k]);
    }

    @Test
    void modOneGivesAllZeros() {
        long[][] m = BinomialCoefficient.calculate(5, 1);
        for (long[] row : m) for (long v : row) assertEquals(0, v);
    }
}

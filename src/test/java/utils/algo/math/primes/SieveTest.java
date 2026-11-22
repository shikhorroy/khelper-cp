package utils.algo.math.primes;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SieveTest {

    private static boolean brutePrime(int x) {
        if (x < 2) return false;
        for (int d = 2; (long) d * d <= x; d++) if (x % d == 0) return false;
        return true;
    }

    private static List<Integer> bruteFactor(int x) {
        List<Integer> f = new ArrayList<>();
        for (int d = 2; (long) d * d <= x; d++) while (x % d == 0) {
            f.add(d);
            x /= d;
        }
        if (x > 1) f.add(x);
        return f;
    }

    @Test
    void isPrimeMatchesBruteForceUpTo2000() {
        Sieve s = new Sieve(2000);
        int cnt = 0;
        for (int x = 0; x <= 2000; x++) {
            assertEquals(brutePrime(x), s.isPrime(x), "isPrime(" + x + ")");
            if (brutePrime(x)) cnt++;
        }
        assertEquals(cnt, s.count());
        assertEquals(2, s.ithPrime(1));
        assertEquals(-1, s.ithPrime(cnt + 1));
        assertEquals(-1, s.ithPrime(0));
    }

    @Test
    void countsPrimesUpToTenMillion() {
        Sieve s = new Sieve(10_000_000);
        int[] p = s.getPrimeArray();
        assertEquals(664_579, s.count());
        assertEquals(664_579, p.length);
        assertEquals(9_999_991, p[p.length - 1]);
        assertEquals(664_579, s.getPrimes().size());
    }

    @Test
    void negativeNumberIsNotPrime() {
        assertFalse(new Sieve(10).isPrime(-5));
    }

    @Test
    void numberAboveLimitThrows() {
        assertThrows(RuntimeException.class, () -> new Sieve(10).isPrime(11));
    }

    @Test
    void tinySievesHaveNoPrimes() {
        assertEquals(0, new Sieve(0).count());
        assertEquals(0, new Sieve(1).count());
        assertFalse(new Sieve(1).isPrime(1));
        assertEquals(0, new Sieve(-3).count());
    }

    @Test
    void factorsMatchBruteForceOnRandomNumbers() {
        Sieve s = new Sieve(1000);
        Random rnd = new Random(42);
        for (int it = 0; it < 20_000; it++) {
            int x = 1 + rnd.nextInt(1_000_000);
            List<Integer> expected = bruteFactor(x);
            assertEquals(expected, s.getPrimeDivisors(x), "factors of " + x);
            assertEquals(expected.size(), s.getPrimeDivisorsCount(x));
        }
    }

    @Test
    void factorsAreAllPrimeAndMultiplyBack() {
        Sieve s = new Sieve(400);
        for (int x = 1; x <= 100_000; x += 7) {
            long prod = 1;
            for (int p : s.getPrimeDivisors(x)) {
                assertTrue(BigInteger.valueOf(p).isProbablePrime(30));
                prod *= p;
            }
            assertEquals(x, prod);
        }
    }

    @Test
    void numberTooLargeForSieveThrowsInsteadOfWrongFactor() {
        assertThrows(IllegalArgumentException.class, () -> new Sieve(10).getPrimeDivisors(169));
    }

    @Test
    void largePrimeFactorBelowSquareBoundIsAccepted() {
        assertEquals(List.of(2, 97), new Sieve(10).getPrimeDivisors(194));
    }

    @Test
    void negativeSieveDoesNotReturnCompositeAsPrime() {
        Sieve s = new Sieve(-5);
        assertThrows(IllegalArgumentException.class, () -> s.getPrimeDivisors(15));
    }

    @Test
    void numbersBelowTwoHaveNoFactors() {
        Sieve s = new Sieve(100);
        assertTrue(s.getPrimeDivisors(1).isEmpty());
        assertTrue(s.getPrimeDivisors(0).isEmpty());
        assertTrue(s.getPrimeDivisors(-12).isEmpty());
    }
}

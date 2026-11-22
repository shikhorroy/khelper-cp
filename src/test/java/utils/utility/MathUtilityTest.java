package utils.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilityTest {

    private static boolean bruteIsPrime(long n) {
        if (n < 2) return false;
        for (long d = 2; d * d <= n; d++) if (n % d == 0) return false;
        return true;
    }

    @Test
    void isPrimeMatchesBruteForce() {
        for (long n = -5; n <= 20_000; n++) assertEquals(bruteIsPrime(n), MathUtility.isPrime(n), "n=" + n);
    }

    @ParameterizedTest
    @ValueSource(longs = {4, 6, 8, 100, 1_000_000})
    void evenNumbersAboveTwoAreNotPrime(long n) {
        assertFalse(MathUtility.isPrime(n));
    }

    @Test
    void isPrimeHandlesLargePrimeWithoutOverflow() {
        assertTrue(MathUtility.isPrime(1_000_000_007L));
        assertTrue(MathUtility.isPrime(2_147_483_647L));
        assertFalse(MathUtility.isPrime(1_000_000_007L * 3));
    }

    @Test
    void isCompositeIsAboveOneAndNotPrime() {
        for (long n = -5; n <= 5_000; n++) assertEquals(n > 1 && !bruteIsPrime(n), MathUtility.isComposite(n), "n=" + n);
    }

    @Test
    void countDigitsMatchesStringLength() {
        for (long n = 0; n <= 100_000; n++) {
            assertEquals(Long.toString(n).length(), MathUtility.countDigits(n));
            assertEquals(Long.toBinaryString(n).length(), MathUtility.countDigits(n, 2));
        }
    }

    @Test
    void countDigitsIsExactNearPowersOfTen() {
        assertEquals(18, MathUtility.countDigits(999_999_999_999_999_999L));
        assertEquals(19, MathUtility.countDigits(1_000_000_000_000_000_000L));
        assertEquals(19, MathUtility.countDigits(Long.MAX_VALUE));
        assertEquals(19, MathUtility.countDigits(Long.MIN_VALUE));
        assertEquals(1, MathUtility.countDigits(-7));
    }

    @Test
    void countDigitsRejectsBaseBelowTwo() {
        assertThrows(IllegalArgumentException.class, () -> MathUtility.countDigits(5, 1));
    }

    @Test
    void isqrtIsExactForEveryRange() {
        for (long n = 0; n <= 100_000; n++) assertEquals((long) Math.floor(Math.sqrt(n)), MathUtility.isqrt(n));
        assertEquals(3_037_000_499L, MathUtility.isqrt(Long.MAX_VALUE));
        long r = 999_999_999L;
        assertEquals(r, MathUtility.isqrt(r * r));
        assertEquals(r - 1, MathUtility.isqrt(r * r - 1));
    }

    @Test
    void isPerfectSquareNearLongLimit() {
        long r = 3_037_000_499L;
        assertTrue(MathUtility.isPerfectSquare(r * r));
        assertFalse(MathUtility.isPerfectSquare(r * r - 1));
        assertFalse(MathUtility.isPerfectSquare(Long.MAX_VALUE));
        assertFalse(MathUtility.isPerfectSquare(-4));
        assertTrue(MathUtility.isPerfectSquare(0));
    }

    @Test
    void isFibonacciMatchesSequence() {
        List<Long> fib = new ArrayList<>(List.of(0L, 1L));
        while (fib.get(fib.size() - 1) < 1_000_000) fib.add(fib.get(fib.size() - 1) + fib.get(fib.size() - 2));
        for (long n = 0; n <= 1_000_000; n++) assertEquals(fib.contains(n), MathUtility.isFibonacci(n), "n=" + n);
        assertFalse(MathUtility.isFibonacci(-1));
    }

    @Test
    void trailingZerosOfFactorial() {
        assertEquals(0, MathUtility.trailingZerosOfFactorial(4));
        assertEquals(1, MathUtility.trailingZerosOfFactorial(5));
        assertEquals(24, MathUtility.trailingZerosOfFactorial(100));
        assertEquals(249_998, MathUtility.trailingZerosOfFactorial(1_000_000));
        assertTrue(MathUtility.trailingZerosOfFactorial(Long.MAX_VALUE) > 0);
    }

    @Test
    void countDigitsOfFactorial() {
        assertEquals(1, MathUtility.countDigitsOfFactorial(0));
        assertEquals(3, MathUtility.countDigitsOfFactorial(5));
        assertEquals(158, MathUtility.countDigitsOfFactorial(100));
    }

    @Test
    void oddEvenWorkForNegativesAndBoxedValues() {
        assertTrue(MathUtility.isOdd(-3));
        assertTrue(MathUtility.isEven(-4));
        assertTrue(MathUtility.isEven((Object) 4L));
        assertTrue(MathUtility.isOdd((Object) "7"));
    }

    @Test
    void powerOfTwo() {
        assertTrue(MathUtility.isPowerOf2(1));
        assertTrue(MathUtility.isPowerOf2(1L << 62));
        assertFalse(MathUtility.isPowerOf2(0));
        assertFalse(MathUtility.isPowerOf2(6));
    }

    @Test
    void nextPermutationWalksAllPermutationsInOrder() {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 2, 3));
        List<String> seen = new ArrayList<>();
        do seen.add(list.toString()); while (MathUtility.findNextPermutation(list));
        assertEquals(12, seen.size()); // 4! / 2! with the repeated 2
        assertEquals("[1, 2, 2, 3]", seen.get(0));
        assertEquals("[3, 2, 2, 1]", seen.get(11));
        assertFalse(MathUtility.findNextPermutation(new ArrayList<>(List.of(1))));
    }

    @Test
    void minAndMax() {
        assertEquals(-3, MathUtility.min(5, -3, 7));
        assertEquals(7, MathUtility.max(5, -3, 7));
    }
}

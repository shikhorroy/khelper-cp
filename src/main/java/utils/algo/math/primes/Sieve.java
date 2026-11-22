package utils.algo.math.primes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sieve of Eratosthenes over [0, n], odd numbers only.
 * Build: O(n log log n) time, O(n) memory (boolean[] plus an int[] of primes).
 */
public class Sieve {
    //~ Keeps i + 2 * x inside int range in the marking loop.
    private static final int MAX_N = Integer.MAX_VALUE - (1 << 17);

    private final int n;
    private final boolean[] isPrime;
    private final int[] primes;
    private final int primeCount;
    //~ Boxed view of primes, built on first getPrimes() call.
    private List<Integer> primeList;

    /**
     * @param n upper bound (inclusive). A negative n gives an empty sieve.
     * @throws IllegalArgumentException if n is too large to sieve without int overflow.
     */
    public Sieve(int n) {
        if (n > MAX_N) throw new IllegalArgumentException("n is too large: " + n);
        this.n = n;
        this.isPrime = new boolean[Math.max(n + 1, 0)];

        //~ implementation details: https://shikhorroy.wordpress.com/2013/11/12/wp-mep2mliv-5r/
        if (n >= 2) isPrime[2] = true;
        for (int i = 3; i <= n; i += 2) isPrime[i] = true;

        int[] found = new int[n >= 2 ? estimatePrimeCount(n) : 0];
        int count = 0;
        if (n >= 2) found[count++] = 2;
        for (int x = 3; x <= n; x += 2) {//~ apply sieve method only for odd numbers
            if (isPrime[x]) {
                if (count == found.length) found = Arrays.copyOf(found, count * 2 + 1);
                found[count++] = x;
                if ((long) x * x <= n) {
                    for (int i = x * x, step = 2 * x; i <= n; i += step) isPrime[i] = false;
                }
            }
        }
        this.primes = found;
        this.primeCount = count;
    }

    /**
     * Upper bound for pi(n) (Rosser and Schoenfeld), so the prime array rarely needs to grow.
     */
    private static int estimatePrimeCount(int n) {
        if (n < 17) return 6;
        return (int) Math.min(Integer.MAX_VALUE - 8, (long) (1.26 * n / Math.log(n)) + 1);
    }

    /**
     * Find if a number is prime or not. O(1).
     *
     * @param x the number to check
     * @return true or false (false for every x {@code < 2})
     * @throws RuntimeException if x {@code > n}.
     */
    public boolean isPrime(int x) {
        if (x > n) throw new RuntimeException(x + " is out of range.");
        return x >= 0 && isPrime[x];
    }

    /**
     * @return all primes {@code <= n} in increasing order, as a boxed list (built once and cached).
     */
    public List<Integer> getPrimes() {
        if (primeList == null) {
            List<Integer> list = new ArrayList<>(primeCount);
            for (int i = 0; i < primeCount; i++) list.add(primes[i]);
            primeList = list;
        }
        return primeList;
    }

    /**
     * @return a copy of all primes {@code <= n} in increasing order, as an int[] (no boxing).
     */
    public int[] getPrimeArray() {
        return Arrays.copyOf(primes, primeCount);
    }

    /**
     * Return i-th prime in the range of 1 to n.
     *
     * @param ith i-th prime number (1-based)
     * @return the i-th prime number, otherwise, -1 if does not exists i-th prime in the range of 1 to n.
     */
    public int ithPrime(int ith) {
        if (1 <= ith && ith <= this.primeCount) return this.primes[ith - 1];
        return -1;
    }

    /**
     * Count total number of primes in the range of 1 to n.
     *
     * @return total prime number count in the range of 1 to n.
     */
    public int count() {
        return this.primeCount;
    }

    /**
     * Prime factors of number with multiplicity, in increasing order. Example: 12 gives [2, 2, 3].
     * Empty for number {@code <= 1}. Complexity: O(pi(sqrt(number)) + log(number)).
     *
     * @throws IllegalArgumentException if number may have a prime factor above n that cannot be proven prime
     *                                  (number {@code >= (n + 1)^2} and not fully factored by primes up to n).
     */
    public List<Integer> getPrimeDivisors(int number) {
        List<Integer> primeDivisorList = new ArrayList<>();
        this.primeDivisors(number, primeDivisorList);

        return primeDivisorList;
    }

    /**
     * Number of prime factors of number counted with multiplicity (12 gives 3). Same rules as
     * {@link #getPrimeDivisors(int)}.
     */
    public int getPrimeDivisorsCount(int number) {
        return this.primeDivisors(number, null);
    }

    private int primeDivisors(int number, List<Integer> divisors) {
        boolean doGenerateList = divisors != null;

        int count = 0;
        boolean provenPrime = false;
        for (int i = 0; i < primeCount; i++) {
            int p = primes[i];
            if (p > number / p) {//~ no prime factor <= sqrt(number) is left, so the rest is 1 or a prime
                provenPrime = true;
                break;
            }
            while (number % p == 0) {
                ++count;
                number /= p;
                if (doGenerateList) divisors.add(p);
            }
        }
        if (number > 1) {
            if (!provenPrime && number >= (long) (Math.max(n, 0) + 1) * (Math.max(n, 0) + 1)) {
                throw new IllegalArgumentException("Sieve limit " + n + " is too small to factor " + number);
            }
            ++count;
            if (doGenerateList) divisors.add(number);
        }
        return count;
    }
}

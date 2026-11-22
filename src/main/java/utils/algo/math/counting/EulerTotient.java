package utils.algo.math.counting;

import java.util.ArrayList;
import java.util.List;

/**
 * Euler's totient function, also known as phi-function,
 * counts the number of integers between 1 and n inclusive, which are co-prime to n.
 * Complexity: O(n log log n) for the table.
 */
public class EulerTotient {

    /**
     * <h3>Properties of Euler's Totient Function:</h3>
     * <ol>
     *     <li>If p is a prime number, then ϕ(p) = p - 1</li>
     *     <li>
     *         If p is a prime number and m is a positive integer (that is, m ≥ 1),
     *         then ϕ(p<sup>m</sup>) = p<sup>m</sup> - p<sup>m - 1</sup>
     *     </li>
     *     <li>
     *         For any two positive integers m and n,
     *         <p>we have ϕ(mn) = ϕ(m) x ϕ(n) x gcd(m,n) / ϕ(gcd(m,n)).</p>
     *         Considering the case when m and n are coprime,
     *         then gcd(m,n) = 1. In such a scenario, the above relation reduces to ϕ(mn) = ϕ(m) x ϕ(n)
     *     </li>
     *     <li>
     *         The sum of values of the totient function over the positive divisors of n equals n itself.
     *         <p>In other words: ∑ϕ(k) = n, where k are the divisors.</p>
     *     </li>
     *     <li>
     *         If m and n are two prime numbers, then using (1) and (3), we get:<p>
     *         ϕ(mn) = ϕ(m) x ϕ(n) = (m - 1) x (n - 1)
     *     </li>
     * </ol>
     * Boxed version of {@link #calculateArray(int)}: phi.get(i) = ϕ(i) for 0 {@code <=} i {@code <=} n, ϕ(0) = 0.
     * Returns an empty list when n {@code < 0}.
     */
    public static List<Integer> calculate(int n) {
        int[] phi = calculateArray(n);
        List<Integer> list = new ArrayList<>(phi.length);
        for (int v : phi) list.add(v);
        return list;
    }

    /**
     * Primitive table: phi[i] = ϕ(i) for 0 {@code <=} i {@code <=} n, ϕ(0) = 0. Empty array when n {@code < 0}.
     * Complexity: O(n log log n).
     */
    public static int[] calculateArray(int n) {
        if (n < 0) return new int[0];
        int[] phi = new int[n + 1];
        for (int i = 0; i <= n; i++) phi[i] = i;

        for (int i = 2; i <= n; i++) {
            if (phi[i] == i) {
                for (int j = i; j <= n && j >= 0; j += i) phi[j] -= phi[j] / i;
            }
        }
        return phi;
    }

    /**
     * ϕ(n) for one value by trial division. ϕ(0) = 0 and ϕ(1) = 1.
     * Complexity: O(sqrt(n)).
     */
    public static long of(long n) {
        if (n <= 0) return 0;
        long result = n;
        for (long p = 2; p <= n / p; p++) {
            if (n % p == 0) {
                while (n % p == 0) n /= p;
                result -= result / p;
            }
        }
        if (n > 1) result -= result / n;
        return result;
    }
}

package utils.algo.math.counting;

/**
 * Pascal's triangle: C[n][k] for all 0 {@code <=} k {@code <=} n {@code <=} maxn.
 * Complexity: O(maxn^2) time and memory.
 */
public class BinomialCoefficient {

    //~ Largest maxn whose whole triangle fits in a long: C(66, 33) < 2^63 but C(67, 33) > 2^63.
    public static final int MAX_EXACT_N = 66;

    /**
     * Exact values. Values in rows above {@link #MAX_EXACT_N} overflow silently; use the mod overload for those.
     *
     * @throws IllegalArgumentException if maxn {@code < 0}.
     */
    public static long[][] calculate(int maxn) {
        if (maxn < 0) throw new IllegalArgumentException("maxn must be >= 0: " + maxn);
        long[][] C = new long[maxn + 1][maxn + 1];
        C[0][0] = 1;

        for (int n = 1; n <= maxn; ++n) {
            C[n][0] = C[n][n] = 1;
            for (int k = 1; k < n; ++k)
                C[n][k] = C[n - 1][k - 1] + C[n - 1][k];
        }
        return C;
    }

    /**
     * Values modulo mod (any mod {@code >= 1}, prime or not).
     *
     * @throws IllegalArgumentException if maxn {@code < 0} or mod {@code <= 0}.
     */
    public static long[][] calculate(int maxn, long mod) {
        if (maxn < 0) throw new IllegalArgumentException("maxn must be >= 0: " + maxn);
        if (mod <= 0) throw new IllegalArgumentException("mod must be positive: " + mod);
        long one = 1 % mod;
        long[][] C = new long[maxn + 1][maxn + 1];
        C[0][0] = one;

        for (int n = 1; n <= maxn; ++n) {
            C[n][0] = C[n][n] = one;
            for (int k = 1; k < n; ++k) {
                long v = C[n - 1][k - 1] + C[n - 1][k];
                C[n][k] = v >= mod ? v - mod : v;
            }
        }
        return C;
    }
}

package utils.algo.math.counting;

import utils.algo.math.Modulo;
import utils.algo.math.euclid.Euclid;

/**
 * nPr = n! / (n - r)! % mod for all n up to maxN.
 * <p>
 * Build: O(maxN). Query: O(1). Queries with n {@code >= mod} are correct when mod is prime (guaranteed by the build
 * step in that case). If mod shares a factor with some i {@code <= maxN} (non-prime mod), inverse factorials are not
 * available and each query falls back to one modular inverse, which throws ArithmeticException when it does not exist.
 */
public class NPR {
    private final int mod;
    private final int maxN;
    private long[] factorial;
    //~ factorial[i]^-1 % mod for i up to min(maxN, mod - 1); null when factorial is not invertible.
    private long[] invFactorial;

    /**
     * @throws IllegalArgumentException if maxN {@code < 0} or mod {@code <= 0}.
     */
    public NPR(int maxN, int mod) {
        if (maxN < 0) throw new IllegalArgumentException("maxN must be >= 0: " + maxN);
        if (mod <= 0) throw new IllegalArgumentException("mod must be positive: " + mod);
        this.mod = mod;
        this.maxN = maxN;

        genFactorials();
    }

    private void genFactorials() {
        this.factorial = new long[maxN + 1];

        factorial[0] = 1 % mod;
        for (int i = 1; i <= maxN; i++) factorial[i] = (factorial[i - 1] * i) % mod;

        int limit = (int) Math.min(maxN, mod - 1L);
        long inv;
        try {
            inv = Euclid.moduloInverse(factorial[limit], mod);
        } catch (ArithmeticException e) {
            return;
        }
        invFactorial = new long[limit + 1];
        invFactorial[limit] = inv;
        for (int i = limit; i > 0; i--) invFactorial[i - 1] = invFactorial[i] * i % mod;
    }

    /**
     * @return nPr % mod, or 0 when r {@code < 0} or r {@code > n}.
     * @throws Error if n {@code > maxN}.
     */
    public int calculate(int n, int r) {
        if (n > this.maxN) throw new Error("n (= " + n + ") can't be greater than maxn(= " + this.maxN + ")");
        if (r < 0 || n < r) return 0;
        if (r == 0) return 1 % mod;

        if (invFactorial == null) {
            Modulo modulo = new Modulo(mod);
            return (int) (modulo.div(factorial[n], factorial[n - r]));
        }
        if (n < mod) return (int) (factorial[n] * invFactorial[n - r] % mod);

        //~ Prime mod p, n >= p: the product (n - r + 1) * ... * n is 0 if it holds a multiple of p,
        //~ otherwise it equals the product of the residues, which are consecutive and non-zero.
        if (n / mod != (n - r) / mod) return 0;
        return (int) (factorial[n % mod] * invFactorial[(n - r) % mod] % mod);
    }

    public int calculate(int r) {
        return calculate(maxN, r);
    }
}

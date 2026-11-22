package utils.algo.math;

import utils.algo.math.euclid.Euclid;

/**
 * Modular arithmetic for an int modulus m. Every result is in the range [0, m), also for negative inputs.
 * All operations are O(1) except {@link #inverse} and {@link #div}, which are O(log m).
 */
public class Modulo {

    private final int mod;

    /**
     * @throws IllegalArgumentException if mod {@code <= 0}.
     */
    public Modulo(int mod) {
        if (mod <= 0) throw new IllegalArgumentException("mod must be positive: " + mod);
        this.mod = mod;
    }

    /**
     * Calculate x % m
     */
    public long mod(long x) {
        long r = x % mod;
        return r < 0 ? r + mod : r;
    }

    /**
     * Calculate (a + b) % m
     */
    public long add(long a, long b) {
        return mod(mod(a) + mod(b));
    }

    /**
     * Calculate (a - b) % m
     */
    public long sub(long a, long b) {
        return mod(mod(a) - mod(b));
    }

    /**
     * Calculate (a * b) % m
     */
    public long mult(long a, long b) {
        return mod(mod(a) * mod(b));
    }

    /**
     * Calculate (a / b) % m, that is a * b<sup>-1</sup> % m.
     *
     * @throws ArithmeticException if b has no inverse modulo m.
     */
    public long div(long a, long b) {
        return mod(mod(a) * inverse(b));
    }

    /**
     * Calculate a<sup>-1</sup> % m with extended Euclid. The modulus does not need to be prime.
     *
     * @throws ArithmeticException if gcd(a, m) != 1.
     */
    public long inverse(long a) {
        return Euclid.moduloInverse(a, this.mod);
    }
}

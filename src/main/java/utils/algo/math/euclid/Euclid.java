package utils.algo.math.euclid;

/**
 * Euclid's algorithm: gcd, lcm, extended gcd and modular inverse. All methods are iterative (no deep recursion).
 */
public class Euclid {
    /**
     * Greatest common divisor, always {@code >= 0}. {@code gcd(0, 0) = 0}.
     * <p>
     * Complexity: O(log(min(|a|, |b|))).
     * Note: {@code gcd(Long.MIN_VALUE, 0)} and {@code gcd(Long.MIN_VALUE, Long.MIN_VALUE)} overflow (result 2^63).
     */
    public static long gcd(long a, long b) {
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return Math.abs(a);
    }

    /**
     * Least common multiple, always {@code >= 0}. {@code lcm(x, 0) = 0}. Overflows silently if the result does not fit
     * in a long.
     * <p>
     * Complexity: O(log(min(|a|, |b|))).
     */
    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) return 0;
        return Math.abs(a / gcd(a, b) * b);
    }

    //~~ Extended Euclid ~~// START //
    public static class Tuple {
        public long g, x, y;

        public Tuple(long g, long x, long y) {
            this.g = g;
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Given a and b, finds g = gcd(a, b) {@code >= 0} and x, y such that {@code a * x + b * y = g}.
     * Works for negative and zero inputs. For {@code a = b = 0} it returns g = 0, x = 1, y = 0.
     * <p>
     * Complexity: O(log(min(|a|, |b|))).
     */
    public static Tuple extendedGCD(long a, long b) {
        long oldR = a, r = b;
        long oldX = 1, x = 0;
        long oldY = 0, y = 1;
        while (r != 0) {
            long q = oldR / r;
            long t = oldR - q * r;
            oldR = r;
            r = t;
            t = oldX - q * x;
            oldX = x;
            x = t;
            t = oldY - q * y;
            oldY = y;
            y = t;
        }
        if (oldR < 0) return new Tuple(-oldR, -oldX, -oldY);
        return new Tuple(oldR, oldX, oldY);
    }
    //~~ Extended Euclid ~~// END //

    /**
     * Modular inverse of a, in the range [0, mod). Works for any mod {@code > 1} where gcd(a, mod) = 1, prime or not.
     * <p>
     * Complexity: O(log mod).
     *
     * @throws ArithmeticException if mod {@code <= 0} or the inverse does not exist (gcd(a, mod) != 1).
     */
    public static long moduloInverse(long a, long mod) {
        if (mod <= 0) throw new ArithmeticException("mod must be positive: " + mod);
        Tuple t = extendedGCD(a % mod, mod);
        if (t.g != 1) throw new ArithmeticException(a + " has no inverse modulo " + mod);
        long inv = t.x % mod;
        return inv < 0 ? inv + mod : inv;
    }
}

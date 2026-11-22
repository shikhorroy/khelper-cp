package utils.algo.math;

import java.math.BigInteger;

/**
 * Greatest common divisor. The result is always {@code >= 0} and {@code gcd(0, 0) = 0}.
 * Complexity: O(log(min(|a|, |b|))).
 */
public class GCD {

    /**
     * Primitive int version (no boxing). Overflows only for {@code gcd(Integer.MIN_VALUE, 0 or MIN_VALUE)}.
     */
    public static int of(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return Math.abs(a);
    }

    /**
     * Primitive long version (no boxing). Overflows only for {@code gcd(Long.MIN_VALUE, 0 or MIN_VALUE)}.
     */
    public static long of(long a, long b) {
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return Math.abs(a);
    }

    /**
     * Generic version for boxed numbers (used by the sparse tables). Returns the same type as the input.
     * Supports Integer, Long, Short, Byte and BigInteger.
     *
     * @throws IllegalArgumentException for other Number types (for example Double).
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T of(T a, T b) {
        if (a instanceof Integer && b instanceof Integer) return (T) Integer.valueOf(of(a.intValue(), b.intValue()));
        if (a instanceof Long && b instanceof Long) return (T) Long.valueOf(of(a.longValue(), b.longValue()));
        if (a instanceof Short && b instanceof Short) return (T) Short.valueOf((short) of(a.intValue(), b.intValue()));
        if (a instanceof Byte && b instanceof Byte) return (T) Byte.valueOf((byte) of(a.intValue(), b.intValue()));
        if (a instanceof BigInteger && b instanceof BigInteger) return (T) ((BigInteger) a).gcd((BigInteger) b);
        throw new IllegalArgumentException("Unsupported types: " + a.getClass() + ", " + b.getClass());
    }
}

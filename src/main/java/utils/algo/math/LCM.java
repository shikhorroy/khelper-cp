package utils.algo.math;

import java.math.BigInteger;

/**
 * Least common multiple. The result is always {@code >= 0} and {@code lcm(x, 0) = 0}.
 * Complexity: O(log(min(|a|, |b|))).
 */
public class LCM {

    /**
     * Primitive int version (no boxing).
     *
     * @throws ArithmeticException if the result does not fit in an int.
     */
    public static int of(int a, int b) {
        return Math.toIntExact(of((long) a, b));
    }

    /**
     * Primitive long version (no boxing).
     *
     * @throws ArithmeticException if the result does not fit in a long.
     */
    public static long of(long a, long b) {
        if (a == 0 || b == 0) return 0;
        return Math.abs(Math.multiplyExact(a / GCD.of(a, b), b));
    }

    /**
     * Generic version for boxed numbers (used by the sparse tables). Returns the same type as the input.
     * Supports Integer, Long, Short, Byte and BigInteger.
     *
     * @throws ArithmeticException      if the result does not fit in the input type.
     * @throws IllegalArgumentException for other Number types (for example Double).
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T of(T a, T b) {
        if (a instanceof Integer && b instanceof Integer) return (T) Integer.valueOf(of(a.intValue(), b.intValue()));
        if (a instanceof Long && b instanceof Long) return (T) Long.valueOf(of(a.longValue(), b.longValue()));
        if (a instanceof Short && b instanceof Short) {
            int r = of(a.intValue(), b.intValue());
            if (r > Short.MAX_VALUE) throw new ArithmeticException("short overflow");
            return (T) Short.valueOf((short) r);
        }
        if (a instanceof Byte && b instanceof Byte) {
            int r = of(a.intValue(), b.intValue());
            if (r > Byte.MAX_VALUE) throw new ArithmeticException("byte overflow");
            return (T) Byte.valueOf((byte) r);
        }
        if (a instanceof BigInteger && b instanceof BigInteger) {
            BigInteger x = (BigInteger) a, y = (BigInteger) b;
            if (x.signum() == 0 || y.signum() == 0) return (T) BigInteger.ZERO;
            return (T) x.divide(x.gcd(y)).multiply(y).abs();
        }
        throw new IllegalArgumentException("Unsupported types: " + a.getClass() + ", " + b.getClass());
    }
}

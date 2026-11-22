package utils.algo.math.euclid;

/**
 * Solves the linear Diophantine equation {@code a * x + b * y = c} over the integers.
 * <p>
 * If (x0, y0) is one solution and g = gcd(a, b), then all solutions are:
 * {@code x = x0 + k * (b / g)}, {@code y = y0 - k * (a / g)} for any integer k, because
 * {@code a * (x0 + b/g) + b * (y0 - a/g) = a * x0 + b * y0 = c}.
 */
public class LinearDiophantineEquation {

    public static class Solution {
        public long x, y, g;
        public boolean hasSolution;

        public Solution(boolean hasSolution, long x, long y, long g) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.hasSolution = hasSolution;
        }
    }

    /**
     * Finds one solution (x, y) of {@code a * x + b * y = c}, with g = gcd(|a|, |b|).
     * A solution exists only if g divides c. For {@code a = b = 0} a solution (0, 0) exists only when c = 0.
     * x and y can overflow when |c / g| times the Bezout coefficients is above 2^63.
     * <p>
     * Complexity: O(log(min(|a|, |b|))).
     */
    public static Solution findAnySolution(long a, long b, long c) {
        if (a == 0 && b == 0) {
            return c == 0 ? new Solution(true, 0, 0, 0) : new Solution(false, -1, -1, -1);
        }
        Euclid.Tuple t = Euclid.extendedGCD(Math.abs(a), Math.abs(b));
        if (c % t.g != 0) return new Solution(false, -1, -1, -1);

        //~ Exact integer: g divides c. Using double here lost precision once the product passed 2^53.
        long scale = c / t.g;
        long x = t.x * scale;
        long y = t.y * scale;
        if (a < 0) x = -x;
        if (b < 0) y = -y;
        return new Solution(true, x, y, t.g);
    }
}

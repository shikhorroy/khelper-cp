package utils.utility;

import java.util.Collections;
import java.util.List;

public class MathUtility {

    public static boolean isOdd(long number) {
        return (number & 1) != 0;
    }

    public static boolean isEven(long number) {
        return (number & 1) == 0;
    }

    /** For boxed values in generic code. A Number is read directly, anything else is parsed. */
    public static <T> boolean isOdd(T number) {
        return isOdd(toLong(number));
    }

    public static <T> boolean isEven(T number) {
        return isEven(toLong(number));
    }

    private static long toLong(Object number) {
        if (number instanceof Number n) return n.longValue();
        return Long.parseLong(number.toString());
    }

    /** Floor of the square root, exact for every long (Math.sqrt alone can be off by one). */
    public static long isqrt(long number) {
        if (number < 0) throw new IllegalArgumentException("negative: " + number);
        long r = (long) Math.sqrt((double) number);
        // compare with division, because r * r can overflow near Long.MAX_VALUE
        while (r > 0 && r > number / r) r--;
        while (r + 1 <= number / (r + 1)) r++;
        return r;
    }

    public static boolean isPerfectSquare(long number) {
        if (number < 0) return false;
        long r = isqrt(number);
        return r * r == number;
    }

    /**
     * Find max values among integer or long values.
     *
     * @param values - among where max value need to find.
     * @return maximum among values.
     */
    public static long max(long... values) {
        long max = values[0];
        for (int i = 1; i < values.length; i++)
            if (values[i] > max) max = values[i];
        return max;
    }

    /**
     * Find min values among integer or long values.
     *
     * @param values - among where min value need to find.
     * @return minimum among values.
     */
    public static long min(long... values) {
        long min = values[0];
        for (int i = 1; i < values.length; i++)
            if (values[i] < min) min = values[i];
        return min;
    }

    /**
     * Number of digits of {@code number} in the given base (base >= 2). The sign is not counted, and 0 has 1 digit.
     * Uses integer division, because log10 gives wrong answers near powers of the base.
     * Time: O(digits).
     */
    public static int countDigits(long number, int base) {
        if (base < 2) throw new IllegalArgumentException("base must be >= 2: " + base);
        int digits = 1;
        // divide the negative value, so Long.MIN_VALUE also works
        for (long n = number < 0 ? number : -number; n <= -base; n /= base) digits++;
        return digits;
    }

    /** Number of decimal digits. See {@link #countDigits(long, int)}. */
    public static int countDigits(long number) {
        return countDigits(number, 10);
    }

    /**
     * Given a List, the task is to find the next lexicographically greater permutation.
     * <p>
     * Source: <a href="https://www.geeksforgeeks.org/implementing-next_permutation-in-java-with-examples/">click here</a>
     * <p>
     * Time Complexity: O(n) per call.
     *
     * @param list - list data.
     */
    public static <T extends Comparable<T>> boolean findNextPermutation(List<T> list) {
        if (list == null || list.size() <= 1) return false;

        int last = list.size() - 2;
        while (last >= 0) {
            if (list.get(last).compareTo(list.get(last + 1)) < 0) break;
            last--;
        }

        if (last < 0) return false;
        int nextGreater = list.size() - 1;

        for (int i = list.size() - 1; i > last; i--) {
            if (list.get(i).compareTo(list.get(last)) > 0) {
                nextGreater = i;
                break;
            }
        }

        Collections.swap(list, nextGreater, last);

        int left = last + 1, right = list.size() - 1;
        while (left < right) {
            Collections.swap(list, left, right);
            left++;
            right--;
        }
        return true;
    }

    /**
     * Find if a number is power of 2.
     *
     * @param n numbers to check
     * @return boolean
     */
    public static boolean isPowerOf2(long n) {
        return (n != 0 && (n & (n - 1)) == 0);
    }

    /** Works for 0 <= n <= ~1.3e9; above that 5 * n * n overflows a long. */
    public static boolean isFibonacci(long n) {
        if (n < 0) return false;
        return isPerfectSquare(5 * n * n + 4) || isPerfectSquare(5 * n * n - 4);
    }


    /**
     * Trial division, O(sqrt(n)).
     */
    public static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        for (long i = 3; i <= n / i; i += 2)
            if (n % i == 0) return false;

        return true;
    }

    /**
     * A composite number is greater than 1 and not prime. O(sqrt(n)).
     */
    public static boolean isComposite(long n) {
        return n > 1 && !isPrime(n);
    }

    /** Number of trailing zeros of n! (Legendre's formula for p = 5). O(log n). */
    public static long trailingZerosOfFactorial(long n) {
        long zeros = 0;
        // divide n instead of growing 5^k, so 5^k can never overflow
        while (n >= 5) {
            n /= 5;
            zeros += n;
        }
        return zeros;
    }

    public static int countDigitsOfFactorial(int n) {
        if (n < 0) return 0;
        if (n <= 1) return 1;

        double digits = 0;
        for (int x = 2; x <= n; x++) digits += Math.log10(x);
        return (int) (Math.floor(digits)) + 1;
    }
}

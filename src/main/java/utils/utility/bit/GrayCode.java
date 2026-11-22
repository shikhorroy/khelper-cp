package utils.utility.bit;

import java.util.ArrayList;
import java.util.List;

/**
 * Gray code is a binary numeral system where two successive values differ in only one bit.
 * <p>
 * For example, the sequence of Gray codes for 3-bit numbers is: 000, 001, 011, 010, 110, 111, 101, 100, so .
 * <p>
 * This code was invented by Frank Gray in 1953.
 * <p>
 * Source: <a href="https://cp-algorithms.com/algebra/gray-code.html">CP-Algo</a>
 */
public class GrayCode {

    public static int of(int number) {
        return number ^ (number >>> 1); // >>> treats the int as 32 unsigned bits
    }

    public static int reverseOf(int gray) {
        int n = 0;
        // >>> and != 0, so a negative value (top bit set) also ends and decodes right
        for (; gray != 0; gray >>>= 1) n ^= gray;
        return n;
    }

    public static List<String> allNBits(int n) {
        List<String> grays = new ArrayList<>(1 << n);
        char[] bits = new char[n];
        for (int i = 0, all = (1 << n); i < all; i++) {
            int val = of(i);
            for (int b = 0; b < n; b++) bits[n - 1 - b] = (char) ('0' + ((val >> b) & 1));
            grays.add(new String(bits));
        }
        return grays;
    }

    public static List<Integer> allNumbers(int n) {
        List<Integer> grays = new ArrayList<>(1 << n);
        for (int i = 0, all = (1 << n); i < all; i++) {
            grays.add(of(i));
        }
        return grays;
    }
}

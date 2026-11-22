package utils.utility.series;

public class SeriesUtility {

    /**
     * ~ source: https://www.geeksforgeeks.org/calculate-xor-1-n/
     * Calculate xor from 1 to n, i.e. 1 ^ 2 ^ 3 ^ .... ^ n
     *
     * @param n - last number of the series.
     * @return xor all numbers from 1 to n
     */
    public static long xorTillN(long n) {
        if (n < 0) throw new IllegalArgumentException("n must be >= 0: " + n);
        // the xor prefix repeats with period 4: n, 1, n + 1, 0
        switch ((int) (n & 3)) {
            case 0: return n;
            case 1: return 1;
            case 2: return n + 1;
            default: return 0;
        }
    }

    public static int xorTillN(int n) {
        return (int) xorTillN((long) n);
    }
}

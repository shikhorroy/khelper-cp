package utils.utility.bit;

public class BitManipulation {

    public static long setBit(long x, int nth) {
        return (x | (1L << nth));
    }

    public static long resetBit(long x, int nth) {
        return (x & ~(1L << nth));
    }

    public static boolean isSet(long x, int nth) {
        // != 0, not > 0: bit 63 is the sign bit, so the masked value is negative
        return (x & (1L << nth)) != 0;
    }

    public static long toggle(long x, int nth) {
        return (x ^ (1L << nth));
    }
}

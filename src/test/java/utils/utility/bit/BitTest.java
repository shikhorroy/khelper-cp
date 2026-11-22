package utils.utility.bit;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BitTest {

    @Test
    void isSetSeesTheSignBit() {
        assertTrue(BitManipulation.isSet(Long.MIN_VALUE, 63));
        assertFalse(BitManipulation.isSet(Long.MAX_VALUE, 63));
    }

    @Test
    void setResetToggle() {
        assertEquals(0b101, BitManipulation.setBit(0b001, 2));
        assertEquals(0b001, BitManipulation.resetBit(0b101, 2));
        assertEquals(0b100, BitManipulation.toggle(0b101, 0));
    }

    @Test
    void grayCodeRoundTripsIncludingNegatives() {
        for (int i = -1000; i <= 1000; i++) assertEquals(i, GrayCode.reverseOf(GrayCode.of(i)));
        assertEquals(Integer.MIN_VALUE, GrayCode.reverseOf(GrayCode.of(Integer.MIN_VALUE)));
    }

    @Test
    void neighbouringGrayCodesDifferInOneBit() {
        List<Integer> all = GrayCode.allNumbers(10);
        for (int i = 1; i < all.size(); i++) assertEquals(1, Integer.bitCount(all.get(i) ^ all.get(i - 1)));
    }

    @Test
    void allNBits() {
        assertEquals(List.of("000", "001", "011", "010", "110", "111", "101", "100"), GrayCode.allNBits(3));
        assertEquals(List.of(""), GrayCode.allNBits(0));
    }
}

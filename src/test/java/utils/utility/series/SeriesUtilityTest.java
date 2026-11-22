package utils.utility.series;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeriesUtilityTest {

    @Test
    void xorTillNMatchesLoop() {
        long x = 0;
        for (long n = 0; n <= 10_000; n++) {
            x ^= n;
            assertEquals(x, SeriesUtility.xorTillN(n));
            assertEquals((int) x, SeriesUtility.xorTillN((int) n));
        }
    }

    @Test
    void negativeNIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> SeriesUtility.xorTillN(-1L));
    }
}

package utils.utility;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @Test
    void allEqualsAndAnyEqualAcceptNulls() {
        assertTrue(Utility.allEquals(1, 1, 1));
        assertTrue(Utility.allEquals());
        assertFalse(Utility.allEquals(1, null));
        assertTrue(Utility.anyEqual(null, 1, null));
        assertFalse(Utility.anyEqual(3, 1, 2));
    }

    @Test
    void arrayUtilityCountsAndChecks() {
        assertTrue(ArrayUtility.areAllEvens(List.of(2, 4L, -6)));
        assertTrue(ArrayUtility.areAllOdds(List.of(1, -3)));
        assertEquals(2, ArrayUtility.countEvens(List.of(1, 2, 4)));
        assertEquals(1, ArrayUtility.countOdds(List.of(1, 2, 4)));
        assertEquals(Map.of(1, 2, 2, 1), ArrayUtility.countFrequency(List.of(1, 1, 2)));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtility.areAllEvens(List.of()));
    }

    @Test
    void fillSetsEveryCellOfANestedArray() {
        long[][][] a = new long[2][3][4];
        ArrayUtility.fill(a, -1L);
        for (long[][] b : a) for (long[] c : b) for (long v : c) assertEquals(-1L, v);
    }
}

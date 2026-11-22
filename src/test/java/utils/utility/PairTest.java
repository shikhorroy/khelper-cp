package utils.utility;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    void sortsByFirstThenSecondInNaturalOrder() {
        List<Pair<String, Integer>> list = new ArrayList<>(List.of(
                Pair.of("b", 1), Pair.of("a", 2), Pair.of("a", -1), Pair.of("Aa", 0), Pair.of("BB", 0)));
        Collections.sort(list);
        assertEquals("[<Aa, 0>, <BB, 0>, <a, -1>, <a, 2>, <b, 1>]", list.toString());
    }

    @Test
    void sortsNegativeLongsCorrectly() {
        // Long.hashCode(-1) is 0, so the old hash-based order put -1 after -2 and 0 wrong
        List<Pair<Long, Long>> list = new ArrayList<>(List.of(Pair.of(0L, 0L), Pair.of(-1L, 0L), Pair.of(-2L, 0L)));
        Collections.sort(list);
        assertEquals(List.of(-2L, -1L, 0L), list.stream().map(p -> p.first).toList());
    }

    @Test
    void equalsAndHashCodeHandleNulls() {
        assertEquals(Pair.of(null, 1), Pair.of(null, 1));
        assertEquals(Pair.of(null, 1).hashCode(), Pair.of(null, 1).hashCode());
        assertNotEquals(Pair.of(1, 2), Pair.of(2, 1));
        assertNotEquals(Pair.of(1, 2), "x");
    }
}

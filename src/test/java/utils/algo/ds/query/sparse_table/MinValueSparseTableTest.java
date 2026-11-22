package utils.algo.ds.query.sparse_table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinValueSparseTableTest {

    @ParameterizedTest
    @MethodSource("utils.algo.ds.query.sparse_table.SparseTableTest#sizes")
    void queryReturnsRangeMinimumForEveryRange(int n) {
        Random rnd = new Random(42);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(rnd.nextInt());
        MinValueSparseTable<Integer> table = new MinValueSparseTable<>(list);

        for (int l = 0; l < n; l++) {
            int min = Integer.MAX_VALUE;
            int next = l;
            for (int r = l; r < n; r += SparseTableTest.step(n)) {
                for (; next <= r; next++) min = Math.min(min, list.get(next));
                assertEquals(min, table.query(l, r), "range " + l + ".." + r);
            }
        }
    }

    @Test
    void worksForStrings() {
        MinValueSparseTable<String> table = new MinValueSparseTable<>(List.of("pear", "apple", "fig", "kiwi"));
        assertEquals("apple", table.query(0, 3));
        assertEquals("fig", table.query(2, 3));
    }
}

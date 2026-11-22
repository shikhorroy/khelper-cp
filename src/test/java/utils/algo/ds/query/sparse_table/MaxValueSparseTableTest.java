package utils.algo.ds.query.sparse_table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaxValueSparseTableTest {

    @ParameterizedTest
    @MethodSource("utils.algo.ds.query.sparse_table.SparseTableTest#sizes")
    void queryReturnsRangeMaximumForEveryRange(int n) {
        Random rnd = new Random(42);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(rnd.nextInt());
        MaxValueSparseTable<Integer> table = new MaxValueSparseTable<>(list);

        for (int l = 0; l < n; l++) {
            int max = Integer.MIN_VALUE;
            int next = l;
            for (int r = l; r < n; r += SparseTableTest.step(n)) {
                for (; next <= r; next++) max = Math.max(max, list.get(next));
                assertEquals(max, table.query(l, r), "range " + l + ".." + r);
            }
        }
    }

    @Test
    void worksForStrings() {
        MaxValueSparseTable<String> table = new MaxValueSparseTable<>(List.of("pear", "apple", "fig", "kiwi"));
        assertEquals("pear", table.query(0, 3));
        assertEquals("kiwi", table.query(2, 3));
    }
}

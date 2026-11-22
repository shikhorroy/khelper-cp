package utils.algo.ds.query.sparse_table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SparseTableTest {

    /** Sizes 1, 2, 3, 1000 and every power of two from 4 to 128 with its two neighbours. */
    static IntStream sizes() {
        IntStream.Builder b = IntStream.builder();
        b.add(1).add(2).add(3);
        for (int p = 2; p <= 7; p++) b.add((1 << p) - 1).add(1 << p).add((1 << p) + 1);
        return IntStream.concat(b.build(), IntStream.of(1000));
    }

    //~ step between right ends; big arrays test a sample of ranges to stay fast
    static int step(int n) {
        return n > 100 ? 7 : 1;
    }

    /** A table that keeps the first element; a plain subclass to test the base class alone. */
    private static SparseTable<Integer> leftMost(List<Integer> list) {
        return new SparseTable<>(list) {
            @Override
            public Integer compute(Integer elem1, Integer elem2) {
                return elem1;
            }
        };
    }

    @Test
    void log2IsExactFloorForEveryPowerOfTwoAndItsNeighbours() {
        for (int p = 0; p < 31; p++) {
            int pow = 1 << p;
            assertEquals(p, SparseTable.log2(pow));
            if (pow > 1) assertEquals(p - 1, SparseTable.log2(pow - 1));
            if (p >= 1 && p < 30) assertEquals(p, SparseTable.log2(pow + 1));
        }
        assertEquals(30, SparseTable.log2(Integer.MAX_VALUE));
    }

    @Test
    void queryOnPowerOfTwoSizeIncludesTopRow() {
        //~ before the fix, n = 2 had no row 1 and query(0, 1) threw IndexOutOfBoundsException
        SparseTable<Integer> table = new MinValueSparseTable<>(List.of(4, 9));
        assertEquals(4, table.query(0, 1));
        assertEquals(2, table.table.size());
    }

    @ParameterizedTest
    @MethodSource("sizes")
    void buildsOneRowPerPowerOfTwoThatFits(int n) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(i);
        SparseTable<Integer> table = leftMost(list);
        assertEquals(SparseTable.log2(n) + 1, table.table.size());
        for (int k = 0; k < table.table.size(); k++) {
            assertEquals(n - (1 << k) + 1, table.table.get(k).size());
        }
    }

    @Test
    void laterChangesToInputListDoNotChangeAnswers() {
        //~ before the fix, row 0 shared the caller's list, so this query returned 100
        List<Integer> list = new ArrayList<>(List.of(5, 1, 7));
        SparseTable<Integer> table = new MinValueSparseTable<>(list);
        list.set(1, 100);
        assertEquals(1, table.query(1, 1));
    }

    @Test
    void emptyListBuildsWithoutError() {
        assertDoesNotThrow(() -> leftMost(new ArrayList<>()));
    }

    @Test
    void randomQueriesMatchBruteForce() {
        Random rnd = new Random(42);
        int n = 300;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(rnd.nextInt(1000));
        SparseTable<Integer> table = leftMost(list);
        for (int q = 0; q < 2000; q++) {
            int l = rnd.nextInt(n);
            int r = l + rnd.nextInt(n - l);
            assertEquals(list.get(l), table.query(l, r));
        }
    }
}

package utils.algo.ds.query.sparse_table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SUMSparseTableTest {

    @ParameterizedTest
    @MethodSource("utils.algo.ds.query.sparse_table.SparseTableTest#sizes")
    void querySumsEveryRangeAndWrapsOnOverflow(int n) {
        Random rnd = new Random(42);
        List<Integer> ints = new ArrayList<>();
        List<Long> longs = new ArrayList<>();
        List<Double> doubles = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ints.add(rnd.nextInt());
            longs.add(rnd.nextLong());
            doubles.add((double) rnd.nextInt(1000));
        }
        SUMSparseTable<Integer> intTable = new SUMSparseTable<>(ints);
        SUMSparseTable<Long> longTable = new SUMSparseTable<>(longs);
        SUMSparseTable<Double> doubleTable = new SUMSparseTable<>(doubles);

        for (int l = 0; l < n; l++) {
            int si = 0;
            long sl = 0;
            double sd = 0;
            int next = l;
            for (int r = l; r < n; r += SparseTableTest.step(n)) {
                for (; next <= r; next++) {
                    si += ints.get(next);
                    sl += longs.get(next);
                    sd += doubles.get(next);
                }
                assertEquals(si, intTable.query(l, r), "int range " + l + ".." + r);
                assertEquals(sl, longTable.query(l, r), "long range " + l + ".." + r);
                assertEquals(sd, doubleTable.query(l, r), "double range " + l + ".." + r);
            }
        }
    }

    @Test
    void queryOnPowerOfTwoSizeIncludesTopRow() {
        //~ before the fix, the whole range of a size-4 table threw IndexOutOfBoundsException
        SUMSparseTable<Integer> table = new SUMSparseTable<>(List.of(1, 2, 3, 4));
        assertEquals(10, table.query(0, 3));
    }

    @Test
    void emptyRangeReturnsZeroOfElementType() {
        assertEquals(Integer.valueOf(0), new SUMSparseTable<>(List.of(5, 6)).query(1, 0));
        assertEquals(Long.valueOf(0L), new SUMSparseTable<>(List.of(5L, 6L)).query(1, 0));
        assertEquals(Double.valueOf(0.0), new SUMSparseTable<>(List.of(5.0, 6.0)).query(1, 0));
    }

    @Test
    void emptyListEmptyRangeReturnsZero() {
        assertEquals(0.0, new SUMSparseTable<Number>(new ArrayList<>()).query(0, -1).doubleValue());
    }
}

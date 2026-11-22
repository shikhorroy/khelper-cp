package utils.algo.ds.query.sparse_table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LCMSparseTableTest {

    @ParameterizedTest
    @MethodSource("utils.algo.ds.query.sparse_table.SparseTableTest#sizes")
    void queryMatchesBigIntegerLcmForIntAndLong(int n) {
        Random rnd = new Random(42);
        List<Integer> ints = new ArrayList<>();
        List<Long> longs = new ArrayList<>();
        //~ values 1..12 keep every range lcm (at most 27720) exact in int
        for (int i = 0; i < n; i++) {
            int v = rnd.nextInt(12) + 1;
            ints.add(v);
            longs.add((long) v);
        }
        LCMSparseTable<Integer> intTable = new LCMSparseTable<>(ints);
        LCMSparseTable<Long> longTable = new LCMSparseTable<>(longs);

        for (int l = 0; l < n; l++) {
            BigInteger lcm = BigInteger.ONE;
            int next = l;
            for (int r = l; r < n; r += SparseTableTest.step(n)) {
                for (; next <= r; next++) {
                    BigInteger v = BigInteger.valueOf(ints.get(next));
                    lcm = lcm.divide(lcm.gcd(v)).multiply(v);
                }
                assertEquals(lcm.intValue(), intTable.query(l, r), "int range " + l + ".." + r);
                assertEquals(lcm.longValue(), longTable.query(l, r), "long range " + l + ".." + r);
            }
        }
    }

    @Test
    void rangeWithTwoZerosReturnsZeroInsteadOfThrowing() {
        //~ before the fix, building this table threw ArithmeticException (BigInteger divide by zero)
        LCMSparseTable<Long> table = new LCMSparseTable<>(List.of(0L, 0L, 3L));
        assertEquals(0L, table.query(0, 1));
        assertEquals(0L, table.query(0, 2));
        assertEquals(3L, table.query(2, 2));
    }

    @Test
    void lcmOfTwoLargeCoprimeIntsIsExactInLong() {
        LCMSparseTable<Long> table = new LCMSparseTable<>(List.of(1_000_000_007L, 998_244_353L));
        assertEquals(1_000_000_007L * 998_244_353L, table.query(0, 1));
    }
}

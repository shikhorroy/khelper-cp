package utils.algo.ds.query.sparse_table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GCDSparseTableTest {

    //~ values that stress the sign and overflow handling of Euclid
    private static final int[] SPECIALS = {0, 1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE, 12, -18, 7};

    @ParameterizedTest
    @MethodSource("utils.algo.ds.query.sparse_table.SparseTableTest#sizes")
    void queryMatchesBigIntegerGcdForIntAndLong(int n) {
        Random rnd = new Random(42);
        List<Integer> ints = new ArrayList<>();
        List<Long> longs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int pick = rnd.nextInt(10);
            int v = pick == 0 ? 0 : pick == 1 ? -rnd.nextInt(50) : rnd.nextInt(60) + 1;
            ints.add(v);
            longs.add(v * 1_000_003L);
        }
        GCDSparseTable<Integer> intTable = new GCDSparseTable<>(ints);
        GCDSparseTable<Long> longTable = new GCDSparseTable<>(longs);

        for (int l = 0; l < n; l++) {
            BigInteger gi = BigInteger.ZERO;
            BigInteger gl = BigInteger.ZERO;
            int next = l;
            for (int r = l; r < n; r += SparseTableTest.step(n)) {
                for (; next <= r; next++) {
                    gi = gi.gcd(BigInteger.valueOf(ints.get(next)));
                    gl = gl.gcd(BigInteger.valueOf(longs.get(next)));
                }
                assertEquals(gi.intValue(), intTable.query(l, r), "int range " + l + ".." + r);
                assertEquals(gl.longValue(), longTable.query(l, r), "long range " + l + ".." + r);
            }
        }
    }

    @Test
    void handlesZeroNegativesAndExtremeValues() {
        Random rnd = new Random(42);
        for (int n = 1; n <= 40; n++) {
            List<Integer> ints = new ArrayList<>();
            List<Long> longs = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int v = rnd.nextBoolean() ? SPECIALS[rnd.nextInt(SPECIALS.length)] : rnd.nextInt();
                ints.add(v);
                longs.add(rnd.nextInt(4) == 0 ? (rnd.nextBoolean() ? Long.MIN_VALUE : Long.MAX_VALUE) : v * 6L);
            }
            GCDSparseTable<Integer> intTable = new GCDSparseTable<>(ints);
            GCDSparseTable<Long> longTable = new GCDSparseTable<>(longs);
            for (int l = 0; l < n; l++) {
                BigInteger gi = BigInteger.ZERO;
                BigInteger gl = BigInteger.ZERO;
                for (int r = l; r < n; r++) {
                    gi = gi.gcd(BigInteger.valueOf(ints.get(r)));
                    gl = gl.gcd(BigInteger.valueOf(longs.get(r)));
                    assertEquals(gi.intValue(), intTable.query(l, r), "ints " + ints.subList(l, r + 1));
                    assertEquals(gl.longValue(), longTable.query(l, r), "longs " + longs.subList(l, r + 1));
                }
            }
        }
    }

    @Test
    void resultIsNeverNegative() {
        GCDSparseTable<Integer> table = new GCDSparseTable<>(List.of(-12, -18, 0));
        assertEquals(6, table.query(0, 1));
        assertEquals(12, table.query(0, 0));
        assertEquals(6, table.query(0, 2));
    }
}

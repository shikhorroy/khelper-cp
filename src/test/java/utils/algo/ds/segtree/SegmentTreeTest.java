package utils.algo.ds.segtree;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SegmentTreeTest {

    /** Range sum; update sets the leaf (default applyChange). */
    private static SegmentTree<Long> sumTree(List<Long> list) {
        return new SegmentTree<>(list) {
            @Override
            public Long invalidReturn() {
                return 0L;
            }

            @Override
            public Long calculate(Long leftValue, Long rightValue) {
                return leftValue + rightValue;
            }
        };
    }

    /** Range max; update adds to the leaf (overridden applyChange). */
    private static SegmentTree<Long> maxAddTree(List<Long> list) {
        return new SegmentTree<>(list) {
            @Override
            public Long invalidReturn() {
                return Long.MIN_VALUE;
            }

            @Override
            public Long calculate(Long leftValue, Long rightValue) {
                return Math.max(leftValue, rightValue);
            }

            @Override
            public void applyChange(Long value, int currIndex) {
                tree.set(currIndex, tree.get(currIndex) + value);
            }
        };
    }

    private static List<Long> toList(long[] values) {
        List<Long> list = new ArrayList<>();
        for (long v : values) list.add(v);
        return list;
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 7, 8, 9, 16, 17, 31, 32, 33, 100, 1000})
    void sumQueryPointSetAndRangeSetMatchBruteForce(int n) {
        Random rnd = new Random(42);
        long[] brute = new long[n];
        for (int i = 0; i < n; i++) brute[i] = rnd.nextInt(100);
        SegmentTree<Long> tree = sumTree(toList(brute));

        for (int q = 0; q < 300; q++) {
            int l = rnd.nextInt(n);
            int r = rnd.nextInt(n);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            int type = rnd.nextInt(3);
            long v = rnd.nextInt(100);
            if (type == 0) {
                long sum = 0;
                for (int i = l; i <= r; i++) sum += brute[i];
                assertEquals(sum, tree.query(l, r), "range " + l + ".." + r);
            } else if (type == 1) {
                brute[l] = v;
                tree.update(l, v);
            } else {
                for (int i = l; i <= r; i++) brute[i] = v;
                tree.update(l, r, v);
            }
        }
    }

    @Test
    void overriddenApplyChangeAddsOnRangeUpdate() {
        Random rnd = new Random(42);
        int n = 37;
        long[] brute = new long[n];
        for (int i = 0; i < n; i++) brute[i] = rnd.nextInt(100);
        SegmentTree<Long> tree = maxAddTree(toList(brute));

        for (int q = 0; q < 2000; q++) {
            int l = rnd.nextInt(n);
            int r = rnd.nextInt(n);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            if (rnd.nextBoolean()) {
                long max = Long.MIN_VALUE;
                for (int i = l; i <= r; i++) max = Math.max(max, brute[i]);
                assertEquals(max, tree.query(l, r), "range " + l + ".." + r);
            } else {
                long v = rnd.nextInt(21) - 10;
                for (int i = l; i <= r; i++) brute[i] += v;
                tree.update(l, r, v);
            }
        }
    }

    @Test
    void queryBoundsOutsideArrayAreClamped() {
        SegmentTree<Long> tree = sumTree(toList(new long[]{1, 2, 3, 4, 5}));
        assertEquals(15L, tree.query(-5, 10));
        assertEquals(9L, tree.query(3, 99));
    }

    @Test
    void emptyRangeReturnsInvalidReturn() {
        SegmentTree<Long> tree = sumTree(toList(new long[]{1, 2, 3}));
        assertEquals(0L, tree.query(2, 1));
    }

    @Test
    void emptyListBuildsAndQueriesWithoutStackOverflow() {
        //~ before the fix, the constructor recursed forever on an empty list (StackOverflowError)
        SegmentTree<Long> tree = assertDoesNotThrow(() -> sumTree(new ArrayList<>()));
        assertEquals(0L, tree.query(0, 0));
        assertDoesNotThrow(() -> tree.update(0, 5L));
        assertDoesNotThrow(() -> tree.update(0, 3, 5L));
    }

    @Test
    void singleElementSupportsQueryAndUpdate() {
        SegmentTree<Long> tree = sumTree(toList(new long[]{7}));
        assertEquals(7L, tree.query(0, 0));
        tree.update(0, 11L);
        assertEquals(11L, tree.query(0, 0));
    }
}

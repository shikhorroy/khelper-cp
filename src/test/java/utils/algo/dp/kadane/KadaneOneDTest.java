package utils.algo.dp.kadane;

import org.junit.jupiter.api.Test;
import utils.utility.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class KadaneOneDTest {

    @Test
    void emptyListGivesZeroAndNoRanges() {
        KadaneOneD k = new KadaneOneD(List.of());
        assertEquals(0, k.maxSum);
        assertTrue(k.ranges.isEmpty());
    }

    @Test
    void nullListGivesZeroAndNoRanges() {
        KadaneOneD k = new KadaneOneD(null);
        assertEquals(0, k.maxSum);
        assertTrue(k.ranges.isEmpty());
    }

    @Test
    void sumDoesNotOverflow() {
        assertEquals(4_000_000_000L, new KadaneOneD(List.of(2_000_000_000, 2_000_000_000)).maxSum);
    }

    @Test
    void allNegativePicksLargestElement() {
        KadaneOneD k = new KadaneOneD(List.of(-5, -2, -7));
        assertEquals(-2, k.maxSum);
        assertEquals(List.of("1,1"), asStrings(k.ranges));
    }

    @Test
    void randomArraysMatchBruteForce() {
        Random random = new Random(42);
        for (int t = 0; t < 2000; t++) {
            int n = 1 + random.nextInt(8);
            boolean big = random.nextBoolean();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < n; i++) list.add(big ? random.nextInt() : random.nextInt(7) - 3);
            KadaneOneD k = new KadaneOneD(list);

            long max = Long.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                long s = 0;
                for (int j = i; j < n; j++) max = Math.max(max, s += list.get(j));
            }
            assertEquals(max, k.maxSum, list.toString());

            //~ For every end index whose best sum equals max: the range with the smallest start.
            List<String> expected = new ArrayList<>();
            for (int end = 0; end < n; end++) {
                long s = 0, bestSum = Long.MIN_VALUE;
                int bestStart = -1;
                for (int start = end; start >= 0; start--) {
                    s += list.get(start);
                    if (s >= bestSum) {
                        bestSum = s;
                        bestStart = start;
                    }
                }
                if (bestSum == max) expected.add(bestStart + "," + end);
            }
            assertEquals(expected, asStrings(k.ranges), list.toString());
        }
    }

    private static List<String> asStrings(List<Pair<Integer, Integer>> ranges) {
        List<String> out = new ArrayList<>();
        for (Pair<Integer, Integer> p : ranges) out.add(p.toString().replaceAll("[^0-9,]", ""));
        return out;
    }
}

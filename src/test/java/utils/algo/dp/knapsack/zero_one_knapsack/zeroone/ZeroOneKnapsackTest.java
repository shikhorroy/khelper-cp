package utils.algo.dp.knapsack.zero_one_knapsack.zeroone;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ZeroOneKnapsackTest {

    @Test
    void zeroWeightItemCountsAtCapacityZero() {
        ZeroOneKnapsack k = new ZeroOneKnapsack(0, List.of(0), List.of(5));
        assertEquals(5, k.iterativeDP());
        assertEquals(List.of(0), k.indexOfSelectedItems());
    }

    @Test
    void largeValuesDoNotOverflow() {
        ZeroOneKnapsack k = new ZeroOneKnapsack(3, List.of(1, 1, 1), List.of(1_000_000_000, 1_000_000_000, 1_000_000_000));
        assertEquals(3_000_000_000L, k.iterativeDP());
    }

    @Test
    void negativeCapacityGivesZeroAndNoItems() {
        ZeroOneKnapsack k = new ZeroOneKnapsack(-1, List.of(1), List.of(5));
        assertEquals(0, k.iterativeDP());
        assertTrue(k.indexOfSelectedItems().isEmpty());
    }

    @Test
    void classicExample() {
        ZeroOneKnapsack k = new ZeroOneKnapsack(50, List.of(10, 20, 30), List.of(60, 100, 120));
        assertEquals(220, k.iterativeDP());
        assertEquals(List.of(1, 2), k.indexOfSelectedItems());
    }

    @Test
    void randomInputsMatchBitmaskBruteForce() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            int n = random.nextInt(9), capacity = random.nextInt(15);
            List<Integer> weights = new ArrayList<>(), values = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                weights.add(random.nextInt(6));
                values.add(random.nextBoolean() ? random.nextInt(1_000_000_000) : random.nextInt(5));
            }
            long best = 0;
            for (int mask = 0; mask < (1 << n); mask++) {
                long w = 0, v = 0;
                for (int i = 0; i < n; i++)
                    if ((mask >> i & 1) == 1) {
                        w += weights.get(i);
                        v += values.get(i);
                    }
                if (w <= capacity) best = Math.max(best, v);
            }
            ZeroOneKnapsack k = new ZeroOneKnapsack(capacity, weights, values);
            assertEquals(best, k.iterativeDP(), "case " + t);

            long w = 0, v = 0;
            List<Integer> items = k.indexOfSelectedItems();
            for (int i = 0; i < items.size(); i++) {
                if (i > 0) assertTrue(items.get(i - 1) < items.get(i));
                w += weights.get(items.get(i));
                v += values.get(items.get(i));
            }
            assertTrue(w <= capacity);
            assertEquals(best, v);
        }
    }
}

package utils.algo.dp.knapsack.zero_one_knapsack.subsetsum;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SubsetSumTest {

    @Test
    void negativeTargetIsFalse() {
        assertFalse(new SubsetSum(List.of(1), -1).iterativeDP());
    }

    @Test
    void emptySubsetReachesZero() {
        assertTrue(new SubsetSum(List.of(), 0).iterativeDP());
        assertFalse(new SubsetSum(List.of(), 1).iterativeDP());
    }

    @Test
    void randomInputsMatchBitmaskBruteForce() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            int n = random.nextInt(9), target = random.nextInt(15);
            List<Integer> array = new ArrayList<>();
            for (int i = 0; i < n; i++) array.add(random.nextInt(6));
            boolean expected = false;
            for (int mask = 0; mask < (1 << n); mask++) {
                int s = 0;
                for (int i = 0; i < n; i++) if ((mask >> i & 1) == 1) s += array.get(i);
                if (s == target) expected = true;
            }
            assertEquals(expected, new SubsetSum(array, target).iterativeDP(), array + " target " + target);
        }
    }
}

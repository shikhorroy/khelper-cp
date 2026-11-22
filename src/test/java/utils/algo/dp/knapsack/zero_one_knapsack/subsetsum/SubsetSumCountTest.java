package utils.algo.dp.knapsack.zero_one_knapsack.subsetsum;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SubsetSumCountTest {

    @Test
    void zerosDoubleTheCount() {
        assertEquals(2, new SubsetSumCount(List.of(0, 1), 1).iterativeDP());
        assertEquals(4, new SubsetSumCount(List.of(0, 0), 0).iterativeDP());
    }

    @Test
    void countDoesNotOverflowInt() {
        List<Integer> ones = new ArrayList<>(Collections.nCopies(34, 1));
        assertEquals(2_333_606_220L, new SubsetSumCount(ones, 17).iterativeDP());
    }

    @Test
    void modVariantMatchesExactCountModulo() {
        List<Integer> ones = new ArrayList<>(Collections.nCopies(34, 1));
        assertEquals(2_333_606_220L % 1_000_000_007L, new SubsetSumCount(ones, 17).iterativeDP(1_000_000_007L));
    }

    @Test
    void negativeTargetIsZero() {
        assertEquals(0, new SubsetSumCount(List.of(1), -1).iterativeDP());
    }

    @Test
    void randomInputsMatchBitmaskBruteForce() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            int n = random.nextInt(9), target = random.nextInt(15);
            List<Integer> array = new ArrayList<>();
            for (int i = 0; i < n; i++) array.add(random.nextInt(6));
            long expected = 0;
            for (int mask = 0; mask < (1 << n); mask++) {
                int s = 0;
                for (int i = 0; i < n; i++) if ((mask >> i & 1) == 1) s += array.get(i);
                if (s == target) expected++;
            }
            assertEquals(expected, new SubsetSumCount(array, target).iterativeDP(), array + " target " + target);
            assertEquals(expected % 7, new SubsetSumCount(array, target).iterativeDP(7));
        }
    }
}

package utils.algo.sorting.mergesort;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CountInversionTest {

    @Test
    void docExampleCountsEqualPairsToo() {
        assertEquals(6, CountInversion.run(new ArrayList<>(List.of(1, 9, 6, 4, 5, 9))));
    }

    @Test
    void immutableListIsAccepted() {
        assertEquals(3, CountInversion.run(List.of(3, 2, 1)));
    }

    @Test
    void callerListIsNotReordered() {
        List<Integer> l = new ArrayList<>(List.of(3, 1, 2));
        CountInversion.run(l);
        assertEquals(List.of(3, 1, 2), l);
    }

    @Test
    void nullAndTinyInputsGiveZero() {
        assertEquals(0, CountInversion.run((List<Integer>) null));
        assertEquals(0, CountInversion.run((int[]) null));
        assertEquals(0, CountInversion.run(new int[0]));
        assertEquals(0, CountInversion.run(new long[]{5}));
        assertEquals(0, CountInversion.run(new ArrayList<Integer>()));
    }

    @Test
    void allOverloadsMatchBruteForceAndKeepInput() {
        Random rnd = new Random(42);
        for (int it = 0; it < 300; it++) {
            int n = rnd.nextInt(40);
            int[] a = new int[n];
            long[] b = new long[n];
            List<Integer> l = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                a[i] = rnd.nextInt(8) - 4;
                b[i] = a[i];
                l.add(a[i]);
            }
            int[] before = a.clone();
            long brute = 0;
            for (int i = 0; i < n; i++) for (int j = i + 1; j < n; j++) if (a[i] >= a[j]) brute++;
            assertEquals(brute, CountInversion.run(a));
            assertEquals(brute, CountInversion.run(b));
            assertEquals(brute, CountInversion.run(l));
            assertArrayEquals(before, a);
        }
    }

    @Test
    void reversedArrayOfTwoHundredThousandDoesNotOverflow() {
        int[] big = new int[200_000];
        for (int i = 0; i < big.length; i++) big[i] = big.length - i;
        assertEquals((long) big.length * (big.length - 1) / 2, CountInversion.run(big));
    }

    @Test
    void sortedDistinctArrayHasNoInversions() {
        int[] a = new int[1000];
        Arrays.setAll(a, i -> i);
        assertEquals(0, CountInversion.run(a));
    }
}

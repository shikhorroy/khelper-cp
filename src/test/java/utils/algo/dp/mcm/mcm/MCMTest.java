package utils.algo.dp.mcm.mcm;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MCMTest {

    @Test
    void costDoesNotOverflowInt() {
        assertEquals(16_000_000_000L, new MCM(List.of(2000, 2000, 2000, 2000)).minCost);
    }

    @Test
    void fewerThanTwoMatricesCostZero() {
        assertEquals(0, new MCM(List.of()).minCost);
        assertEquals(0, new MCM(List.of(5)).minCost);
        assertEquals(0, new MCM(List.of(5, 7)).minCost);
    }

    @Test
    void classicExample() {
        assertEquals(4500, new MCM(List.of(10, 30, 5, 60)).minCost);
    }

    @Test
    void randomInputsMatchRecursiveReference() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            int n = 3 + random.nextInt(5);
            long[] dims = new long[n];
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < n; i++) list.add((int) (dims[i] = 1 + random.nextInt(3000)));
            assertEquals(reference(dims, 1, n - 1), new MCM(list).minCost, list.toString());
        }
    }

    private static long reference(long[] d, int i, int j) {
        if (i >= j) return 0;
        long best = Long.MAX_VALUE;
        for (int k = i; k < j; k++)
            best = Math.min(best, reference(d, i, k) + reference(d, k + 1, j) + d[i - 1] * d[k] * d[j]);
        return best;
    }
}

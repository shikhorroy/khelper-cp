package utils.algo.math.cycle;

import org.junit.jupiter.api.Test;
import utils.utility.Pair;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FloydCycleTest {

    private static FloydCycle cycleOf(int mod) {
        return new FloydCycle() {
            @Override
            public int f(int x) {
                return (int) (((long) x * x + 1) % mod);
            }
        };
    }

    private static int[] brute(FloydCycle fc, int x0) {
        Map<Integer, Integer> seen = new HashMap<>();
        int x = x0;
        for (int i = 0; ; i++) {
            Integer first = seen.putIfAbsent(x, i);
            if (first != null) return new int[]{first, i - first};
            x = fc.f(x);
        }
    }

    @Test
    void findsStartAndLengthMatchingBruteForce() {
        for (int mod = 2; mod <= 300; mod++) {
            FloydCycle fc = cycleOf(mod);
            for (int x0 = 0; x0 < Math.min(mod, 20); x0++) {
                Pair<Integer, Integer> r = fc.detect(x0);
                int[] e = brute(fc, x0);
                assertEquals(e[0], r.first, "mu for mod " + mod + ", x0 " + x0);
                assertEquals(e[1], r.second, "lambda for mod " + mod + ", x0 " + x0);
            }
        }
    }

    @Test
    void fixedPointHasCycleLengthOne() {
        FloydCycle fc = new FloydCycle() {
            @Override
            public int f(int x) {
                return 5;
            }
        };
        Pair<Integer, Integer> r = fc.detect(0);
        assertEquals(1, r.first);
        assertEquals(1, r.second);
    }
}

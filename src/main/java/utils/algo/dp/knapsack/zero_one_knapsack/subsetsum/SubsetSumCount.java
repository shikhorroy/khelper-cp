package utils.algo.dp.knapsack.zero_one_knapsack.subsetsum;

import java.util.List;

/**
 * Count the subsets (by index) of the non-negative numbers that add up to {@code target}.
 * Zeros count: each 0 doubles the number of ways. The empty subset counts for target 0.
 * Complexity: O(n * target) time, O(target) memory.
 */
public class SubsetSumCount {

    private final int target;
    private final List<Integer> array;

    public SubsetSumCount(List<Integer> array, int target) {
        this.array = array;
        this.target = target;
    }

    /** Exact count in {@code long} (exact while the answer is below 2^63, e.g. up to 62 numbers). */
    public long iterativeDP() {
        return count(0);
    }

    /** Count modulo {@code mod}. */
    public long iterativeDP(long mod) {
        return count(mod);
    }

    // mod == 0 means no modulo
    private long count(long mod) {
        if (target < 0) return 0;
        //~ ways[s] = number of subsets of the numbers seen so far with sum s
        long[] ways = new long[target + 1];
        ways[0] = 1;
        for (int curr : array) {
            // Go from high to low sums so each number is used at most once; curr == 0 doubles ways[s]
            for (int s = target; s >= curr; s--) {
                ways[s] += ways[s - curr];
                if (mod != 0) ways[s] %= mod;
            }
        }
        return ways[target];
    }
}

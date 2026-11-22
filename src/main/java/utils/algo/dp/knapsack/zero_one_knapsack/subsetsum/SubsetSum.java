package utils.algo.dp.knapsack.zero_one_knapsack.subsetsum;

import java.util.List;

/**
 * Subset sum: can some subset of the (non-negative) numbers add up to {@code target}?
 * {@code dp[i][s]} is true when some subset of the first i numbers sums to s. A negative target gives false.
 * Complexity: O(n * target) time and memory.
 */
public class SubsetSum {
    public boolean[][] dp;

    private final int n;
    private final int target;
    private final List<Integer> array;

    public SubsetSum(List<Integer> array, int target) {
        this.array = array;
        this.target = target;
        this.n = array.size();
    }

    public boolean iterativeDP() {
        if (target < 0) return false;
        dp = new boolean[n + 1][target + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = true;

        for (int i = 1; i <= n; i++) {
            int curr = array.get(i - 1);
            boolean[] prev = dp[i - 1], cur = dp[i];
            for (int s = 1; s <= target; s++) cur[s] = prev[s] || (curr <= s && prev[s - curr]);
        }

        return dp[n][target];
    }
}

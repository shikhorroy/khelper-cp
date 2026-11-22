package utils.algo.dp.knapsack.zero_one_knapsack.zeroone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 0/1 knapsack: max total value of items with total weight <= capacity, each item used at most once.
 * Weights must be >= 0. Values are summed in {@code long}, so they do not overflow.
 * The full table is kept so {@link #indexOfSelectedItems()} can rebuild the chosen items.
 * Complexity: O(n * capacity) time and memory.
 */
public class ZeroOneKnapsack {
    //~ dp[i][c] = best value using the first i items with capacity c
    private long[][] dp;

    private final int n;
    private final int capacity;
    private final List<Integer> values;
    private final List<Integer> weights;

    public ZeroOneKnapsack(int capacity, List<Integer> weights, List<Integer> values) {
        this.values = values;
        this.weights = weights;
        this.n = weights.size();
        this.capacity = capacity;
    }

    public long iterativeDP() {
        if (capacity < 0) return 0;
        dp = new long[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            int weight = weights.get(i - 1);
            long value = values.get(i - 1);
            long[] prev = dp[i - 1], cur = dp[i];
            // c starts at 0, so an item with weight 0 is also counted when capacity is 0
            for (int c = 0; c <= capacity; c++) {
                cur[c] = prev[c];
                if (weight <= c) cur[c] = Math.max(cur[c], value + prev[c - weight]);
            }
        }

        return dp[n][capacity];
    }

    /** 0-based indexes of one optimal item set, in increasing order. Call {@link #iterativeDP()} first. */
    public List<Integer> indexOfSelectedItems() {
        List<Integer> indexList = new ArrayList<>();
        if (capacity < 0) return indexList;
        int cap = capacity;
        for (int i = n; i > 0; i--) {
            if (dp[i][cap] != dp[i - 1][cap]) {
                indexList.add(i - 1);
                cap -= weights.get(i - 1);
            }
        }
        Collections.reverse(indexList);
        return indexList;
    }
}

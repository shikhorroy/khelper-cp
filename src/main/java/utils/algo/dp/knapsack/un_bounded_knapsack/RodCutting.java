package utils.algo.dp.knapsack.un_bounded_knapsack;

import java.util.List;

/**
 * Rod cutting (unbounded knapsack): max total price for cutting a rod of {@code length}, where piece i
 * has length {@code lengths[i]} and price {@code prices[i]}. Each piece can be used any number of times.
 * Pieces with length <= 0 are ignored. Pieces need not fill the rod exactly.
 * Result is {@code long}, so it does not overflow.
 * Complexity: O(n * length) time, O(length) memory.
 */
public class RodCutting {

    private final int length;
    private final List<Integer> prices;
    private final List<Integer> lengths;

    public RodCutting(int length, List<Integer> lengths, List<Integer> prices) {
        this.prices = prices;
        this.lengths = lengths;
        this.length = length;
    }

    public long iterativeDP() {
        if (length < 0) return 0;
        //~ best[l] = max price using a rod of length l (pieces may leave some length unused)
        long[] best = new long[length + 1];
        for (int i = 0, n = lengths.size(); i < n; i++) {
            int pieceLength = lengths.get(i);
            if (pieceLength <= 0) continue;
            long price = prices.get(i);
            for (int l = pieceLength; l <= length; l++) best[l] = Math.max(best[l], best[l - pieceLength] + price);
        }
        return best[length];
    }
}

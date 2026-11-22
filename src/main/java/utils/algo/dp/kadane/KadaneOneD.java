package utils.algo.dp.kadane;

import utils.utility.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Kadane - maximum sum of a non-empty contiguous subarray. Sums are {@code long}, so they do not overflow.
 * {@code ranges} holds, for every end index whose best sum equals {@code maxSum}, the range (start, end)
 * with the smallest start. For an empty or null list, {@code maxSum = 0} and {@code ranges} is empty.
 * Complexity: O(n).
 */
public class KadaneOneD {

    public long maxSum;
    private final List<Integer> list;
    public List<Pair<Integer, Integer>> ranges;

    public KadaneOneD(List<Integer> list) {
        ranges = new ArrayList<>();
        this.list = list != null ? list : new ArrayList<>();

        if (!this.list.isEmpty()) run();
    }

    private void run() {
        int start = 0;
        long currMaxSum = list.get(0), maxSumSoFar = currMaxSum;
        ranges.add(Pair.of(0, 0));

        for (int i = 1, sz = list.size(); i < sz; i++) {
            long currValue = list.get(i);
            long sum = currMaxSum + currValue;

            if (currValue > sum) {
                currMaxSum = currValue;
                start = i;
            } else {
                currMaxSum = sum;
            }

            if (currMaxSum > maxSumSoFar) {
                maxSumSoFar = currMaxSum;
                ranges.clear();
            }
            if (currMaxSum == maxSumSoFar) ranges.add(Pair.of(start, i));
        }
        maxSum = maxSumSoFar;
    }
}

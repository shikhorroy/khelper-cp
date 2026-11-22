package utils.algo.dp.mcm.mcm;

import java.util.List;

/**
 * Matrix Chain Multiplication: min number of scalar multiplications to multiply a chain of matrices,
 * where matrix i has size dimensions[i - 1] x dimensions[i]. Costs are {@code long}, so they do not overflow.
 * Fewer than 3 dimensions (0 or 1 matrix) gives 0.
 * Complexity: O(n^3) time, O(n^2) memory.
 */
public class MCM {

    public long minCost;

    public MCM(List<Integer> dimensions) {
        int sz = dimensions.size();
        long[] dim = new long[sz];
        for (int i = 0; i < sz; i++) dim[i] = dimensions.get(i);
        this.minCost = sz < 3 ? 0 : bottomUp(dim);
    }

    private static long bottomUp(long[] dim) {
        int matrices = dim.length - 1;
        //~ cost[i][j] = min cost to multiply matrices i..j (1-based, matrix i is dim[i - 1] x dim[i])
        long[][] cost = new long[matrices + 1][matrices + 1];
        for (int len = 2; len <= matrices; len++) {
            for (int i = 1; i + len - 1 <= matrices; i++) {
                int j = i + len - 1;
                long best = Long.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    long candidate = cost[i][k] + cost[k + 1][j] + dim[i - 1] * dim[k] * dim[j];
                    if (candidate < best) best = candidate;
                }
                cost[i][j] = best;
            }
        }
        return cost[1][matrices];
    }
}

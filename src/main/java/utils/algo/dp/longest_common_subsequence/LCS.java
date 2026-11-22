package utils.algo.dp.longest_common_subsequence;

/**
 * Length of the Longest Common Subsequence of two strings.
 * Complexity: O(m * n) time, O(n) memory (two rolling rows).
 */
public class LCS {

    private final char[] string1;
    private final char[] string2;

    public LCS(String string1, String string2) {
        this.string1 = string1.toCharArray();
        this.string2 = string2.toCharArray();
    }

    public int iterativeDP() {
        int m = string1.length, n = string2.length;
        //~ prev = dp row i - 1, cur = dp row i; dp[i][j] = LCS of the first i and first j characters
        int[] prev = new int[n + 1], cur = new int[n + 1];
        for (int i = 1; i <= m; i++) {
            char a = string1[i - 1];
            for (int j = 1; j <= n; j++) {
                if (a == string2[j - 1]) cur[j] = 1 + prev[j - 1];
                else cur[j] = Math.max(cur[j - 1], prev[j]);
            }
            int[] tmp = prev;
            prev = cur;
            cur = tmp;
        }
        return prev[n];
    }
}

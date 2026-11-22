package utils.algo.dp.longest_common_subsequence;

/**
 * Length of the Longest Common Substring (contiguous) of two strings.
 * Complexity: O(m * n) time, O(n) memory (two rolling rows).
 */
public class LongestCommonSubstring {

    private final char[] string1;
    private final char[] string2;

    public LongestCommonSubstring(String string1, String string2) {
        this.string1 = string1.toCharArray();
        this.string2 = string2.toCharArray();
    }

    public int iterativeDP() {
        int m = string1.length, n = string2.length;
        //~ dp[i][j] = length of the common suffix of the first i and first j characters
        int[] prev = new int[n + 1], cur = new int[n + 1];
        int maxMatched = 0;
        for (int i = 1; i <= m; i++) {
            char a = string1[i - 1];
            for (int j = 1; j <= n; j++) {
                cur[j] = a == string2[j - 1] ? 1 + prev[j - 1] : 0;
                if (cur[j] > maxMatched) maxMatched = cur[j];
            }
            int[] tmp = prev;
            prev = cur;
            cur = tmp;
        }
        return maxMatched;
    }
}

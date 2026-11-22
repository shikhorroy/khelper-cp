package utils.algo.dp.mcm.mcm;

/**
 * Palindrome partitioning: min number of cuts so that every piece of the string is a palindrome.
 * An empty string or a palindrome needs 0 cuts.
 * Every palindrome is found by expanding around its center, so the run is O(n^2) time and O(n) memory
 * (the classic MCM-style recursion is O(n^3)).
 */
public class PalindromePartitioning {

    public int minPartitions;

    public PalindromePartitioning(String string) {
        char[] s = string.toCharArray();
        int n = s.length;
        //~ pieces[i] = min number of palindrome pieces for the prefix s[0 .. i - 1]
        int[] pieces = new int[n + 1];
        for (int i = 0; i <= n; i++) pieces[i] = i;
        for (int center = 0; center < n; center++) {
            // Odd length palindromes centered at `center`, then even length ones centered at center + 0.5.
            for (int lo = center, hi = center; lo >= 0 && hi < n && s[lo] == s[hi]; lo--, hi++)
                pieces[hi + 1] = Math.min(pieces[hi + 1], pieces[lo] + 1);
            for (int lo = center, hi = center + 1; lo >= 0 && hi < n && s[lo] == s[hi]; lo--, hi++)
                pieces[hi + 1] = Math.min(pieces[hi + 1], pieces[lo] + 1);
        }
        this.minPartitions = n == 0 ? 0 : pieces[n] - 1;
    }
}

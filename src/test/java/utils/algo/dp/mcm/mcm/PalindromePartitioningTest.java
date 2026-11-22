package utils.algo.dp.mcm.mcm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PalindromePartitioningTest {

    @ParameterizedTest
    @CsvSource({"'', 0", "a, 0", "aba, 0", "ab, 1", "aab, 1", "abcde, 4", "ababbbabbababa, 3"})
    void knownCases(String s, int expectedCuts) {
        assertEquals(expectedCuts, new PalindromePartitioning(s).minPartitions);
    }

    @Test
    void longStringRunsInQuadraticTime() {
        Random random = new Random(42);
        char[] c = new char[5000];
        for (int i = 0; i < c.length; i++) c[i] = (char) ('a' + random.nextInt(2));
        assertTimeoutPreemptively(java.time.Duration.ofSeconds(1), () -> new PalindromePartitioning(new String(c)));
    }

    @Test
    void randomStringsMatchRecursiveReference() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            StringBuilder sb = new StringBuilder();
            int n = random.nextInt(12);
            for (int i = 0; i < n; i++) sb.append((char) ('a' + random.nextInt(2)));
            String s = sb.toString();
            assertEquals(s.isEmpty() ? 0 : reference(s), new PalindromePartitioning(s).minPartitions, s);
        }
    }

    private static boolean isPalindrome(String s) {
        return new StringBuilder(s).reverse().toString().equals(s);
    }

    private static int reference(String s) {
        if (isPalindrome(s)) return 0;
        int best = Integer.MAX_VALUE;
        for (int k = 1; k < s.length(); k++)
            if (isPalindrome(s.substring(0, k))) best = Math.min(best, 1 + reference(s.substring(k)));
        return best;
    }
}

package utils.algo.dp.longest_common_subsequence;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LCSTest {

    @ParameterizedTest
    @CsvSource({"abcde, ace, 3", "abc, abc, 3", "abc, def, 0", "'', abc, 0", "'', '', 0", "AGGTAB, GXTXAYB, 4"})
    void knownCases(String a, String b, int expected) {
        assertEquals(expected, new LCS(a, b).iterativeDP());
    }

    @Test
    void randomStringsMatchRecursiveReference() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            String a = randomString(random, random.nextInt(9)), b = randomString(random, random.nextInt(9));
            assertEquals(reference(a, b, 0, 0), new LCS(a, b).iterativeDP(), a + " / " + b);
        }
    }

    private static String randomString(Random random, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) sb.append((char) ('a' + random.nextInt(3)));
        return sb.toString();
    }

    private static int reference(String a, String b, int i, int j) {
        if (i == a.length() || j == b.length()) return 0;
        if (a.charAt(i) == b.charAt(j)) return 1 + reference(a, b, i + 1, j + 1);
        return Math.max(reference(a, b, i + 1, j), reference(a, b, i, j + 1));
    }
}

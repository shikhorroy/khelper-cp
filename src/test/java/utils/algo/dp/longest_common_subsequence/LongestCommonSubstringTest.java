package utils.algo.dp.longest_common_subsequence;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LongestCommonSubstringTest {

    @ParameterizedTest
    @CsvSource({"abcde, xbcdy, 3", "abc, abc, 3", "abc, def, 0", "'', abc, 0", "abcxyzab, zzabczz, 3"})
    void knownCases(String a, String b, int expected) {
        assertEquals(expected, new LongestCommonSubstring(a, b).iterativeDP());
    }

    @Test
    void randomStringsMatchBruteForce() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            String a = randomString(random, random.nextInt(9)), b = randomString(random, random.nextInt(9));
            int best = 0;
            for (int i = 0; i < a.length(); i++)
                for (int j = i; j <= a.length(); j++)
                    if (b.contains(a.substring(i, j))) best = Math.max(best, j - i);
            assertEquals(best, new LongestCommonSubstring(a, b).iterativeDP(), a + " / " + b);
        }
    }

    private static String randomString(Random random, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) sb.append((char) ('a' + random.nextInt(3)));
        return sb.toString();
    }
}

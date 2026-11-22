package utils.utility;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class StringUtility {

    /**
     * Complexity: O(n log n)
     *
     * @param string - what need to sort alphabetically
     * @return alphabetically sorted string
     */
    public static String sort(String string) {
        char[] chars = string.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    /**
     * Count character frequency
     * Complexity: O(n), n = length of string
     *
     * @param string - need to count frequency.
     * @return Map of characters count.
     */
    public static Map<Character, Long> countFrequency(String string) {
        return string.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
    }

    /**
     * Check if target string is a sub-sequence of source string.
     * Complexity: O(m), m - length of source string
     *
     * @param source where need to check
     * @param target is to check
     * @return true if target is a sub-sequence, false otherwise. An empty target is always a sub-sequence.
     */
    public static boolean isSubSequence(String source, String target) {
        int sourceLength = source.length(), targetLength = target.length();
        if (sourceLength < targetLength) return false;

        int targetIndex = 0;
        for (int i = 0; i < sourceLength && targetIndex < targetLength; i++) {
            if (source.charAt(i) == target.charAt(targetIndex)) targetIndex++;
        }
        return targetIndex == targetLength;
    }

    public static Set<Character> uniqueCharsSet(String string) {
        return string.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toSet());
    }

    public static Set<Character> sortedUniqueCharsSet(String string) {
        return string.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}

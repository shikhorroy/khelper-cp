package utils.utility;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilityTest {

    @Test
    void emptyTargetIsAlwaysASubSequence() {
        assertTrue(StringUtility.isSubSequence("abc", ""));
        assertTrue(StringUtility.isSubSequence("", ""));
    }

    @Test
    void isSubSequence() {
        assertTrue(StringUtility.isSubSequence("abcde", "ace"));
        assertTrue(StringUtility.isSubSequence("abcde", "abcde"));
        assertFalse(StringUtility.isSubSequence("abcde", "acf"));
        assertFalse(StringUtility.isSubSequence("abcde", "ea"));
        assertFalse(StringUtility.isSubSequence("ab", "abc"));
        assertTrue(StringUtility.isSubSequence("aab", "ab"));
    }

    @Test
    void sortSortsCharacters() {
        assertEquals("aabcd", StringUtility.sort("dbaca"));
        assertEquals("", StringUtility.sort(""));
    }

    @Test
    void countFrequencyAndUniqueChars() {
        assertEquals(Map.of('a', 2L, 'b', 1L), StringUtility.countFrequency("aba"));
        assertEquals(Set.of('a', 'b'), StringUtility.uniqueCharsSet("abba"));
        assertEquals(List.of('a', 'b', 'c'), List.copyOf(StringUtility.sortedUniqueCharsSet("cabca")));
    }
}

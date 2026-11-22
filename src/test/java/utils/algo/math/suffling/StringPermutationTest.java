package utils.algo.math.suffling;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringPermutationTest {

    @Test
    void emptyStringHasOnePermutation() {
        assertEquals(List.of(""), new StringPermutation("").list());
    }

    @Test
    void repeatedLettersGiveDuplicatesInListButNotInSet() {
        StringPermutation sp = new StringPermutation("aab");
        assertEquals(6, sp.list().size());
        assertEquals(3, sp.set().size());
        assertTrue(sp.set().contains("aba"));
        assertEquals(6, sp.count());
        assertEquals(3, sp.countDistinct());
    }

    @Test
    void distinctLettersGiveFactorialPermutations() {
        assertEquals(24, new StringPermutation("abcd").set().size());
        assertEquals(120, new StringPermutation("abcde").list().size());
    }

    @Test
    void repeatedCallsDoNotGenerateTwice() {
        StringPermutation sp = new StringPermutation("abc");
        sp.permute();
        sp.permute();
        sp.list();
        assertEquals(6, sp.count());
    }

    @Test
    void countsAreZeroBeforeGeneration() {
        StringPermutation sp = new StringPermutation("abc");
        assertEquals(0, sp.count());
        assertEquals(0, sp.countDistinct());
    }
}

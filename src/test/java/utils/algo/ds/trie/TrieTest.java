package utils.algo.ds.trie;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrieTest {

    //~ small alphabet with a non-letter, a non-ASCII char and a space, so random words share prefixes
    private static final String ALPHABET = "ab#é ";

    private static String randomWord(Random rnd, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) sb.append(ALPHABET.charAt(rnd.nextInt(ALPHABET.length())));
        return sb.toString();
    }

    @Test
    void searchAndLongestPrefixMatchBruteForce() {
        Random rnd = new Random(42);
        for (int t = 0; t < 200; t++) {
            Trie trie = new Trie();
            Set<String> words = new HashSet<>();
            for (int i = 0; i < 20; i++) {
                String w = randomWord(rnd, rnd.nextInt(5));
                trie.add(w);
                words.add(w);
            }
            for (int i = 0; i < 50; i++) {
                String s = randomWord(rnd, rnd.nextInt(7));
                assertEquals(words.contains(s), trie.search(s), "search '" + s + "'");
                String best = "";
                for (int k = 0; k <= s.length(); k++) if (words.contains(s.substring(0, k))) best = s.substring(0, k);
                assertEquals(best, trie.getMatchedPrefixWord(s), "prefix of '" + s + "'");
            }
        }
    }

    @Test
    void prefixOfAddedWordIsNotFoundUnlessAdded() {
        Trie trie = new Trie();
        trie.add("apple");
        assertFalse(trie.search("app"));
        assertTrue(trie.search("apple"));
        trie.add("app");
        assertTrue(trie.search("app"));
    }

    @Test
    void longestPrefixFallsBackToShorterWordWhenLongerPathIsNotAWord() {
        Trie trie = new Trie();
        trie.add("ab");
        trie.add("abcde");
        assertEquals("ab", trie.getMatchedPrefixWord("abcdx"));
        assertEquals("abcde", trie.getMatchedPrefixWord("abcdefg"));
        assertEquals("", trie.getMatchedPrefixWord("xyz"));
    }

    @Test
    void emptyStringIsFoundOnlyAfterItIsAdded() {
        Trie trie = new Trie();
        assertFalse(trie.search(""));
        trie.add("");
        assertTrue(trie.search(""));
        assertEquals("", trie.getMatchedPrefixWord("zzz"));
    }
}

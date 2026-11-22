package utils.algo.ds.trie;

import java.util.HashMap;
import java.util.Map;

class TrieNode {
    private boolean isEnd;
    private final char value;
    private final Map<Character, TrieNode> children;

    public TrieNode(char ch) {
        value = ch;
        isEnd = false;
        children = new HashMap<>();
    }

    public char getValue() {
        return value;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean val) {
        isEnd = val;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }
}

/**
 * Prefix tree of words over any chars. Each node keeps its children in a map.
 * add, search and getMatchedPrefixWord are O(length) map lookups.
 */
public class Trie {
    private final TrieNode root;

    public Trie() {
        root = new TrieNode((char) 0);
    }

    /** Insert a word. The empty string is allowed. */
    public void add(String word) {
        TrieNode node = root;
        for (int i = 0, ln = word.length(); i < ln; ++i) {
            char ch = word.charAt(i);
            node = node.getChildren().computeIfAbsent(ch, TrieNode::new);
        }
        node.setIsEnd(true);
    }

    /** True if {@code key} was added as a whole word. */
    public boolean search(String key) {
        TrieNode node = root;
        for (int i = 0, ln = key.length(); i < ln && node != null; ++i) {
            node = node.getChildren().get(key.charAt(i));
        }
        return node != null && node.isEnd();
    }

    /** The longest prefix of {@code string} that is an added word, or "" if there is none. */
    public String getMatchedPrefixWord(String string) {
        TrieNode node = root;
        int bestLength = 0;
        for (int i = 0, ln = string.length(); i < ln; ++i) {
            node = node.getChildren().get(string.charAt(i));
            if (node == null) break;
            if (node.isEnd()) bestLength = i + 1;
        }
        return string.substring(0, bestLength);
    }
}

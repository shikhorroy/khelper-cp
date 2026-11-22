package utils.algo.math.suffling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * All permutations of a string, by swapping in place. Results are computed once and cached.
 * Complexity: O(n * n!) time and memory, so only use it for short strings (n up to about 10).
 */
public class StringPermutation {

    private final char[] chars;
    private final Set<String> stringSet;
    private final List<String> stringList;
    //~ True after the permutations are generated; an empty result is not used as the flag.
    private boolean permuted;
    private boolean deduplicated;

    public StringPermutation(String string) {
        chars = string.toCharArray();

        stringSet = new HashSet<>();
        stringList = new ArrayList<>();
    }

    /**
     * Generate all permutation list (duplicate elements may found). The empty string has one permutation: "".
     *
     * @return this, for chaining.
     */
    public StringPermutation permute() {
        if (!permuted) {
            permuted = true;
            if (chars.length == 0) stringList.add("");
            else this.permute(0, this.chars.length - 1);
        }
        return this;
    }

    private void permute(int leftIndex, int rightIndex) {
        if (leftIndex == rightIndex) {
            stringList.add(new String(chars));
        } else {
            for (int i = leftIndex; i <= rightIndex; i++) {
                swap(leftIndex, i);
                this.permute(leftIndex + 1, rightIndex);
                swap(leftIndex, i);
            }
        }
    }

    private void swap(int i, int j) {
        char t = chars[i];
        chars[i] = chars[j];
        chars[j] = t;
    }

    /**
     * Generate the set of distinct permutations.
     *
     * @return this, for chaining.
     */
    public StringPermutation unique() {
        if (!deduplicated) {
            deduplicated = true;
            this.permute();
            this.stringSet.addAll(this.stringList);
        }
        return this;
    }

    public List<String> list() {
        this.permute();
        return this.stringList;
    }

    public Set<String> set() {
        this.unique();
        return this.stringSet;
    }

    /**
     * @return number of generated permutations (with duplicates); 0 until permute() or list() is called.
     */
    public int count() {
        return this.stringList.size();
    }

    /**
     * @return number of distinct permutations; 0 until unique() or set() is called.
     */
    public int countDistinct() {
        return this.stringSet.size();
    }
}

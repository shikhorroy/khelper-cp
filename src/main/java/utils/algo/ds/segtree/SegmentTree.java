package utils.algo.ds.segtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic segment tree over a list. A subclass gives the combine operation ({@link #calculate}) and its
 * neutral value ({@link #invalidReturn}).
 * Build: O(n). Range query and point update: O(log n). Range update: O(r - l + 1), it visits every leaf
 * in the range (no lazy propagation).
 * Node 1 is the root, node i has children 2i and 2i+1. Query bounds outside [0, n-1] are clamped.
 *
 * @param <T> element type
 */
public abstract class SegmentTree<T> {

    //~ number of elements
    private final int n;
    //~ tree.get(i) = calculate over the segment of node i; index 0 is unused
    protected final List<T> tree;

    /**
     * Value for an empty range. It must be neutral for {@link #calculate}:
     * e.g. 0 for range sum, +infinity for range minimum.
     */
    public abstract T invalidReturn();

    /**
     * Combine the values of two neighbour segments, e.g. {@code leftValue + rightValue} for range sum,
     * {@code Math.min(leftValue, rightValue)} for range minimum.
     *
     * @param leftValue  left subtree value
     * @param rightValue right subtree value
     * @return the combined value
     */
    public abstract T calculate(T leftValue, T rightValue);

    /**
     * Change a leaf on update. The default sets the leaf to {@code value}.
     * Override it to e.g. add instead: {@code tree.set(currIndex, tree.get(currIndex) + value)}.
     *
     * @param value     the update value
     * @param currIndex tree index of the leaf
     */
    public void applyChange(T value, int currIndex) {
        tree.set(currIndex, value);
    }

    public SegmentTree(List<T> array) {
        this.n = array.size();
        this.tree = new ArrayList<>(Collections.nCopies(4 * n + 1, (T) null));
        if (n > 0) buildTree(array, 0, n - 1, 1);
    }

    private void buildTree(List<T> array, int start, int end, int currIndex) {
        if (start == end) {
            tree.set(currIndex, array.get(start));
            return;
        }
        int mid = (start + end) >>> 1;
        buildTree(array, start, mid, 2 * currIndex);
        buildTree(array, mid + 1, end, 2 * currIndex + 1);
        pull(currIndex);
    }

    /**
     * Combine the values from index start to index end, both inclusive and 0-based. O(log n).
     * An empty range returns {@link #invalidReturn()}.
     */
    public T query(int start, int end) {
        if (n == 0) return invalidReturn();
        return query(0, n - 1, start, end, 1);
    }

    /**
     * Apply {@link #applyChange} to the leaf at {@code index}. O(log n).
     *
     * @param index 0-based index
     * @param val   the update value
     */
    public void update(int index, T val) {
        if (n == 0) return;
        update(0, n - 1, index, index, val, 1);
    }

    /**
     * Apply {@link #applyChange} to every leaf from index start to index end, both inclusive.
     * O(end - start + 1), because it has no lazy propagation.
     */
    public void update(int start, int end, T val) {
        if (n == 0) return;
        update(0, n - 1, start, end, val, 1);
    }

    private void update(int start, int end, int ql, int qr, T val, int currIndex) {
        if (end < ql || qr < start) return;

        if (start == end) {
            applyChange(val, currIndex);
            return;
        }
        int mid = (start + end) >>> 1;
        update(start, mid, ql, qr, val, 2 * currIndex);
        update(mid + 1, end, ql, qr, val, 2 * currIndex + 1);
        pull(currIndex);
    }

    private T query(int start, int end, int ql, int qr, int currIndex) {
        //~ full overlap: node segment is inside the query range
        if (ql <= start && end <= qr) return tree.get(currIndex);
        //~ no overlap: node segment is outside the query range
        if (end < ql || qr < start) return invalidReturn();

        int mid = (start + end) >>> 1;
        T left = query(start, mid, ql, qr, 2 * currIndex);
        T right = query(mid + 1, end, ql, qr, 2 * currIndex + 1);
        return calculate(left, right);
    }

    private void pull(int currIndex) {
        tree.set(currIndex, calculate(tree.get(2 * currIndex), tree.get(2 * currIndex + 1)));
    }
}

package utils.algo.ds.query.sparse_table;

import utils.algo.ds.query.RangeQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Sparse table for an idempotent operation (min, max, gcd, lcm, ...).
 * Row k holds {@code compute} over each window of length 2^k.
 * Build: O(n log n). Query: O(1), two overlapping windows.
 * The input list is copied, so later changes to it do not affect the table.
 *
 * @param <T> element type
 */
public abstract class SparseTable<T> implements RangeQuery<T> {

    //~ copy of the input; also row 0 of the table
    protected final List<T> itemList;
    //~ table.get(k).get(i) = compute over itemList[i .. i + 2^k - 1]
    protected final List<List<T>> table;

    public SparseTable(List<T> itemList) {
        this.itemList = new ArrayList<>(itemList);
        this.table = new ArrayList<>();
        build();
    }

    /** Fill every row k while a window of length 2^k still fits. O(n log n). */
    private void build() {
        int n = itemList.size();
        table.add(itemList);
        for (int k = 1; (1 << k) <= n; k++) {
            List<T> prev = table.get(k - 1);
            int half = 1 << (k - 1);
            int rowSize = n - (1 << k) + 1;
            List<T> row = new ArrayList<>(rowSize);
            for (int i = 0; i < rowSize; i++) row.add(compute(prev.get(i), prev.get(i + half)));
            table.add(row);
        }
    }

    /** floor(log2(len)) for len >= 1, exact and O(1). */
    protected static int log2(int len) {
        return 31 - Integer.numberOfLeadingZeros(len);
    }

    @Override
    public T query(int l, int r) {
        int k = log2(r - l + 1);
        List<T> row = table.get(k);
        return compute(row.get(l), row.get(r - (1 << k) + 1));
    }
}

package utils.algo.ds.query.sparse_table;

import java.util.List;

/**
 * Range sum on top of a sparse table. Sum is not idempotent, so the windows must not overlap:
 * the range is split into O(log n) disjoint power-of-two windows. Query: O(log n).
 * Integer and Long sums wrap on overflow like plain int and long. Other number types are summed as Double.
 * An empty range (l &gt; r) returns 0.
 */
public class SUMSparseTable<T extends Number> extends SparseTable<T> {

    public SUMSparseTable(List<T> itemList) {
        super(itemList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T compute(T elem1, T elem2) {
        if (elem1 instanceof Integer) return (T) Integer.valueOf(elem1.intValue() + elem2.intValue());
        if (elem1 instanceof Long) return (T) Long.valueOf(elem1.longValue() + elem2.longValue());
        return (T) Double.valueOf(elem1.doubleValue() + elem2.doubleValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public T query(int l, int r) {
        T answer = null;
        while (l <= r) {
            int k = log2(r - l + 1);
            T part = table.get(k).get(l);
            answer = answer == null ? part : compute(answer, part);
            l += 1 << k;
        }
        if (answer != null) return answer;

        //~ empty range: return 0 of the element type
        Number first = itemList.isEmpty() ? null : itemList.get(0);
        if (first instanceof Integer) return (T) Integer.valueOf(0);
        if (first instanceof Long) return (T) Long.valueOf(0L);
        return (T) Double.valueOf(0.0);
    }
}

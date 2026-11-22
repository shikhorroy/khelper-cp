package utils.algo.ds.query.sparse_table;

import utils.algo.math.LCM;

import java.util.List;

/**
 * Range lcm in O(1) per query after O(n log n) build. lcm(0, x) = 0.
 * Integer and Long are computed as a / gcd(a, b) * b with no allocation; on overflow the value wraps
 * like plain int or long. Other number types fall back to {@link LCM#of}.
 */
public class LCMSparseTable<T extends Number> extends SparseTable<T> {

    public LCMSparseTable(List<T> itemList) {
        super(itemList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T compute(T elem1, T elem2) {
        if (elem1 instanceof Integer) return (T) Integer.valueOf((int) lcm(elem1.longValue(), elem2.longValue()));
        if (elem1 instanceof Long) return (T) Long.valueOf(lcm(elem1.longValue(), elem2.longValue()));
        return LCM.of(elem1, elem2);
    }

    private static long lcm(long a, long b) {
        if (a == 0 || b == 0) return 0;
        return a / GCDSparseTable.gcd(a, b) * b;
    }
}

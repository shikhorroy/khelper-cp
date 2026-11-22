package utils.algo.ds.query.sparse_table;

import utils.algo.math.GCD;

import java.util.List;

/**
 * Range gcd in O(1) per query after O(n log n) build. The result is never negative; gcd(0, x) = |x|.
 * Integer and Long use plain Euclid with no allocation. Other number types fall back to {@link GCD#of}.
 */
public class GCDSparseTable<T extends Number> extends SparseTable<T> {

    public GCDSparseTable(List<T> itemList) {
        super(itemList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T compute(T elem1, T elem2) {
        if (elem1 instanceof Integer) return (T) Integer.valueOf((int) gcd(elem1.longValue(), elem2.longValue()));
        if (elem1 instanceof Long) return (T) Long.valueOf(gcd(elem1.longValue(), elem2.longValue()));
        return GCD.of(elem1, elem2);
    }

    static long gcd(long a, long b) {
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return Math.abs(a);
    }
}

package utils.algo.ds.query;

/**
 * A range query over a fixed array: combine all elements from index l to index r with {@link #compute}.
 *
 * @param <T> element type
 */
public interface RangeQuery<T> {

    /**
     * Combine the elements from index l to index r, both inclusive and 0-based.
     *
     * @param l left index
     * @param r right index
     * @return the result of {@link #compute} over the range
     */
    T query(int l, int r);

    /**
     * The combine operation, e.g. min, max, gcd, lcm or sum.
     *
     * @param elem1 first element
     * @param elem2 second element
     * @return the combined value
     */
    T compute(T elem1, T elem2);
}

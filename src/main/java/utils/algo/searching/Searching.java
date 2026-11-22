package utils.algo.searching;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Binary search helpers. The input must be sorted in ascending order. All searches are O(log n)
 * (for a List, only when it supports fast random access, like ArrayList).
 */
public class Searching {

    /**
     * LOWER: first index whose value is {@code >= key} (C++ lower_bound).
     * UPPER: first index whose value is {@code > key} (C++ upper_bound).
     */
    public enum Boundary {
        LOWER, UPPER
    }

    /**
     * @return index of key, or {@code -(insertionPoint) - 1} if not found (same as Arrays.binarySearch).
     */
    public static int binarySearch(int[] a, int key) {
        return Arrays.binarySearch(a, key);
    }

    /**
     * @return index of key, or {@code -(insertionPoint) - 1} if not found (same as Arrays.binarySearch).
     */
    public static int binarySearch(long[] a, long key) {
        return Arrays.binarySearch(a, key);
    }

    /**
     * @return index of key, or {@code -(insertionPoint) - 1} if not found (same as Collections.binarySearch).
     */
    public static <T>
    int binarySearch(List<? extends Comparable<? super T>> list, T key) {
        return Collections.binarySearch(list, key);
    }

    /**
     * Lower or upper bound in a sorted list. Returns a value in [0, size]; size means "no such element".
     * The elements must implement Comparable.
     */
    @SuppressWarnings("unchecked")
    public static <T> int boundarySearch(List<T> array, T key, Boundary bound) {
        final boolean upper = bound == Boundary.UPPER;
        int low = 0;
        int high = array.size();
        while (low < high) {
            final int mid = (low + high) >>> 1;
            int cmp = ((Comparable<? super T>) array.get(mid)).compareTo(key);

            if (cmp < 0 || (upper && cmp == 0)) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    /**
     * Primitive lower or upper bound (no boxing). Returns a value in [0, a.length].
     */
    public static int boundarySearch(int[] a, int key, Boundary bound) {
        final boolean upper = bound == Boundary.UPPER;
        int low = 0, high = a.length;
        while (low < high) {
            final int mid = (low + high) >>> 1;
            if (a[mid] < key || (upper && a[mid] == key)) low = mid + 1;
            else high = mid;
        }
        return low;
    }

    /**
     * Primitive lower or upper bound (no boxing). Returns a value in [0, a.length].
     */
    public static int boundarySearch(long[] a, long key, Boundary bound) {
        final boolean upper = bound == Boundary.UPPER;
        int low = 0, high = a.length;
        while (low < high) {
            final int mid = (low + high) >>> 1;
            if (a[mid] < key || (upper && a[mid] == key)) low = mid + 1;
            else high = mid;
        }
        return low;
    }
}

package utils.algo.sorting.mergesort;

import java.util.List;

/***
 * Inversion Count of an Array:
 * Given an array, find the total number of inversions of it. If (i < j) and (A[i] >= A[j]), then pair (i, j) is called
 * an inversion of an array A. We need to count all such pairs in the array.
 * Note: equal pairs are counted too. The classic definition uses A[i] > A[j].
 *
 * For example,
 * Input:  A = [1, 9, 6, 4, 5, 9]
 * Output: The inversion count is: 6
 * Inversions are: (9, 6), (9, 4), (9, 5), (9, 9), (6, 4), (6, 5)
 *
 * Complexity: O(n log n) time, O(n) extra memory. The input is never modified.
 * Source: Modified version of - https://www.topcoder.com/thrive/articles/count-inversions-in-an-array
 */
public class CountInversion {

    /**
     * Counts pairs i < j with A[i] >= A[j]. A null list counts as empty. The list is not modified, so immutable
     * lists work too.
     */
    public static <T extends Comparable<T>> long run(List<T> elems) {
        if (elems == null || elems.size() < 2) return 0;
        Object[] a = elems.toArray();
        return sortCount(a, new Object[a.length], 0, a.length - 1);
    }

    /**
     * Primitive version (no boxing). Counts pairs i < j with a[i] >= a[j]. The array is not modified.
     */
    public static long run(int[] a) {
        if (a == null || a.length < 2) return 0;
        int[] copy = a.clone();
        return sortCount(copy, new int[a.length], 0, a.length - 1);
    }

    /**
     * Primitive version (no boxing). Counts pairs i < j with a[i] >= a[j]. The array is not modified.
     */
    public static long run(long[] a) {
        if (a == null || a.length < 2) return 0;
        long[] copy = a.clone();
        return sortCount(copy, new long[a.length], 0, a.length - 1);
    }

    @SuppressWarnings("unchecked")
    private static long sortCount(Object[] a, Object[] buf, int lo, int hi) {
        if (lo >= hi) return 0;
        int mid = (lo + hi) >>> 1;
        long count = sortCount(a, buf, lo, mid) + sortCount(a, buf, mid + 1, hi);
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            if (((Comparable<Object>) a[i]).compareTo(a[j]) >= 0) {//~ A[i] >= A[j]: all of a[i..mid] count
                count += mid + 1 - i;
                buf[k++] = a[j++];
            } else buf[k++] = a[i++];
        }
        while (i <= mid) buf[k++] = a[i++];
        while (j <= hi) buf[k++] = a[j++];
        System.arraycopy(buf, lo, a, lo, hi - lo + 1);
        return count;
    }

    private static long sortCount(int[] a, int[] buf, int lo, int hi) {
        if (lo >= hi) return 0;
        int mid = (lo + hi) >>> 1;
        long count = sortCount(a, buf, lo, mid) + sortCount(a, buf, mid + 1, hi);
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            if (a[i] >= a[j]) {
                count += mid + 1 - i;
                buf[k++] = a[j++];
            } else buf[k++] = a[i++];
        }
        while (i <= mid) buf[k++] = a[i++];
        while (j <= hi) buf[k++] = a[j++];
        System.arraycopy(buf, lo, a, lo, hi - lo + 1);
        return count;
    }

    private static long sortCount(long[] a, long[] buf, int lo, int hi) {
        if (lo >= hi) return 0;
        int mid = (lo + hi) >>> 1;
        long count = sortCount(a, buf, lo, mid) + sortCount(a, buf, mid + 1, hi);
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            if (a[i] >= a[j]) {
                count += mid + 1 - i;
                buf[k++] = a[j++];
            } else buf[k++] = a[i++];
        }
        while (i <= mid) buf[k++] = a[i++];
        while (j <= hi) buf[k++] = a[j++];
        System.arraycopy(buf, lo, a, lo, hi - lo + 1);
        return count;
    }
}

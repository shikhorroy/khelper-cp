package utils.algo.searching;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static utils.algo.searching.Searching.Boundary.LOWER;
import static utils.algo.searching.Searching.Boundary.UPPER;

class SearchingTest {

    @Test
    void lowerAndUpperBoundOnList() {
        List<Integer> l = List.of(1, 2, 2, 2, 5);
        assertEquals(1, Searching.boundarySearch(l, 2, LOWER));
        assertEquals(4, Searching.boundarySearch(l, 2, UPPER));
        assertEquals(5, Searching.boundarySearch(l, 9, UPPER));
        assertEquals(0, Searching.boundarySearch(l, 0, LOWER));
    }

    @Test
    void emptyInputGivesZero() {
        assertEquals(0, Searching.boundarySearch(new int[0], 3, LOWER));
        assertEquals(0, Searching.boundarySearch(new long[0], 3, UPPER));
        assertEquals(0, Searching.boundarySearch(new ArrayList<Integer>(), 3, LOWER));
    }

    @Test
    void allBoundOverloadsMatchBruteForce() {
        Random rnd = new Random(42);
        for (int it = 0; it < 500; it++) {
            int n = rnd.nextInt(20);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(10);
            Arrays.sort(a);
            long[] la = Arrays.stream(a).asLongStream().toArray();
            List<Integer> list = new ArrayList<>();
            for (int v : a) list.add(v);
            for (int key = -1; key <= 11; key++) {
                int lo = 0, up = 0;
                for (int v : a) {
                    if (v < key) lo++;
                    if (v <= key) up++;
                }
                assertEquals(lo, Searching.boundarySearch(a, key, LOWER));
                assertEquals(up, Searching.boundarySearch(a, key, UPPER));
                assertEquals(lo, Searching.boundarySearch(la, key, LOWER));
                assertEquals(up, Searching.boundarySearch(la, key, UPPER));
                assertEquals(lo, Searching.boundarySearch(list, key, LOWER));
                assertEquals(up, Searching.boundarySearch(list, key, UPPER));
            }
        }
    }

    @Test
    void binarySearchReturnsIndexOrNegativeInsertionPoint() {
        int[] a = {1, 3, 5};
        assertEquals(1, Searching.binarySearch(a, 3));
        assertEquals(-3, Searching.binarySearch(a, 4));
        assertEquals(2, Searching.binarySearch(new long[]{1, 3, 5}, 5L));
        assertEquals(0, Searching.binarySearch(List.of(1, 3, 5), 1));
    }
}

package utils.algo.ds.dsu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisjointSetUnionTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 7, 8, 9, 31, 60, 200})
    void unionSameParentSizeAndComponentsMatchBruteForce(int n) {
        Random rnd = new Random(42);
        DisjointSetUnion dsu = new DisjointSetUnion(n);
        //~ comp[v] is a label of v's set; merging relabels one whole set
        int[] comp = new int[n];
        for (int i = 0; i < n; i++) comp[i] = i;

        for (int q = 0; q < 3 * n + 50; q++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            if (rnd.nextBoolean()) {
                assertEquals(comp[a] != comp[b], dsu.union(a, b), "union(" + a + ", " + b + ")");
                int keep = comp[a];
                int drop = comp[b];
                for (int i = 0; i < n; i++) if (comp[i] == drop) comp[i] = keep;
            } else {
                assertEquals(comp[a] == comp[b], dsu.sameParent(a, b), "sameParent(" + a + ", " + b + ")");
            }
            int v = rnd.nextInt(n);
            int size = 0;
            for (int i = 0; i < n; i++) if (comp[i] == comp[v]) size++;
            assertEquals(size, dsu.size(v), "size(" + v + ")");
            assertEquals(Arrays.stream(comp).distinct().count(), dsu.components());
        }
    }

    @Test
    void unionOfNodesAlreadyTogetherKeepsSizeUnchanged() {
        //~ before the fix, a second union of the same set doubled the root's stored size
        DisjointSetUnion dsu = new DisjointSetUnion(4);
        assertTrue(dsu.union(0, 1));
        assertFalse(dsu.union(1, 0));
        assertFalse(dsu.union(0, 0));
        assertEquals(2, dsu.size(0));
        assertEquals(3, dsu.components());
    }

    @Test
    void longChainOfUnionsDoesNotOverflowStack() {
        int n = 1_000_000;
        DisjointSetUnion dsu = new DisjointSetUnion(n);
        for (int i = 1; i < n; i++) dsu.union(i - 1, i);
        int root = dsu.find(0);
        for (int i = 0; i < n; i++) assertEquals(root, dsu.find(i));
        assertEquals(n, dsu.size(n - 1));
        assertEquals(1, dsu.components());
    }

    @Test
    void zeroNodesHasZeroComponents() {
        assertEquals(0, new DisjointSetUnion(0).components());
    }
}

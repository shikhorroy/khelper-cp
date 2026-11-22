package utils.algo.graph.tree;

import org.junit.jupiter.api.Test;
import utils.algo.graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LCATest {

    @Test
    void queryNearMaxDepthDoesNotGoOutOfBounds() {
        Tree t = new Tree(6, 0);
        t.addEdge(0, 1);
        t.addEdge(1, 2);
        t.addEdge(2, 3);
        t.addEdge(3, 4);
        t.addEdge(0, 5);
        assertEquals(0, new LCA(t).findLCA(4, 5));
    }

    @Test
    void deepPathDoesNotOverflowStack() {
        int n = 200_000;
        Tree t = new Tree(n, 0);
        for (int i = 1; i < n; i++) t.addEdge(i - 1, i);
        LCA lca = new LCA(t);
        Random random = new Random(42);
        for (int q = 0; q < 100_000; q++) {
            int u = random.nextInt(n), v = random.nextInt(n);
            assertEquals(Math.min(u, v), lca.findLCA(u, v));
        }
        assertEquals(0, lca.kthAncestor(n - 1, n - 1));
        assertEquals(n - 1, lca.levelOf(n - 1));
    }

    @Test
    void levelOfRejectsNodeEqualToCount() {
        LCA lca = new LCA(new Tree(2, 0));
        assertThrows(AssertionError.class, () -> lca.levelOf(2));
    }

    @Test
    void singleNodeTree() {
        LCA lca = new LCA(new Tree(1, 0));
        assertEquals(0, lca.findLCA(0, 0));
        assertEquals(0, lca.kthAncestor(0, 0));
        assertEquals(-1, lca.kthAncestor(0, 1));
    }

    @Test
    void duplicateEdgeIsIgnored() {
        Tree t = new Tree(3, 0);
        t.addEdge(0, 1);
        t.addEdge(0, 1);
        t.addEdge(1, 2);
        LCA lca = new LCA(t);
        assertEquals(2, lca.levelOf(2));
        assertEquals(1, lca.findLCA(1, 2));
    }

    @Test
    void kthAncestorBeyondRootIsMinusOne() {
        Tree t = new Tree(3, 0);
        t.addEdge(0, 1);
        t.addEdge(1, 2);
        LCA lca = new LCA(t);
        assertEquals(0, lca.kthAncestor(2, 2));
        assertEquals(-1, lca.kthAncestor(2, 3));
        assertEquals(-1, lca.kthAncestor(2, -1));
    }

    @Test
    void randomTreesMatchNaiveParentClimb() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            int n = 1 + random.nextInt(30);
            int root = random.nextInt(n);
            List<Integer> order = new ArrayList<>();
            for (int i = 0; i < n; i++) order.add(i);
            Collections.shuffle(order, random);
            order.remove((Integer) root);
            order.add(0, root);

            //~ parent[v] = parent in the random tree, -1 for the root
            int[] parent = new int[n];
            parent[root] = -1;
            Tree tree = new Tree(n, root);
            if (random.nextBoolean()) tree.setType(Graph.Type.BIDIRECTIONAL);
            for (int i = 1; i < n; i++) {
                int child = order.get(i), p = order.get(random.nextInt(i));
                parent[child] = p;
                tree.addEdge(p, child);
            }
            LCA lca = new LCA(tree);

            int[] depth = new int[n];
            for (int v = 0; v < n; v++) {
                for (int x = v; parent[x] != -1; x = parent[x]) depth[v]++;
                assertEquals(depth[v], lca.levelOf(v));
            }
            for (int q = 0; q < 30; q++) {
                int u = random.nextInt(n), v = random.nextInt(n);
                Set<Integer> ancestors = new HashSet<>();
                for (int x = u; x != -1; x = parent[x]) ancestors.add(x);
                int x = v;
                while (!ancestors.contains(x)) x = parent[x];
                assertEquals(x, lca.findLCA(u, v), "case " + t);

                int k = random.nextInt(n + 1);
                int y = u;
                for (int i = 0; i < k && y != -1; i++) y = parent[y];
                assertEquals(k > depth[u] ? -1 : y, lca.kthAncestor(u, k));
            }
        }
    }
}

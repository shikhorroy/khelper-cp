package utils.algo.graph.dfs;

import org.junit.jupiter.api.Test;
import utils.algo.graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DfsTest {

    @Test
    void deepPathDoesNotOverflowStack() {
        int n = 200_000;
        Graph g = new Graph(n);
        for (int i = 1; i < n; i++) g.addEdge(i - 1, i);
        Dfs.run(g, 0);
        assertEquals(n, Dfs.orderSize);
        assertEquals(n - 1, Dfs.order[n - 1]);
    }

    @Test
    void unreachableNodeIsNotVisited() {
        Graph g = new Graph(3);
        g.addEdge(0, 1);
        Dfs.run(g, 0);
        assertArrayEquals(new boolean[]{true, true, false}, Dfs.visited);
        assertEquals(2, Dfs.orderSize);
    }

    @Test
    void randomGraphsGiveSamePreorderAsRecursiveDfs() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            int n = 1 + random.nextInt(12);
            Graph g = random.nextBoolean() ? new Graph(n) : new Graph(n, Graph.Type.UNIDIRECTIONAL);
            int m = random.nextInt(25);
            for (int i = 0; i < m; i++) g.addEdge(random.nextInt(n), random.nextInt(n));
            int source = random.nextInt(n);

            List<Integer> expected = new ArrayList<>();
            boolean[] seen = new boolean[n];
            recursiveDfs(g, source, seen, expected);

            Dfs.run(g, source);
            List<Integer> actual = new ArrayList<>();
            for (int i = 0; i < Dfs.orderSize; i++) actual.add(Dfs.order[i]);
            assertEquals(expected, actual, "case " + t);
            assertArrayEquals(seen, Dfs.visited);
        }
    }

    private static void recursiveDfs(Graph g, int u, boolean[] seen, List<Integer> order) {
        seen[u] = true;
        order.add(u);
        for (int v : g.adjacencyList.get(u)) if (!seen[v]) recursiveDfs(g, v, seen, order);
    }
}

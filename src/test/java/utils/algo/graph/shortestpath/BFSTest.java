package utils.algo.graph.shortestpath;

import org.junit.jupiter.api.Test;
import utils.algo.graph.Graph;
import utils.algo.graph.WeightedGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BFSTest {

    @Test
    void unreachableNodeHasDistanceMinusOne() {
        BFS.run(new Graph(2), 0);
        assertArrayEquals(new int[]{0, -1}, BFS.distance);
        assertTrue(BFS.getPath(1).isEmpty());
    }

    @Test
    void stateIsNotOverwrittenByDijkstraRun() {
        Graph g = new Graph(3);
        g.addEdge(0, 1);
        BFS.run(g, 0);
        Dijkstra.run(new WeightedGraph(3), 2);
        assertEquals(List.of(0, 1), BFS.getPath(1));
    }

    @Test
    void sourceOnlyGraph() {
        BFS.run(new Graph(1), 0);
        assertArrayEquals(new int[]{0}, BFS.distance);
        assertEquals(List.of(0), BFS.getPath(0));
    }

    @Test
    void deepPathDoesNotOverflow() {
        int n = 200_000;
        Graph g = new Graph(n);
        for (int i = 1; i < n; i++) g.addEdge(i - 1, i);
        BFS.run(g, 0);
        assertEquals(n - 1, BFS.distance[n - 1]);
        assertEquals(n, BFS.getPath(n - 1).size());
    }

    @Test
    void randomGraphsMatchBellmanFordWithUnitWeights() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            int n = 1 + random.nextInt(9);
            boolean bidirectional = random.nextBoolean();
            Graph g = bidirectional ? new Graph(n) : new Graph(n, Graph.Type.UNIDIRECTIONAL);
            List<int[]> edges = new ArrayList<>();
            int m = random.nextInt(20);
            for (int i = 0; i < m; i++) {
                int u = random.nextInt(n), v = random.nextInt(n);
                g.addEdge(u, v);
                edges.add(new int[]{u, v});
                if (bidirectional) edges.add(new int[]{v, u});
            }
            int source = random.nextInt(n);
            BFS.run(g, source);
            int[] expected = unitDistances(n, edges, source);
            assertArrayEquals(expected, BFS.distance, "case " + t);
            for (int v = 0; v < n; v++) {
                List<Integer> path = BFS.getPath(v);
                assertEquals(expected[v] == -1 ? 0 : expected[v] + 1, path.size());
                if (!path.isEmpty()) {
                    assertEquals(source, path.get(0));
                    assertEquals(v, path.get(path.size() - 1));
                }
            }
        }
    }

    private static int[] unitDistances(int n, List<int[]> edges, int source) {
        int[] d = new int[n];
        Arrays.fill(d, -1);
        d[source] = 0;
        for (int it = 0; it < n; it++)
            for (int[] e : edges)
                if (d[e[0]] != -1 && (d[e[1]] == -1 || d[e[0]] + 1 < d[e[1]])) d[e[1]] = d[e[0]] + 1;
        return d;
    }
}

package utils.algo.graph.shortestpath;

import org.junit.jupiter.api.Test;
import utils.algo.graph.Graph;
import utils.algo.graph.WeightedGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraTest {

    @Test
    void distancesDoNotOverflowWithLargeWeights() {
        WeightedGraph g = new WeightedGraph(3);
        g.addEdge(0, 1, 1_500_000_000);
        g.addEdge(1, 2, 1_500_000_000);
        Dijkstra.run(g, 0);
        assertArrayEquals(new long[]{0, 1_500_000_000L, 3_000_000_000L}, Dijkstra.distance);
    }

    @Test
    void unreachableNodeHasInfDistanceAndNoPath() {
        WeightedGraph g = new WeightedGraph(2);
        Dijkstra.run(g, 0);
        assertEquals(Dijkstra.INF, Dijkstra.distance[1]);
        assertTrue(Dijkstra.getPath(1).isEmpty());
    }

    @Test
    void stateIsNotOverwrittenByBfsRun() {
        WeightedGraph g = new WeightedGraph(3);
        g.addEdge(0, 1, 5);
        Dijkstra.run(g, 0);
        BFS.run(new Graph(3), 2);
        assertEquals(List.of(0, 1), Dijkstra.getPath(1));
    }

    @Test
    void largeRandomGraphRunsFast() {
        Random random = new Random(42);
        int n = 200_000;
        WeightedGraph g = new WeightedGraph(n);
        for (int i = 1; i < n; i++) g.addEdge(random.nextInt(i), i, random.nextInt(1_000_000_000));
        for (int i = 0; i < 300_000; i++) g.addEdge(random.nextInt(n), random.nextInt(n), random.nextInt(1_000_000_000));
        Dijkstra.run(g, 0);
        for (long d : Dijkstra.distance) assertTrue(d >= 0 && d < Dijkstra.INF);
    }

    @Test
    void randomGraphsMatchBellmanFordAndPathsSumToDistance() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            int n = 1 + random.nextInt(9);
            boolean bidirectional = random.nextBoolean();
            boolean bigWeights = random.nextBoolean();
            WeightedGraph g = bidirectional ? new WeightedGraph(n) : new WeightedGraph(n, Graph.Type.UNIDIRECTIONAL);
            List<int[]> edges = new ArrayList<>();
            int m = random.nextInt(20);
            for (int i = 0; i < m; i++) {
                int u = random.nextInt(n), v = random.nextInt(n);
                int w = bigWeights ? random.nextInt(Integer.MAX_VALUE) : random.nextInt(10);
                g.addEdge(u, v, w);
                edges.add(new int[]{u, v, w});
                if (bidirectional) edges.add(new int[]{v, u, w});
            }
            int source = random.nextInt(n);
            Dijkstra.run(g, source);
            long[] expected = bellmanFord(n, edges, source);
            assertArrayEquals(expected, Dijkstra.distance, "case " + t);
            for (int v = 0; v < n; v++) {
                List<Integer> path = Dijkstra.getPath(v);
                if (expected[v] == Dijkstra.INF) {
                    assertTrue(path.isEmpty());
                    continue;
                }
                assertEquals(source, path.get(0));
                assertEquals(v, path.get(path.size() - 1));
                assertEquals(expected[v], pathCost(path, edges));
            }
        }
    }

    private static long pathCost(List<Integer> path, List<int[]> edges) {
        long sum = 0;
        for (int i = 0; i + 1 < path.size(); i++) {
            long best = Long.MAX_VALUE;
            for (int[] e : edges) if (e[0] == path.get(i) && e[1] == path.get(i + 1)) best = Math.min(best, e[2]);
            sum += best;
        }
        return sum;
    }

    private static long[] bellmanFord(int n, List<int[]> edges, int source) {
        long[] d = new long[n];
        Arrays.fill(d, Long.MAX_VALUE);
        d[source] = 0;
        for (int it = 0; it < n; it++)
            for (int[] e : edges)
                if (d[e[0]] != Long.MAX_VALUE && d[e[0]] + e[2] < d[e[1]]) d[e[1]] = d[e[0]] + e[2];
        return d;
    }
}

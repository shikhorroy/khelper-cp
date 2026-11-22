package utils.algo.graph.shortestpath;

import org.junit.jupiter.api.Test;
import utils.algo.graph.AdjacencyMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FloydWarshallTest {

    @Test
    void sumOfTwoLargeFiniteEdgesDoesNotOverflow() {
        AdjacencyMatrix m = new AdjacencyMatrix(3, AdjacencyMatrix.Type.UNIDIRECTIONAL);
        m.addEdge(0, 1, 2_000_000_000);
        m.addEdge(1, 2, 2_000_000_000);
        FloydWarshall.run(m);
        assertEquals(4_000_000_000L, FloydWarshall.getMinCosts(0, 2));
        assertEquals(List.of(0, 1, 2), FloydWarshall.getPath(0, 2));
    }

    @Test
    void inputMatrixIsNotChanged() {
        AdjacencyMatrix m = new AdjacencyMatrix(3);
        m.addEdge(0, 1, 1);
        m.addEdge(1, 2, 1);
        FloydWarshall.run(m);
        assertEquals(Integer.MAX_VALUE, m.matrix[0][2]);
        assertEquals(2, FloydWarshall.getMinCosts(0, 2));
    }

    @Test
    void unreachablePairHasInfAndNullPath() {
        FloydWarshall.run(new AdjacencyMatrix(2));
        assertEquals(FloydWarshall.INF, FloydWarshall.getMinCosts(0, 1));
        assertNull(FloydWarshall.getPath(0, 1));
        assertEquals(List.of(1), FloydWarshall.getPath(1, 1));
    }

    @Test
    void negativeCycleIsDetectedAndGetPathTerminates() {
        AdjacencyMatrix m = new AdjacencyMatrix(3, AdjacencyMatrix.Type.UNIDIRECTIONAL);
        m.addEdge(0, 1, 1);
        m.addEdge(1, 0, -5);
        m.addEdge(1, 2, 1);
        FloydWarshall.run(m);
        assertTrue(FloydWarshall.onNegativeCycle(0));
        assertFalse(FloydWarshall.onNegativeCycle(2));
        assertTimeoutPreemptively(java.time.Duration.ofSeconds(1), () -> FloydWarshall.getPath(0, 2));
    }

    @Test
    void randomGraphsMatchBellmanFord() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            int n = 1 + random.nextInt(8);
            boolean bidirectional = random.nextBoolean();
            //~ Negative weights only on a DAG (edges go low to high index), so no negative cycle.
            boolean negative = !bidirectional && random.nextBoolean();
            AdjacencyMatrix m = bidirectional ? new AdjacencyMatrix(n)
                    : new AdjacencyMatrix(n, AdjacencyMatrix.Type.UNIDIRECTIONAL);
            List<int[]> edges = new ArrayList<>();
            int count = random.nextInt(20);
            for (int i = 0; i < count; i++) {
                int u = random.nextInt(n), v = random.nextInt(n);
                if (negative && u >= v) continue;
                int w = negative ? random.nextInt(21) - 10
                        : random.nextBoolean() ? random.nextInt(Integer.MAX_VALUE - 1) : random.nextInt(10);
                m.addEdge(u, v, w);
                edges.add(new int[]{u, v, w});
                if (bidirectional) edges.add(new int[]{v, u, w});
            }
            FloydWarshall.run(m);
            for (int s = 0; s < n; s++) {
                long[] expected = bellmanFord(n, edges, s);
                for (int v = 0; v < n; v++) {
                    assertEquals(expected[v], FloydWarshall.getMinCosts(s, v), "case " + t);
                    List<Integer> path = FloydWarshall.getPath(s, v);
                    if (expected[v] == FloydWarshall.INF) {
                        assertNull(path);
                        continue;
                    }
                    long sum = 0;
                    for (int i = 0; i + 1 < path.size(); i++) sum += m.matrix[path.get(i)][path.get(i + 1)];
                    assertEquals(expected[v], sum);
                }
            }
        }
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

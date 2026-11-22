package utils.algo.graph.shortestpath;

import utils.algo.graph.AdjacencyMatrix;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Floyd-Warshall - shortest paths between all pairs of nodes. Negative edges are allowed.
 * Works on a {@code long} copy of the input, so the input matrix is not changed and sums do not overflow.
 * Complexity: O(V^3) time, O(V^2) memory.
 */
public class FloydWarshall {

    //~ Distance between two nodes with no path; same value AdjacencyMatrix uses for "no edge".
    public static final long INF = Long.MAX_VALUE;

    public static int nodes;
    //~ next[i][j] = node after i on the shortest path from i to j, or -1 if j is unreachable from i
    public static int[][] next;
    //~ matrix[i][j] = shortest distance from i to j, or INF
    public static long[][] matrix;

    public static void run(AdjacencyMatrix adjacencyMatrix) {
        nodes = adjacencyMatrix.nodes;
        matrix = new long[nodes][nodes];
        next = new int[nodes][nodes];
        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {
                int cost = adjacencyMatrix.matrix[i][j];
                boolean noEdge = cost == Integer.MAX_VALUE;
                matrix[i][j] = noEdge ? INF : cost;
                next[i][j] = noEdge ? -1 : j;
            }
        }

        for (int k = 0; k < nodes; k++) {
            long[] rowK = matrix[k];
            for (int i = 0; i < nodes; i++) {
                long ik = matrix[i][k];
                if (ik == INF) continue;
                long[] rowI = matrix[i];
                int[] nextI = next[i];
                for (int j = 0; j < nodes; j++) {
                    if (rowK[j] == INF) continue;
                    long candidate = ik + rowK[j];
                    if (candidate < rowI[j]) {
                        rowI[j] = candidate;
                        nextI[j] = nextI[k];
                    }
                }
            }
        }
    }

    /** True if node {@code v} lies on a negative cycle (only valid after {@link #run}). */
    public static boolean onNegativeCycle(int v) {
        return matrix[v][v] < 0;
    }

    /** Shortest distance from source to destination, or {@link #INF} if there is no path. */
    public static long getMinCosts(int source, int destination) {
        if (source >= nodes || destination >= nodes)
            throw new InvalidParameterException(source + " and " + destination + " should be < " + nodes);

        return matrix[source][destination];
    }

    /**
     * Nodes on the shortest path from source to destination.
     *
     * @return the path, or null if there is no path or the path is not well defined (negative cycle).
     */
    public static List<Integer> getPath(int source, int destination) {
        if (next[source][destination] == -1) return null;

        List<Integer> path = new ArrayList<>();
        path.add(source);
        while (source != destination) {
            source = next[source][destination];
            path.add(source);
            // A simple path has at most `nodes` nodes; a longer walk means we loop on a negative cycle.
            if (path.size() > nodes) return null;
        }
        return path;
    }
}

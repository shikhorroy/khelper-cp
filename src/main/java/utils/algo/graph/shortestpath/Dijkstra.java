package utils.algo.graph.shortestpath;

import utils.algo.graph.WeightedGraph;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Dijkstra - shortest paths from one source in a graph with non-negative edge weights.
 * Distances are {@code long}, so long paths of large int weights do not overflow.
 * Complexity: O((V + E) log V).
 */
public class Dijkstra extends FullPath {

    //~ Distance of a node that cannot be reached from the source.
    public static final long INF = Long.MAX_VALUE;

    //~ distance[v] = shortest distance from source to v, or INF if v is unreachable
    public static long[] distance;
    //~ parent[v] = previous node on the shortest path, -1 for the source and unreachable nodes
    public static int[] parent;
    public static boolean[] visited;

    public static void run(WeightedGraph graph, int source) {
        int n = graph.getNodesCount();
        distance = new long[n];
        parent = new int[n];
        visited = new boolean[n];
        Arrays.fill(distance, INF);
        Arrays.fill(parent, -1);

        //~ Queue entries are {distance, node}; an entry is stale when a shorter distance was found later.
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        distance[source] = 0;
        visited[source] = true;
        pq.add(new long[]{0, source});
        while (!pq.isEmpty()) {
            long[] top = pq.poll();
            int u = (int) top[1];
            if (top[0] != distance[u]) continue;
            for (WeightedGraph.Vertex edge : graph.adjacencyList.get(u)) {
                long candidate = top[0] + edge.cost;
                if (candidate < distance[edge.node]) {
                    distance[edge.node] = candidate;
                    parent[edge.node] = u;
                    visited[edge.node] = true;
                    pq.add(new long[]{candidate, edge.node});
                }
            }
        }
    }

    /** Nodes from the source to {@code destination}, or an empty list if it is unreachable. */
    public static List<Integer> getPath(int destination) {
        return buildPath(parent, visited, destination);
    }

    /** Print the path from the source to {@code destination}, or "No path!". */
    public static void printPath(int destination) {
        printPath(getPath(destination));
    }
}

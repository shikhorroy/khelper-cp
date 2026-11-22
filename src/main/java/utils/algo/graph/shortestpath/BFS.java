package utils.algo.graph.shortestpath;

import utils.algo.graph.Graph;

import java.util.Arrays;
import java.util.List;

/**
 * BFS (Breadth-First Search) - shortest path (edge count) from one source in an un-weighted graph.
 * Complexity: O(V + E).
 */
public class BFS extends FullPath {

    //~ distance[v] = number of edges on the shortest path from source to v, or -1 if v is unreachable
    public static int[] distance;
    //~ parent[v] = previous node on the shortest path, -1 for the source and unreachable nodes
    public static int[] parent;
    public static boolean[] visited;

    public static void run(Graph graph, int source) {
        int n = graph.getNodesCount();
        distance = new int[n];
        parent = new int[n];
        visited = new boolean[n];
        Arrays.fill(distance, -1);
        Arrays.fill(parent, -1);

        //~ Plain int array as the queue: each node is pushed at most once.
        int[] queue = new int[n];
        int head = 0, tail = 0;
        queue[tail++] = source;
        visited[source] = true;
        distance[source] = 0;
        while (head < tail) {
            int u = queue[head++];
            for (int v : graph.adjacencyList.get(u)) {
                if (visited[v]) continue;
                visited[v] = true;
                parent[v] = u;
                distance[v] = distance[u] + 1;
                queue[tail++] = v;
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

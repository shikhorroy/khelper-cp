package utils.algo.graph.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shared path helpers for single-source shortest path algorithms (BFS, Dijkstra).
 * Each algorithm keeps its own {@code parent} / {@code visited} arrays, so running one does not
 * overwrite the result of another.
 */
public abstract class FullPath {

    /**
     * Rebuild the path from the source to {@code destination} by following parent links.
     * Complexity: O(path length).
     *
     * @return nodes from source to destination, or an empty list if destination was not reached.
     */
    static List<Integer> buildPath(int[] parent, boolean[] visited, int destination) {
        List<Integer> path = new ArrayList<>();
        if (!visited[destination]) return path;
        for (int v = destination; v != -1; v = parent[v]) path.add(v);
        Collections.reverse(path);
        return path;
    }

    static void printPath(List<Integer> path) {
        if (path.isEmpty()) {
            System.out.println("No path!");
            return;
        }
        StringBuilder sb = new StringBuilder("Path: ");
        for (int i = 0; i < path.size(); i++) {
            if (i != 0) sb.append(" -> ");
            sb.append(path.get(i));
        }
        System.out.println(sb);
    }
}

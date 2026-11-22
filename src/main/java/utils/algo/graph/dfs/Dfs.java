package utils.algo.graph.dfs;

import utils.algo.graph.Graph;

import java.util.List;

/**
 * Depth-First Search from one source. Iterative (explicit stack), so a path of 2e5 nodes does not
 * overflow the call stack. Visit order is the same as the classic recursive DFS.
 * Complexity: O(V + E).
 */
public class Dfs {

    public static boolean[] visited;
    //~ order[0 .. orderSize - 1] = nodes in the order DFS first visits them (preorder)
    public static int[] order;
    public static int orderSize;

    public static void run(Graph graph, int source) {
        int n = graph.getNodesCount();
        visited = new boolean[n];
        order = new int[n];
        orderSize = 0;

        //~ stack holds the current DFS path; nextEdge[u] is the index of the next neighbour of u to try
        int[] stack = new int[n];
        int[] nextEdge = new int[n];
        int top = 0;
        stack[top++] = source;
        visited[source] = true;
        order[orderSize++] = source;
        while (top > 0) {
            int u = stack[top - 1];
            List<Integer> neighbours = graph.adjacencyList.get(u);
            if (nextEdge[u] == neighbours.size()) {
                top--;
                continue;
            }
            int v = neighbours.get(nextEdge[u]++);
            if (visited[v]) continue;
            visited[v] = true;
            order[orderSize++] = v;
            stack[top++] = v;
        }
    }

    /** Print the visit order, space separated, on one line. */
    public static void printOrder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orderSize; i++) sb.append(order[i]).append(' ');
        System.out.println(sb);
    }
}

package utils.algo.graph.tree;

/**
 * Lowest Common Ancestor (LCA) with binary lifting.<br><br>
 * Works when tree edges are added one way (parent to child) or both ways. Traversal is iterative,
 * so deep trees (a path of 2e5 nodes) do not overflow the stack. Nodes not reachable from the root
 * are treated as their own roots at level 0.<br><br>
 * <b>Complexity:</b><br>
 * Preprocessing: O(n log n)<br>
 * Query: O(log n)<br>
 * Source: <a href="http://www.shafaetsplanet.com/?p=1831"> Shafaet's Planet</a>
 */
public class LCA {
    private static final int SENTINEL = -1;

    //~ Number of table rows: 2^log > n - 1, so any depth difference fits in log bits.
    private final int log;

    private final Tree tree;
    private final int[] level;

    //~ up[p][node] = the 2^p-th ancestor of node. The root points to itself,
    //~ so jumps past the root stay at the root.
    private final int[][] up;

    public LCA(Tree tree) {
        this.tree = tree;

        int nodes = this.tree.getNodesCount();
        int lg = 1;
        while ((1 << lg) < nodes) lg++;
        this.log = lg;

        level = new int[nodes];
        up = new int[log][nodes];
        if (nodes == 0) return;
        buildParents(nodes);
        for (int p = 1; p < log; p++) {
            int[] prev = up[p - 1], cur = up[p];
            for (int v = 0; v < nodes; v++) cur[v] = prev[prev[v]];
        }
    }

    /** BFS from the root to fill level[] and up[0][] (the direct parent). */
    private void buildParents(int nodes) {
        int[] parent = up[0];
        for (int v = 0; v < nodes; v++) parent[v] = v;
        boolean[] visited = new boolean[nodes];
        int[] queue = new int[nodes];
        int head = 0, tail = 0;
        queue[tail++] = tree.root;
        visited[tree.root] = true;
        while (head < tail) {
            int u = queue[head++];
            for (int child : tree.childrenOf(u)) {
                if (visited[child]) continue;
                visited[child] = true;
                parent[child] = u;
                level[child] = level[u] + 1;
                queue[tail++] = child;
            }
        }
    }

    public int levelOf(int node) {
        if (node < 0 || node >= tree.getNodesCount()) throw new AssertionError("node " + node + " - is out of bound");
        return level[node];
    }

    /** Lowest common ancestor of node1 and node2. O(log n). */
    public int findLCA(int node1, int node2) {
        if (level[node1] < level[node2]) {
            int tmp = node1;
            node1 = node2;
            node2 = tmp;
        }

        node1 = lift(node1, level[node1] - level[node2]);
        if (node1 == node2) return node1;

        for (int p = log - 1; p >= 0; p--) {
            if (up[p][node1] != up[p][node2]) {
                node1 = up[p][node1];
                node2 = up[p][node2];
            }
        }
        return up[0][node1];
    }

    /** The k-th ancestor of node (k = 0 gives node itself), or -1 if k is larger than its level. */
    public int kthAncestor(int node, int k) {
        if (k < 0 || k > level[node]) return SENTINEL;
        return lift(node, k);
    }

    private int lift(int node, int k) {
        for (int p = 0; k > 0; p++, k >>= 1) if ((k & 1) != 0) node = up[p][node];
        return node;
    }
}

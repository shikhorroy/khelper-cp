package utils.algo.ds.dsu;

/**
 * Disjoint set union (union-find) on nodes {@code 0..nodes-1}.
 * Uses union by size and path halving, so each operation is amortized O(alpha(n)), almost O(1).
 * {@code find} is iterative, so a long chain cannot overflow the stack.
 */
public class DisjointSetUnion {

    //~ parent[v] is the parent of v; a root points to itself
    private final int[] parent;
    //~ size[r] is the number of nodes in the set whose root is r (only valid for roots)
    private final int[] size;
    //~ number of disjoint sets right now
    private int components;

    public DisjointSetUnion(int nodes) {
        parent = new int[nodes];
        size = new int[nodes];
        for (int i = 0; i < nodes; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        components = nodes;
    }

    /** Root of the set that holds {@code node}. Amortized O(alpha(n)). */
    public int find(int node) {
        while (parent[node] != node) {
            parent[node] = parent[parent[node]];
            node = parent[node];
        }
        return node;
    }

    /**
     * Merge the sets of the two nodes. The smaller set goes under the larger one.
     *
     * @return true if the nodes were in different sets and got merged, false if they were already together
     */
    public boolean union(int firstNode, int secondNode) {
        int a = find(firstNode);
        int b = find(secondNode);
        if (a == b) return false;

        if (size[a] < size[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        parent[b] = a;
        size[a] += size[b];
        components--;
        return true;
    }

    /** True if both nodes are in the same set. */
    public boolean sameParent(int firstNode, int secondNode) {
        return find(firstNode) == find(secondNode);
    }

    /** Number of nodes in the set that holds {@code node}. */
    public int size(int node) {
        return size[find(node)];
    }

    /** Number of disjoint sets. */
    public int components() {
        return components;
    }
}

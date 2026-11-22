package utils.algo.graph;

import java.security.InvalidParameterException;

/**
 * Graph stored as a V x V cost matrix. {@code matrix[i][j] == Integer.MAX_VALUE} means "no edge",
 * and {@code matrix[i][i] == 0}. Memory: O(V^2).
 */
public class AdjacencyMatrix {

    public enum Type {
        UNIDIRECTIONAL, BIDIRECTIONAL
    }

    public int nodes;
    protected Type type;
    public int[][] matrix;

    private void init(int nodes) {
        this.nodes = nodes;
        this.matrix = new int[nodes][nodes];
        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {
                if (i != j) this.matrix[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    public AdjacencyMatrix(int nodes) {
        this.init(nodes);
        this.type = Type.BIDIRECTIONAL;
    }

    public AdjacencyMatrix(int nodes, Type type) {
        this.init(nodes);
        this.type = type;
    }

    /**
     * Add an edge. With parallel edges the cheapest one is kept. A self-loop only changes
     * {@code matrix[i][i]} when its cost is negative.
     */
    public void addEdge(int source, int destination, int cost) {
        if (source < 0 || destination < 0 || source >= nodes || destination >= nodes)
            throw new InvalidParameterException(source + " and " + destination + " should be in [0, " + nodes + ")");
        this.matrix[source][destination] = Math.min(this.matrix[source][destination], cost);
        if (this.type == Type.BIDIRECTIONAL)
            this.matrix[destination][source] = Math.min(this.matrix[destination][source], cost);
    }
}

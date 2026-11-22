package utils.algo.graph;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Weighted graph as an adjacency list of {@link Vertex} (target node + edge cost). Nodes are 0 .. n - 1.
 * Note: {@code adjacencyList} here hides {@link Graph#adjacencyList}; the parent list is kept empty per node
 * and is only used for {@link #getNodesCount()}.
 */
public class WeightedGraph extends Graph {

    public static class Vertex implements Comparable<Vertex> {
        public int node;
        public int cost;

        private Vertex(int node, int cost) {
            this.node = node;
            this.cost = cost;
        }

        public static Vertex of(int node, int cost) {
            return new Vertex(node, cost);
        }

        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(this.cost, o.cost);
        }
    }

    public List<List<Vertex>> adjacencyList;

    private void init(int nodes) {
        this.adjacencyList = new ArrayList<>();
        for (int node = 0; node < nodes; ++node) this.adjacencyList.add(new ArrayList<>());
    }

    public WeightedGraph(int nodes) {
        super(nodes);
        init(nodes);
    }

    public WeightedGraph(int nodes, Type type) {
        super(nodes, type);
        init(nodes);
    }

    /** Add an edge with the given cost; for BIDIRECTIONAL graphs the reverse edge is added too. */
    public void addEdge(int source, int destination, int cost) {
        int nodeCount = this.getNodesCount();
        if (source < 0 || destination < 0 || source >= nodeCount || destination >= nodeCount)
            throw new InvalidParameterException(source + ", " + destination + " should be in [0, " + nodeCount + ")");
        this.adjacencyList.get(source).add(Vertex.of(destination, cost));
        if (this.type.equals(Type.BIDIRECTIONAL)) this.adjacencyList.get(destination).add(Vertex.of(source, cost));
    }
}

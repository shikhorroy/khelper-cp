package utils.algo.graph.tree;

import utils.algo.graph.Graph;

/**
 * Rooted tree on nodes 0 .. n - 1. Edges go parent to child by default (UNIDIRECTIONAL);
 * call {@code setType(Type.BIDIRECTIONAL)} before adding edges to store both directions.
 */
public class Tree extends Graph {

    public int root = -1;

    public Tree(int nodes, int root) {
        super(nodes, Type.UNIDIRECTIONAL);
        this.root = root;
    }
}

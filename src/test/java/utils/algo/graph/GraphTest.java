package utils.algo.graph;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void bidirectionalEdgeIsStoredBothWays() {
        Graph g = new Graph(3);
        g.addEdge(0, 2);
        assertEquals(List.of(2), g.childrenOf(0));
        assertEquals(List.of(0), g.childrenOf(2));
        assertEquals(3, g.getNodesCount());
    }

    @Test
    void unidirectionalEdgeIsStoredOneWay() {
        Graph g = new Graph(3, Graph.Type.UNIDIRECTIONAL);
        g.addEdge(0, 2);
        assertEquals(List.of(2), g.childrenOf(0));
        assertTrue(g.childrenOf(2).isEmpty());
    }

    @Test
    void childrenOfOutOfRangeNodeThrows() {
        assertThrows(AssertionError.class, () -> new Graph(2).childrenOf(2));
    }
}

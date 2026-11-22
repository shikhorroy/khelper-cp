package utils.algo.graph;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class WeightedGraphTest {

    @Test
    void bidirectionalEdgeKeepsCostBothWays() {
        WeightedGraph g = new WeightedGraph(2);
        g.addEdge(0, 1, 7);
        assertEquals(1, g.adjacencyList.get(0).get(0).node);
        assertEquals(7, g.adjacencyList.get(0).get(0).cost);
        assertEquals(0, g.adjacencyList.get(1).get(0).node);
        assertEquals(2, g.getNodesCount());
    }

    @Test
    void unidirectionalEdgeIsStoredOneWay() {
        WeightedGraph g = new WeightedGraph(2, Graph.Type.UNIDIRECTIONAL);
        g.addEdge(0, 1, 7);
        assertTrue(g.adjacencyList.get(1).isEmpty());
    }

    @Test
    void outOfRangeSourceOrDestinationThrows() {
        WeightedGraph g = new WeightedGraph(2);
        assertThrows(InvalidParameterException.class, () -> g.addEdge(2, 0, 1));
        assertThrows(InvalidParameterException.class, () -> g.addEdge(0, 2, 1));
        assertThrows(InvalidParameterException.class, () -> g.addEdge(-1, 0, 1));
    }
}

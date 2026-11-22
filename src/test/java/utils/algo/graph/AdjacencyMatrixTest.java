package utils.algo.graph;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyMatrixTest {

    @Test
    void newMatrixHasZeroDiagonalAndNoEdges() {
        AdjacencyMatrix m = new AdjacencyMatrix(3);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                assertEquals(i == j ? 0 : Integer.MAX_VALUE, m.matrix[i][j]);
    }

    @Test
    void parallelEdgeKeepsCheapestCost() {
        AdjacencyMatrix m = new AdjacencyMatrix(2);
        m.addEdge(0, 1, 1);
        m.addEdge(0, 1, 5);
        assertEquals(1, m.matrix[0][1]);
        assertEquals(1, m.matrix[1][0]);
    }

    @Test
    void positiveSelfLoopKeepsDiagonalZero() {
        AdjacencyMatrix m = new AdjacencyMatrix(2);
        m.addEdge(0, 0, 3);
        assertEquals(0, m.matrix[0][0]);
    }

    @Test
    void negativeSelfLoopIsStored() {
        AdjacencyMatrix m = new AdjacencyMatrix(2);
        m.addEdge(1, 1, -2);
        assertEquals(-2, m.matrix[1][1]);
    }

    @Test
    void unidirectionalEdgeIsStoredOneWay() {
        AdjacencyMatrix m = new AdjacencyMatrix(2, AdjacencyMatrix.Type.UNIDIRECTIONAL);
        m.addEdge(0, 1, 4);
        assertEquals(4, m.matrix[0][1]);
        assertEquals(Integer.MAX_VALUE, m.matrix[1][0]);
    }

    @Test
    void outOfRangeNodeThrows() {
        AdjacencyMatrix m = new AdjacencyMatrix(2);
        assertThrows(InvalidParameterException.class, () -> m.addEdge(2, 0, 1));
        assertThrows(InvalidParameterException.class, () -> m.addEdge(0, -1, 1));
    }
}

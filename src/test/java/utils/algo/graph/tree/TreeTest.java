package utils.algo.graph.tree;

import org.junit.jupiter.api.Test;
import utils.algo.graph.Graph;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreeTest {

    @Test
    void edgesGoParentToChildByDefault() {
        Tree t = new Tree(3, 0);
        t.addEdge(0, 1);
        assertEquals(0, t.root);
        assertEquals(List.of(1), t.childrenOf(0));
        assertTrue(t.childrenOf(1).isEmpty());
    }

    @Test
    void bidirectionalTypeStoresBothDirections() {
        Tree t = new Tree(3, 0);
        t.setType(Graph.Type.BIDIRECTIONAL);
        t.addEdge(0, 1);
        assertEquals(List.of(0), t.childrenOf(1));
    }
}

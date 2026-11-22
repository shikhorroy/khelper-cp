package utils.algo.graph.shortestpath;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FullPathTest {

    @Test
    void buildPathFollowsParentsFromSource() {
        int[] parent = {-1, 0, 1, -1};
        boolean[] visited = {true, true, true, false};
        assertEquals(List.of(0, 1, 2), FullPath.buildPath(parent, visited, 2));
        assertEquals(List.of(0), FullPath.buildPath(parent, visited, 0));
    }

    @Test
    void buildPathIsEmptyForUnreachedNode() {
        int[] parent = {-1, 0, 1, -1};
        boolean[] visited = {true, true, true, false};
        assertTrue(FullPath.buildPath(parent, visited, 3).isEmpty());
    }
}

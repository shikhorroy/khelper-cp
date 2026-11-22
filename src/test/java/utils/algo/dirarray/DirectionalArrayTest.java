package utils.algo.dirarray;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DirectionalArrayTest {

    @Test
    void sizesPerDirection() {
        assertEquals(4, DirectionalArray.getMoves(Direction.FOUR).size());
        assertEquals(8, DirectionalArray.getMoves(Direction.EIGHT).size());
        assertEquals(8, DirectionalArray.getMoves(Direction.KNIGHT).size());
    }

    @Test
    void changingReturnedMoveDoesNotLeakIntoLaterCalls() {
        List<Move> m = DirectionalArray.getMoves(Direction.FOUR);
        m.get(0).dx = 99;
        assertEquals(1, DirectionalArray.getMoves(Direction.FOUR).get(0).dx);
    }

    @Test
    void changingReturnedArrayDoesNotLeakIntoLaterCalls() {
        int[] dx = DirectionalArray.dx(Direction.EIGHT);
        dx[0] = 99;
        assertNotEquals(99, DirectionalArray.dx(Direction.EIGHT)[0]);
    }

    @Test
    void movesAreDistinctAndMatchDxDy() {
        for (Direction d : Direction.values()) {
            int[] dx = DirectionalArray.dx(d), dy = DirectionalArray.dy(d);
            List<Move> moves = DirectionalArray.getMoves(d);
            Set<String> seen = new HashSet<>();
            for (int k = 0; k < dx.length; k++) {
                assertEquals(dx[k], moves.get(k).dx);
                assertEquals(dy[k], moves.get(k).dy);
                seen.add(moves.get(k).toString());
            }
            assertEquals(dx.length, seen.size(), d + " has duplicate moves");
        }
    }

    @Test
    void movesHaveExpectedShape() {
        for (Move m : DirectionalArray.getMoves(Direction.FOUR)) assertEquals(1, Math.abs(m.dx) + Math.abs(m.dy));
        for (Move m : DirectionalArray.getMoves(Direction.EIGHT)) {
            assertTrue(Math.max(Math.abs(m.dx), Math.abs(m.dy)) == 1);
        }
        for (Move m : DirectionalArray.getMoves(Direction.KNIGHT)) assertEquals(5, m.dx * m.dx + m.dy * m.dy);
    }
}

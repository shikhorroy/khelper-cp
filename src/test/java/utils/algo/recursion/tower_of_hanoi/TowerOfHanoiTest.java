package utils.algo.recursion.tower_of_hanoi;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TowerOfHanoiTest {

    private static String[] solve(int plates) {
        PrintStream old = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            new TowerOfHanoi(plates, "A", "C", "B").solve();
        } finally {
            System.setOut(old);
        }
        String out = buffer.toString().replace("\r", "");
        return out.isEmpty() ? new String[0] : out.split("\n");
    }

    @Test
    void zeroPlatesPrintsNothingAndDoesNotRecurseForever() {
        assertEquals(0, solve(0).length);
        assertEquals(0, solve(-2).length);
    }

    @Test
    void threePlatesPrintsSevenMovesInOrder() {
        String[] lines = solve(3);
        assertArrayEquals(new String[]{
                "Move plate - 1 from A to C",
                "Move plate - 2 from A to B",
                "Move plate - 1 from C to B",
                "Move plate - 3 from A to C",
                "Move plate - 1 from B to A",
                "Move plate - 2 from B to C",
                "Move plate - 1 from A to C",
        }, lines);
    }

    @Test
    void movesAreLegalAndEndOnDestination() {
        int n = 10;
        String[] lines = solve(n);
        assertEquals((1 << n) - 1, lines.length);
        java.util.Map<String, java.util.ArrayDeque<Integer>> towers = new java.util.HashMap<>();
        for (String t : new String[]{"A", "B", "C"}) towers.put(t, new java.util.ArrayDeque<>());
        for (int p = n; p >= 1; p--) towers.get("A").push(p);
        for (String line : lines) {
            String[] w = line.split(" ");
            int plate = Integer.parseInt(w[3]);
            java.util.ArrayDeque<Integer> from = towers.get(w[5]), to = towers.get(w[7]);
            assertEquals(plate, from.pop());
            assertTrue(to.isEmpty() || to.peek() > plate, "bigger plate on smaller: " + line);
            to.push(plate);
        }
        assertEquals(n, towers.get("C").size());
    }

    @Test
    void moveCountIsTwoToTheNMinusOne() {
        assertEquals(1023, new TowerOfHanoi(10, "A", "C", "B").moveCount());
        assertEquals(0, new TowerOfHanoi(0, "A", "C", "B").moveCount());
    }
}

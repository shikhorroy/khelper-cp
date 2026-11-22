package utils.algo.recursion.tower_of_hanoi;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Algorithm: IBH (Induction Base Hypothesis) recursion to solve Tower of Hanoi.
 * Prints 2^n - 1 moves. Time: O(2^n). Recursion depth: n.
 */
public class TowerOfHanoi {

    private final int platesCount;
    private final String sourceTower;
    private final String helperTower;
    private final String destinationTower;

    public TowerOfHanoi(int platesCount, String sourceTower, String destinationTower, String helperTower) {
        this.platesCount = platesCount;
        this.sourceTower = sourceTower;
        this.helperTower = helperTower;
        this.destinationTower = destinationTower;
    }

    private void printSolution(PrintWriter out, int platesCount, String sourceTower, String destinationTower,
                               String helperTower) {
        //~ Base:
        if (platesCount <= 0) return;

        //~ Hypothesis:
        printSolution(out, platesCount - 1, sourceTower, helperTower, destinationTower);
        //~ Induction:
        out.println("Move plate - " + platesCount + " from " + sourceTower + " to " + destinationTower);
        //~ Hypothesis:
        printSolution(out, platesCount - 1, helperTower, destinationTower, sourceTower);
    }

    /**
     * Prints every move to System.out, one per line, through a buffered writer. Prints nothing when the plate count
     * is 0 or negative.
     */
    public void solve() {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out), 1 << 16));
        this.printSolution(out, this.platesCount, this.sourceTower, this.destinationTower, this.helperTower);
        out.flush();
    }

    /**
     * @return the number of moves, 2^n - 1 (0 for n {@code <= 0}). Valid for n {@code <= 63}.
     */
    public long moveCount() {
        return platesCount <= 0 ? 0 : (1L << platesCount) - 1;
    }
}

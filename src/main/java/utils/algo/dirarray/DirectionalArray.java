package utils.algo.dirarray;

import java.util.ArrayList;
import java.util.List;

/**
 * Grid move offsets for 4 directions, 8 directions and knight moves.
 */
public class DirectionalArray {
    //~ Index by Direction.ordinal(): {dx[], dy[]}. Never handed out directly, so callers cannot change them.
    private static final int[][][] DIRECTIONS = new int[Direction.values().length][][];

    static {
        DIRECTIONS[Direction.FOUR.ordinal()] = new int[][]{{1, 0, -1, 0}, {0, 1, 0, -1}};
        DIRECTIONS[Direction.EIGHT.ordinal()] = new int[][]{{1, 1, 0, -1, -1, -1, 0, 1}, {0, 1, 1, 1, 0, -1, -1, -1}};
        DIRECTIONS[Direction.KNIGHT.ordinal()] =
                new int[][]{{2, 1, -1, -2, -2, -1, 1, 2}, {1, 2, 2, 1, -1, -2, -2, -1}};
    }

    /**
     * Give the list of possible ways to move. Each call returns a new list of new Move objects, so changing them
     * does not affect later calls.
     *
     * @param direction requested direction.
     * @return list of Move(dx, dy)
     */
    public static List<Move> getMoves(Direction direction) {
        int[] xDirections = DIRECTIONS[direction.ordinal()][0];
        int[] yDirections = DIRECTIONS[direction.ordinal()][1];
        List<Move> moves = new ArrayList<>(xDirections.length);

        for (int i = 0, ln = xDirections.length; i < ln; i++) {
            moves.add(Move.of(xDirections[i], yDirections[i]));
        }
        return moves;
    }

    /**
     * @return a copy of the dx offsets, for a primitive loop {@code for (int k = 0; k < dx.length; k++)}.
     */
    public static int[] dx(Direction direction) {
        return DIRECTIONS[direction.ordinal()][0].clone();
    }

    /**
     * @return a copy of the dy offsets, in the same order as {@link #dx(Direction)}.
     */
    public static int[] dy(Direction direction) {
        return DIRECTIONS[direction.ordinal()][1].clone();
    }
}

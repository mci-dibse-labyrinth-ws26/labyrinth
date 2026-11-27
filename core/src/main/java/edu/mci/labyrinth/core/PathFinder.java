package edu.mci.labyrinth.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Finds the fields a figure can reach from its position (Pflichtenheft 6.3, A-33).
 *
 * <p>Two neighbouring fields are connected if both tiles are open towards each other. The
 * board border is closed. The search is a breadth-first search that visits neighbours in the
 * order {@code UP, RIGHT, DOWN, LEFT}, so the result is the same on every run. Figures do not
 * block the search: passing occupied fields is allowed (A-12); whether a figure may stop on a
 * field is checked by {@link RuleValidator}.
 */
public final class PathFinder {

    private static final Direction[] DIRECTIONS = Direction.values();

    private PathFinder() {
    }

    /**
     * Returns every reachable field with the length of the shortest path to it.
     *
     * <p>The length counts as the steps the figure takes (A-11). The start field is always
     * contained with distance 0, so a figure enclosed on its field reaches only itself.
     *
     * @param board the board to search
     * @param start the field the figure stands on
     * @return unmodifiable map from each reachable field to its distance, in the order the
     *     fields were found
     * @throws IllegalArgumentException if the start lies outside the board
     */
    public static Map<Position, Integer> distancesFrom(Board board, Position start) {
        Objects.requireNonNull(board, "Board must not be null");
        Objects.requireNonNull(start, "Start must not be null");
        if (!board.contains(start)) {
            throw new IllegalArgumentException("Start " + start + " is outside the board");
        }

        int rows = board.rows();
        int cols = board.cols();
        int[] openings = openingMasks(board);
        int[] distance = new int[rows * cols];
        Arrays.fill(distance, -1);
        int[] queue = new int[rows * cols];

        int head = 0;
        int tail = 0;
        int startIndex = start.row() * cols + start.column();
        queue[tail++] = startIndex;
        distance[startIndex] = 0;

        Map<Position, Integer> reachable = new LinkedHashMap<>();
        while (head < tail) {
            int current = queue[head++];
            Position position = new Position(current / cols, current % cols);
            reachable.put(position, distance[current]);

            for (Direction direction : DIRECTIONS) {
                if ((openings[current] & bit(direction)) == 0) {
                    continue;
                }
                Position next = position.neighbor(direction);
                if (!board.contains(next)) {
                    continue;
                }
                int nextIndex = next.row() * cols + next.column();
                if (distance[nextIndex] < 0
                        && (openings[nextIndex] & bit(direction.opposite())) != 0) {
                    distance[nextIndex] = distance[current] + 1;
                    queue[tail++] = nextIndex;
                }
            }
        }

        return Collections.unmodifiableMap(reachable);
    }

    /**
     * Returns the reachable fields in the order the search found them (F-216).
     *
     * @param board the board to search
     * @param start the field the figure stands on
     * @return unmodifiable list of reachable fields, starting with {@code start}
     * @throws IllegalArgumentException if the start lies outside the board
     */
    public static List<Position> reachablePositions(Board board, Position start) {
        return List.copyOf(distancesFrom(board, start).keySet());
    }

    /**
     * Reads the openings of every tile once, so the search does not rebuild sets per step.
     */
    private static int[] openingMasks(Board board) {
        int cols = board.cols();
        int[] masks = new int[board.rows() * cols];
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < cols; col++) {
                int mask = 0;
                for (Direction direction : board.tileAt(new Position(row, col)).openings()) {
                    mask |= bit(direction);
                }
                masks[row * cols + col] = mask;
            }
        }
        return masks;
    }

    private static int bit(Direction direction) {
        return 1 << direction.ordinal();
    }
}

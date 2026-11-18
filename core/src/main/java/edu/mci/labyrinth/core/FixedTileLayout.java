package edu.mci.labyrinth.core;

import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Positions and orientations of the fixed tiles (Pflichtenheft 6.1, A-02, A-32).
 *
 * <p>Fixed tiles lie on every field whose row and column are both even. The four corners are
 * corners opening towards the board interior, the other border fields are T-junctions closed
 * towards the border, and the inner fixed fields are T-junctions with a random rotation.
 */
public final class FixedTileLayout {

    private static final int BOTTOM_LEFT_CORNER = 0;
    private static final int TOP_LEFT_CORNER = 1;
    private static final int TOP_RIGHT_CORNER = 2;
    private static final int BOTTOM_RIGHT_CORNER = 3;

    private static final int BOTTOM_EDGE = 0;
    private static final int LEFT_EDGE = 1;
    private static final int TOP_EDGE = 2;
    private static final int RIGHT_EDGE = 3;

    private static final int ROTATIONS = 4;

    private FixedTileLayout() {
    }

    /**
     * Checks whether a fixed tile lies on the given position.
     *
     * @param position the position to check
     * @return {@code true} if both row and column are even
     */
    public static boolean isFixedPosition(Position position) {
        Objects.requireNonNull(position, "Position must not be null");
        return position.row() % 2 == 0 && position.column() % 2 == 0;
    }

    /**
     * Creates the fixed tile for a fixed position of a board.
     *
     * <p>Only inner fixed tiles draw a rotation from {@code random}; corners and border tiles do
     * not consume random values. Callers that iterate the board in a fixed order therefore get
     * the same tiles for the same seed (NF-11).
     *
     * @param position a fixed position inside the board
     * @param rows number of rows of the board
     * @param cols number of columns of the board
     * @param random source for the rotation of inner fixed tiles
     * @return a fixed tile without treasure and bonus
     * @throws IllegalArgumentException if the position is outside the board or not fixed
     */
    public static Tile fixedTileAt(Position position, int rows, int cols, RandomGenerator random) {
        Objects.requireNonNull(position, "Position must not be null");
        Objects.requireNonNull(random, "Random must not be null");

        int row = position.row();
        int col = position.column();

        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IllegalArgumentException("Position " + position + " is outside the board");
        }
        if (!isFixedPosition(position)) {
            throw new IllegalArgumentException("Position " + position + " is not a fixed position");
        }

        boolean top = row == 0;
        boolean bottom = row == rows - 1;
        boolean left = col == 0;
        boolean right = col == cols - 1;

        if (top && left) {
            return fixedTile(TileShape.CORNER, TOP_LEFT_CORNER);
        }
        if (top && right) {
            return fixedTile(TileShape.CORNER, TOP_RIGHT_CORNER);
        }
        if (bottom && right) {
            return fixedTile(TileShape.CORNER, BOTTOM_RIGHT_CORNER);
        }
        if (bottom && left) {
            return fixedTile(TileShape.CORNER, BOTTOM_LEFT_CORNER);
        }
        if (top) {
            return fixedTile(TileShape.T_JUNCTION, TOP_EDGE);
        }
        if (right) {
            return fixedTile(TileShape.T_JUNCTION, RIGHT_EDGE);
        }
        if (bottom) {
            return fixedTile(TileShape.T_JUNCTION, BOTTOM_EDGE);
        }
        if (left) {
            return fixedTile(TileShape.T_JUNCTION, LEFT_EDGE);
        }
        return fixedTile(TileShape.T_JUNCTION, random.nextInt(ROTATIONS));
    }

    private static Tile fixedTile(TileShape shape, int rotation) {
        return new Tile(shape, rotation, true, null, null);
    }
}


package edu.mci.labyrinth.core;

import java.util.Objects;
import java.util.Arrays;

/**
 * Immutable representation of the labyrinth game board.
 *
 * @param rows number of rows
 * @param cols number of columns
 * @param tiles two-dimensional array of tiles
 * @param spareTile the additional tile outside the board
 */
public record Board(
    int rows,
    int cols,
    Tile[][] tiles,
    Tile spareTile
) {

    /** Default board size according to A-01. */
    public static final int DEFAULT_SIZE = 7;

    /**
     * Validates the dimensions and creates a defensive copy
     * of the tile grid.
     *
     * @throws IllegalArgumentException if the board is invalid
     */
    public Board {
        if (!validSize(rows) || !validSize(cols)) {
            throw new IllegalArgumentException(
                "Board dimensions must be odd and between 3 and 15");
        }

        Objects.requireNonNull(tiles, "Tiles must not be null");
        Objects.requireNonNull(spareTile, "Spare tile must not be null");

        if (tiles.length != rows) {
            throw new IllegalArgumentException("Invalid row count");
        }

        Tile[][] copy = new Tile[rows][cols];

        for (int row = 0; row < rows; row++) {
            if (tiles[row] == null || tiles[row].length != cols) {
                throw new IllegalArgumentException(
                    "Invalid column count at row " + row);
            }

            for (int col = 0; col < cols; col++) {
                copy[row][col] = Objects.requireNonNull(
                    tiles[row][col], "Tile must not be null");
            }
        }

        tiles = copy;
    }

    /**
     * Checks whether a board dimension is valid.
     *
     * @param size the dimension to validate
     * @return true if the dimension is valid
     */
    private static boolean validSize(int size) {
        return size >= 3 && size <= 15 && size % 2 == 1;
    }

    /**
     * Returns the tile at a specific position.
     *
     * @param position the requested position
     * @return the tile at that position
     */
    public Tile tileAt(Position position) {
        Objects.requireNonNull(position, "Position must not be null");
        return tiles[position.row()][position.column()];
    }

    /**
     * Returns whether a position is inside the board.
     *
     * @param position the position to check
     * @return true if the position is inside the board
     */
    public boolean contains(Position position) {
        Objects.requireNonNull(position, "Position must not be null");

        return position.row() < rows
            && position.column() < cols;
    }

    /**
     * Returns a defensive copy of the tile grid.
     *
     * @return a copy of the tile array
     */
    @Override
    public Tile[][] tiles() {
        Tile[][] copy = new Tile[rows][];

        for (int row = 0; row < rows; row++) {
            copy[row] = tiles[row].clone();
        }

        return copy;
    }

    /**
     * Compares boards by dimensions, tiles and spare tile.
     *
     * @param obj the object to compare
     * @return true if both boards have identical contents
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Board other)) {
            return false;
        }

        return rows == other.rows
            && cols == other.cols
            && Arrays.deepEquals(tiles, other.tiles)
            && spareTile.equals(other.spareTile);
    }

    /**
     * Computes a hash code based on the board contents.
     *
     * @return the board's hash code
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols, spareTile);
        result = 31 * result + Arrays.deepHashCode(tiles);
        return result;
    }
}

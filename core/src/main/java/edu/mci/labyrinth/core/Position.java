
package edu.mci.labyrinth.core;

/**
 * Represents a position on the labyrinth board using
 * kartesian row and column coordinates.
 *
 * @param row the row index
 * @param column the column index
 */
public record Position(int row, int column) {

    /**
     * Validates the position coordinates.
     *
     * @throws IllegalArgumentException if either coordinate is negative
     */
    public Position {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException(
                "Row and column must be non-negative");
        }
    }
}

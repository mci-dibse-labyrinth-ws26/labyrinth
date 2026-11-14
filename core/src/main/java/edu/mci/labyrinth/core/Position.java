
package edu.mci.labyrinth.core;

/**
 * Represents a position using zero-based row and column coordinates.
 * Coordinates may be outside the board boundaries.
 *
 * @param row the row coordinate
 * @param column the column coordinate
 */
public record Position(int row, int column) {

    /**
     * Returns the neighbouring position in the given direction.
     *
     * <p>The returned position may be outside the board.
     * Use Board.contains() to check board boundaries.</p>
     *
     * @param direction the direction to move
     * @return the neighbouring position
     * @throws NullPointerException if direction is null
     */
    public Position neighbor(Direction direction) {
        java.util.Objects.requireNonNull(direction, "Direction must not be null");

        return switch (direction) {
            case UP -> new Position(row - 1, column);
            case RIGHT -> new Position(row, column + 1);
            case DOWN -> new Position(row + 1, column);
            case LEFT -> new Position(row, column - 1);
        };
    }
}

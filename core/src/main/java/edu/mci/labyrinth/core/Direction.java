
package edu.mci.labyrinth.core;

/**
 * Represents the four cardinal directions on the game board.
 */
public enum Direction {
    /** Direction towards the top of the board. */
    UP,

    /** Direction towards the right of the board. */
    RIGHT,

    /** Direction towards the bottom of the board. */
    DOWN,

    /** Direction towards the left of the board. */
    LEFT;

    /**
     * Returns the direction opposite to this direction.
     *
     * @return the opposite direction
     */
    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case RIGHT -> LEFT;
            case DOWN -> UP;
            case LEFT -> RIGHT;
        };
    }

    /**
     * Returns the direction after clockwise quarter-turns.
     *
     * @param rotation number of clockwise quarter-turns
     * @return the rotated direction
     * @throws IllegalArgumentException if rotation is outside 0 to 3
     */
    public Direction rotate(int rotation) {
        if (rotation < 0 || rotation > 3) {
            throw new IllegalArgumentException(
                "Rotation must be between 0 and 3");
        }

        return values()[(ordinal() + rotation) % values().length];
    }
}

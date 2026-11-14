
package edu.mci.labyrinth.core;

import java.util.Set;

/**
 * Represents an immutable labyrinth tile.
 *
 * @param shape the geometric shape of the tile
 * @param rotation absolute clockwise rotation from 0 to 3
 * @param fixed whether the tile occupies a fixed board position
 * @param treasure the treasure on this tile, or null
 * @param bonus the bonus on this tile, or null
 */
public record Tile(
    TileShape shape,
    int rotation,
    boolean fixed,
    Treasure treasure,
    BonusType bonus
) {

    /**
     * Validates the tile properties.
     *
     * @throws IllegalArgumentException if the rotation is invalid
     */
    public Tile {
        if (shape == null) {
            throw new IllegalArgumentException("Shape must not be null");
        }

        if (rotation < 0 || rotation > 3) {
            throw new IllegalArgumentException(
                "Rotation must be between 0 and 3");
        }

        if (treasure != null && bonus != null) {
            throw new IllegalArgumentException(
                "A tile cannot contain both a treasure and a bonus");
        }
    }

    /**
     * Returns the open directions for this tile.
     *
     * @return an unmodifiable set of open directions
     */
    public Set<Direction> openings() {
        return shape.openings(rotation);
    }

    /**
     * Creates a copy of this tile with a new absolute rotation.
     *
     * @param newRotation absolute rotation from 0 to 3
     * @return a new tile with the specified rotation
     */
    public Tile withRotation(int newRotation) {
        return new Tile(shape, newRotation, fixed, treasure, bonus);
    }
}

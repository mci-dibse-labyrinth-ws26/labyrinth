
package edu.mci.labyrinth.core;

import java.util.Set;

/**
 * Defines the three tile shapes and their openings
 * at rotation zero, according to requirement A-32.
 */
public enum TileShape {

    /** Straight corridor with two opposite openings. */
    STRAIGHT(Set.of(Direction.UP, Direction.DOWN)),

    /** Corner corridor with two adjacent openings. */
    CORNER(Set.of(Direction.UP, Direction.RIGHT)),

    /** T-shaped corridor with three openings. */
    T_JUNCTION(Set.of(
        Direction.LEFT,
        Direction.UP,
        Direction.RIGHT));

    private final Set<Direction> baseOpenings;

    TileShape(Set<Direction> baseOpenings) {
        this.baseOpenings = baseOpenings;
    }

    /**
     * Returns the openings at rotation zero.
     *
     * @return an unmodifiable set of base openings
     */
    public Set<Direction> baseOpenings() {
        return baseOpenings;
    }

    /**
     * Returns the openings after clockwise rotation.
     *
     * @param rotation absolute orientation from 0 to 3
     * @return the rotated openings
     * @throws IllegalArgumentException if rotation is invalid
     */
    public Set<Direction> openings(int rotation) {
        if (rotation < 0 || rotation > 3) {
            throw new IllegalArgumentException(
                "Rotation must be between 0 and 3");
        }

        Set<Direction> result =
            java.util.EnumSet.noneOf(Direction.class);

        for (Direction direction : baseOpenings) {
            result.add(direction.rotate(rotation));
        }

        return Set.copyOf(result);
    }
}

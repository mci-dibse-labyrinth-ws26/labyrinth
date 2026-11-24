
package edu.mci.labyrinth.core;

import java.util.Objects;

/**
 * Describes a tile push by its row or column index and direction.
 *
 * @param index the row or column index
 * @param direction the direction in which tiles are pushed
 */
public record Push(int index, Direction direction) {

    /**
     * Validates the push parameters.
     *
     * @throws NullPointerException if direction is null
     */
    public Push {
        Objects.requireNonNull(direction, "Direction must not be null");
    }

    /**
     * Returns whether the given push would reverse this push.
     *
     * @param next the proposed next push
     * @return true if the push would immediately reverse this push
     */
    public boolean isReversedBy(Push next) {
        Objects.requireNonNull(next, "Next push must not be null");

        return index == next.index()
            && direction.opposite() == next.direction();
    }
}

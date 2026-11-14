
package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tests position coordinates and neighbour calculation.
 */
@Tag("A-29")
class PositionTest {

    @Test
    void acceptsValidPosition() {
        Position position = new Position(2, 3);

        assertEquals(2, position.row());
        assertEquals(3, position.column());
    }

    @Test
    void acceptsOrigin() {
        assertEquals(new Position(0, 0), new Position(0, 0));
    }

    @Test
    void allowsCoordinatesOutsideBoard() {
        Position position = new Position(-1, -2);

        assertEquals(-1, position.row());
        assertEquals(-2, position.column());
    }

    @Test
    void calculatesNeighboursCorrectly() {
        Position position = new Position(2, 3);

        assertEquals(new Position(1, 3),
            position.neighbor(Direction.UP));

        assertEquals(new Position(2, 4),
            position.neighbor(Direction.RIGHT));

        assertEquals(new Position(3, 3),
            position.neighbor(Direction.DOWN));

        assertEquals(new Position(2, 2),
            position.neighbor(Direction.LEFT));
    }

    @Test
    void neighbourCanBeOutsideBoard() {
        Position origin = new Position(0, 0);

        assertEquals(new Position(-1, 0),
            origin.neighbor(Direction.UP));

        assertEquals(new Position(0, -1),
            origin.neighbor(Direction.LEFT));
    }

    @Test
    void rejectsNullDirection() {
        Position position = new Position(0, 0);

        assertThrows(NullPointerException.class,
            () -> position.neighbor(null));
    }
}

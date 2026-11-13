
package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests tile openings for all shapes and rotations.
 */
class TileShapeTest {

    @Tag("A-32")
    @ParameterizedTest
    @MethodSource("openingCases")
    void openingsMatchShapeAndRotation(
        TileShape shape,
        int rotation,
        Set<Direction> expected) {

        assertEquals(expected, shape.openings(rotation));
    }

    private static Stream<Arguments> openingCases() {
        return Stream.of(
            // Straight
            Arguments.of(TileShape.STRAIGHT, 0,
                Set.of(Direction.UP, Direction.DOWN)),
            Arguments.of(TileShape.STRAIGHT, 1,
                Set.of(Direction.LEFT, Direction.RIGHT)),
            Arguments.of(TileShape.STRAIGHT, 2,
                Set.of(Direction.UP, Direction.DOWN)),
            Arguments.of(TileShape.STRAIGHT, 3,
                Set.of(Direction.LEFT, Direction.RIGHT)),

            // Corner
            Arguments.of(TileShape.CORNER, 0,
                Set.of(Direction.UP, Direction.RIGHT)),
            Arguments.of(TileShape.CORNER, 1,
                Set.of(Direction.RIGHT, Direction.DOWN)),
            Arguments.of(TileShape.CORNER, 2,
                Set.of(Direction.DOWN, Direction.LEFT)),
            Arguments.of(TileShape.CORNER, 3,
                Set.of(Direction.LEFT, Direction.UP)),

            // T-junction
            Arguments.of(TileShape.T_JUNCTION, 0,
                Set.of(Direction.LEFT, Direction.UP, Direction.RIGHT)),
            Arguments.of(TileShape.T_JUNCTION, 1,
                Set.of(Direction.UP, Direction.RIGHT, Direction.DOWN)),
            Arguments.of(TileShape.T_JUNCTION, 2,
                Set.of(Direction.RIGHT, Direction.DOWN, Direction.LEFT)),
            Arguments.of(TileShape.T_JUNCTION, 3,
                Set.of(Direction.DOWN, Direction.LEFT, Direction.UP))
        );
    }
}

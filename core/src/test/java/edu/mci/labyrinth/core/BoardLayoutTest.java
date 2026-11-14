
package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the required orientations of fixed corner and edge tiles.
 */
class BoardLayoutTest {

    @Tag("A-32")
    @ParameterizedTest
    @MethodSource("cornerCases")
    void cornersOpenTowardsBoardInterior(
        int rotation,
        Set<Direction> expected) {

        Tile tile = new Tile(
            TileShape.CORNER, rotation, true, null, null);

        assertEquals(expected, tile.openings());
    }

    private static Stream<Arguments> cornerCases() {
        return Stream.of(
            // Top-left
            Arguments.of(1,
                Set.of(Direction.RIGHT, Direction.DOWN)),
            // Top-right
            Arguments.of(2,
                Set.of(Direction.DOWN, Direction.LEFT)),
            // Bottom-right
            Arguments.of(3,
                Set.of(Direction.LEFT, Direction.UP)),
            // Bottom-left
            Arguments.of(0,
                Set.of(Direction.UP, Direction.RIGHT))
        );
    }

    @Tag("A-32")
    @ParameterizedTest
    @MethodSource("edgeCases")
    void edgeTilesDoNotOpenOutsideBoard(
        int rotation,
        Set<Direction> expected) {

        Tile tile = new Tile(
            TileShape.T_JUNCTION, rotation, true, null, null);

        assertEquals(expected, tile.openings());
    }

    private static Stream<Arguments> edgeCases() {
        return Stream.of(
            // Top edge: closed towards UP
            Arguments.of(2,
                Set.of(Direction.RIGHT, Direction.DOWN, Direction.LEFT)),
            // Right edge: closed towards RIGHT
            Arguments.of(3,
                Set.of(Direction.DOWN, Direction.LEFT, Direction.UP)),
            // Bottom edge: closed towards DOWN
            Arguments.of(0,
                Set.of(Direction.LEFT, Direction.UP, Direction.RIGHT)),
            // Left edge: closed towards LEFT
            Arguments.of(1,
                Set.of(Direction.UP, Direction.RIGHT, Direction.DOWN))
        );
    }
}

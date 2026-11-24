package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("F-210")
@Tag("A-02")
@Tag("A-32")
class FixedTileLayoutTest {

    @ParameterizedTest(name = "({0}, {1}) fixed: {2}")
    @CsvSource({
        "0, 0, true",
        "2, 4, true",
        "6, 6, true",
        "1, 0, false",
        "0, 1, false",
        "3, 5, false",
    })
    void fixedPositionsHaveEvenRowAndColumn(int row, int col, boolean expected) {
        assertEquals(expected, FixedTileLayout.isFixedPosition(new Position(row, col)));
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("allBoardSizes")
    void cornersOpenTowardsBoardInterior(int rows, int cols) {
        assertCorner(rows, cols, 0, 0, Direction.RIGHT, Direction.DOWN);
        assertCorner(rows, cols, 0, cols - 1, Direction.DOWN, Direction.LEFT);
        assertCorner(rows, cols, rows - 1, cols - 1, Direction.UP, Direction.LEFT);
        assertCorner(rows, cols, rows - 1, 0, Direction.UP, Direction.RIGHT);
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("allBoardSizes")
    void borderTilesAreJunctionsClosedTowardsBorder(int rows, int cols) {
        Random random = new Random(1);

        for (Position position : fixedPositions(rows, cols)) {
            Set<Direction> outwards = outwardDirections(position, rows, cols);
            if (outwards.size() != 1) {
                continue;
            }
            Tile tile = FixedTileLayout.fixedTileAt(position, rows, cols, random);

            assertEquals(TileShape.T_JUNCTION, tile.shape(), position.toString());
            assertEquals(EnumSet.complementOf(EnumSet.copyOf(outwards)), tile.openings(),
                    position.toString());
        }
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("allBoardSizes")
    void everyFixedTileIsFixedAndEmpty(int rows, int cols) {
        Random random = new Random(1);

        for (Position position : fixedPositions(rows, cols)) {
            Tile tile = FixedTileLayout.fixedTileAt(position, rows, cols, random);

            assertTrue(tile.fixed(), position.toString());
            assertNull(tile.treasure(), position.toString());
            assertNull(tile.bonus(), position.toString());
        }
    }

    @Test
    void innerTilesAreJunctionsWithRandomRotation() {
        Position inner = new Position(2, 2);
        Random random = new Random(42);
        Set<Integer> rotations = new HashSet<>();

        for (int draw = 0; draw < 50; draw++) {
            Tile tile = FixedTileLayout.fixedTileAt(inner, 7, 7, random);

            assertEquals(TileShape.T_JUNCTION, tile.shape());
            rotations.add(tile.rotation());
        }

        assertEquals(Set.of(0, 1, 2, 3), rotations);
    }

    @Test
    @Tag("NF-11")
    void sameSeedGivesSameInnerRotations() {
        assertEquals(innerRotations(42), innerRotations(42));
    }

    @Test
    void onlyInnerTilesConsumeRandomValues() {
        Random used = new Random(7);
        Random untouched = new Random(7);

        for (Position position : fixedPositions(7, 7)) {
            if (outwardDirections(position, 7, 7).isEmpty()) {
                continue;
            }
            FixedTileLayout.fixedTileAt(position, 7, 7, used);
        }

        assertEquals(untouched.nextInt(), used.nextInt());
    }

    @ParameterizedTest(name = "({0}, {1})")
    @CsvSource({"1, 0", "0, 1", "3, 3"})
    void rejectsLoosePosition(int row, int col) {
        assertThrows(IllegalArgumentException.class,
                () -> FixedTileLayout.fixedTileAt(new Position(row, col), 7, 7, new Random(1)));
    }

    @ParameterizedTest(name = "({0}, {1})")
    @CsvSource({"-2, 0", "0, -2", "8, 0", "0, 8"})
    void rejectsPositionOutsideBoard(int row, int col) {
        assertThrows(IllegalArgumentException.class,
                () -> FixedTileLayout.fixedTileAt(new Position(row, col), 7, 7, new Random(1)));
    }

    @Test
    void negativeOddPositionIsNotFixed() {
        assertFalse(FixedTileLayout.isFixedPosition(new Position(-1, 0)));
    }

    private static void assertCorner(
            int rows, int cols, int row, int col, Direction first, Direction second) {
        Tile tile = FixedTileLayout.fixedTileAt(new Position(row, col), rows, cols, new Random(1));

        assertEquals(TileShape.CORNER, tile.shape());
        assertEquals(EnumSet.of(first, second), tile.openings());
    }

    private static List<Integer> innerRotations(long seed) {
        Random random = new Random(seed);
        List<Integer> rotations = new ArrayList<>();

        for (Position position : fixedPositions(9, 9)) {
            Tile tile = FixedTileLayout.fixedTileAt(position, 9, 9, random);
            if (outwardDirections(position, 9, 9).isEmpty()) {
                rotations.add(tile.rotation());
            }
        }
        return rotations;
    }

    private static List<Position> fixedPositions(int rows, int cols) {
        List<Position> positions = new ArrayList<>();
        for (int row = 0; row < rows; row += 2) {
            for (int col = 0; col < cols; col += 2) {
                positions.add(new Position(row, col));
            }
        }
        return positions;
    }

    private static Set<Direction> outwardDirections(Position position, int rows, int cols) {
        Set<Direction> outwards = EnumSet.noneOf(Direction.class);
        if (position.row() == 0) {
            outwards.add(Direction.UP);
        }
        if (position.row() == rows - 1) {
            outwards.add(Direction.DOWN);
        }
        if (position.column() == 0) {
            outwards.add(Direction.LEFT);
        }
        if (position.column() == cols - 1) {
            outwards.add(Direction.RIGHT);
        }
        return outwards;
    }

    private static Stream<Arguments> allBoardSizes() {
        return IntStream.iterate(3, size -> size <= 15, size -> size + 2).boxed()
                .flatMap(rows -> IntStream.iterate(3, size -> size <= 15, size -> size + 2)
                        .mapToObj(cols -> Arguments.of(rows, cols)));
    }
}

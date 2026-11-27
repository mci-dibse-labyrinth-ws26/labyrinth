package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("F-216")
@Tag("A-33")
class PathFinderTest {

    /** Ring of eight fields around a centre that is closed towards all neighbours. */
    private static final Board RING = BoardFixtures.board(
            "┌─┐",
            "│─│",
            "└─┘");

    @Test
    @Tag("A-11")
    void enclosedFigureReachesOnlyItsOwnFieldWithZeroSteps() {
        assertEquals(Map.of(new Position(1, 1), 0),
                PathFinder.distancesFrom(RING, new Position(1, 1)));
    }

    @Test
    @Tag("A-11")
    void countsShortestPathWhenSeveralPathsHaveTheSameLength() {
        Map<Position, Integer> distances = PathFinder.distancesFrom(RING, new Position(0, 0));

        assertEquals(8, distances.size());
        assertEquals(4, distances.get(new Position(2, 2)));
        assertEquals(2, distances.get(new Position(0, 2)));
        assertEquals(2, distances.get(new Position(2, 0)));
    }

    @Test
    @Tag("A-11")
    void countsStepsAlongCorridor() {
        Board corridor = BoardFixtures.board(
                "───────",
                "│││││││",
                "│││││││");

        Map<Position, Integer> distances = PathFinder.distancesFrom(corridor, new Position(0, 3));

        assertEquals(7, distances.size());
        for (int col = 0; col < 7; col++) {
            assertEquals(Math.abs(col - 3), distances.get(new Position(0, col)), "column " + col);
        }
    }

    @Test
    void findsFieldsInNeighbourOrderUpRightDownLeft() {
        List<Position> order = PathFinder.reachablePositions(RING, new Position(0, 0));

        assertEquals(List.of(
                new Position(0, 0),
                new Position(0, 1), new Position(1, 0),
                new Position(0, 2), new Position(2, 0),
                new Position(1, 2), new Position(2, 1),
                new Position(2, 2)), order);
    }

    @Test
    void connectsFieldsOnlyIfBothTilesAreOpenTowardsEachOther() {
        Board board = BoardFixtures.board(
                "─││",
                "│││",
                "│││");

        assertEquals(List.of(new Position(0, 0)),
                PathFinder.reachablePositions(board, new Position(0, 0)));
        assertEquals(List.of(new Position(0, 1), new Position(1, 1), new Position(2, 1)),
                PathFinder.reachablePositions(board, new Position(0, 1)));
    }

    @Test
    void borderIsClosedEvenIfTilesOpenOutwards() {
        Board board = BoardFixtures.board(
                "│─│",
                "│─│",
                "│─│");

        assertEquals(List.of(new Position(0, 0), new Position(1, 0), new Position(2, 0)),
                PathFinder.reachablePositions(board, new Position(0, 0)));
    }

    @ParameterizedTest(name = "seed {0}")
    @ValueSource(longs = {0, 1, 42, 2026})
    void reachabilityAndDistanceAreSymmetric(long seed) {
        Board board = BoardGenerator.generateBoard(7, 7, new Random(seed));

        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                Position from = new Position(row, col);
                Map<Position, Integer> distances = PathFinder.distancesFrom(board, from);
                for (Map.Entry<Position, Integer> entry : distances.entrySet()) {
                    assertEquals(entry.getValue(),
                            PathFinder.distancesFrom(board, entry.getKey()).get(from),
                            from + " -> " + entry.getKey());
                }
            }
        }
    }

    @Test
    void sameBoardGivesSameResult() {
        Board board = BoardGenerator.generateBoard(15, 15, new Random(7));

        assertEquals(PathFinder.reachablePositions(board, new Position(7, 7)),
                PathFinder.reachablePositions(board, new Position(7, 7)));
    }

    /**
     * On 15 x 15 the strategic AI simulates up to 2 * (7 + 7) * 4 = 112 pushes per decision and
     * searches once per push (F-402); 2,000 searches leave a wide margin for slow CI machines.
     */
    @Test
    @Tag("NF-03")
    void staysFastOnLargestBoard() {
        Board board = BoardGenerator.generateBoard(15, 15, new Random(3));

        assertTimeout(Duration.ofSeconds(2), () -> {
            for (int search = 0; search < 2_000; search++) {
                PathFinder.distancesFrom(board, new Position(search % 15, search / 15 % 15));
            }
        });
    }

    @Test
    void rejectsStartOutsideBoard() {
        assertThrows(IllegalArgumentException.class,
                () -> PathFinder.distancesFrom(RING, new Position(3, 0)));
        assertThrows(IllegalArgumentException.class,
                () -> PathFinder.distancesFrom(RING, new Position(0, -1)));
    }

    @Test
    void rejectsMissingArguments() {
        assertThrows(NullPointerException.class,
                () -> PathFinder.distancesFrom(null, new Position(0, 0)));
        assertThrows(NullPointerException.class, () -> PathFinder.distancesFrom(RING, null));
    }

    @Test
    void resultCannotBeModified() {
        Map<Position, Integer> distances = PathFinder.distancesFrom(RING, new Position(0, 0));

        assertThrows(UnsupportedOperationException.class, distances::clear);
        assertThrows(UnsupportedOperationException.class,
                () -> PathFinder.reachablePositions(RING, new Position(0, 0)).clear());
    }
}

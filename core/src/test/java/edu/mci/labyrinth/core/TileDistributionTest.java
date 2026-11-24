package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("F-210")
@Tag("A-03")
class TileDistributionTest {

    @ParameterizedTest(name = "{0} x {1}: {2} fixed, {3} loose")
    @CsvSource({
        "3, 3, 4, 6",
        "5, 5, 9, 17",
        "7, 7, 16, 34",
        "15, 15, 64, 162",
        "5, 9, 15, 31",
    })
    void countsFixedAndLooseTiles(int rows, int cols, int fixed, int loose) {
        assertEquals(fixed, TileDistribution.fixedTileCount(rows, cols));
        assertEquals(loose, TileDistribution.looseTileCount(rows, cols));
    }

    @ParameterizedTest(name = "{0} x {1}: {2} : {3} : {4}")
    @CsvSource({
        "3, 3, 2, 3, 1",
        "5, 5, 6, 8, 3",
        "7, 7, 12, 16, 6",
        "15, 15, 57, 76, 29",
        "3, 5, 3, 5, 2",
    })
    void splitsLooseTilesByLargestRemainder(
            int rows, int cols, int straights, int corners, int tJunctions) {
        Map<TileShape, Integer> counts = TileDistribution.looseTileCounts(rows, cols);

        assertEquals(straights, counts.get(TileShape.STRAIGHT));
        assertEquals(corners, counts.get(TileShape.CORNER));
        assertEquals(tJunctions, counts.get(TileShape.T_JUNCTION));
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("allBoardSizes")
    void usesOneTilePerFieldPlusSpareTile(int rows, int cols) {
        int loose = TileDistribution.looseTileCounts(rows, cols).values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        assertEquals(TileDistribution.looseTileCount(rows, cols), loose);
        assertEquals(rows * cols + 1, TileDistribution.fixedTileCount(rows, cols) + loose);
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("allBoardSizes")
    void staysWithinOneTileOfExactRatio(int rows, int cols) {
        int loose = TileDistribution.looseTileCount(rows, cols);
        Map<TileShape, Integer> counts = TileDistribution.looseTileCounts(rows, cols);

        assertWithinOneTile(loose * 12, counts.get(TileShape.STRAIGHT));
        assertWithinOneTile(loose * 16, counts.get(TileShape.CORNER));
        assertWithinOneTile(loose * 6, counts.get(TileShape.T_JUNCTION));
    }

    @ParameterizedTest(name = "{0} x {1}")
    @CsvSource({"1, 7", "7, 1", "4, 7", "7, 4", "17, 7", "7, 17"})
    void rejectsInvalidBoardSize(int rows, int cols) {
        assertThrows(IllegalArgumentException.class,
                () -> TileDistribution.looseTileCounts(rows, cols));
    }

    @Test
    void returnsUnmodifiableCounts() {
        Map<TileShape, Integer> counts = TileDistribution.looseTileCounts(7, 7);

        assertThrows(UnsupportedOperationException.class,
                () -> counts.put(TileShape.STRAIGHT, 0));
    }

    private static void assertWithinOneTile(int weightedExact, int actual) {
        int difference = Math.abs(actual * 34 - weightedExact);
        assertTrue(difference < 34, "expected " + weightedExact / 34.0 + ", was " + actual);
    }

    private static Stream<Arguments> allBoardSizes() {
        return IntStream.iterate(3, size -> size <= 15, size -> size + 2).boxed()
                .flatMap(rows -> IntStream.iterate(3, size -> size <= 15, size -> size + 2)
                        .mapToObj(cols -> Arguments.of(rows, cols)));
    }
}

package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("F-210")
@Tag("A-02")
@Tag("A-03")
class BoardGeneratorTest {

    private static final long[] SEEDS = {0, 1, 42, 2026};

    @ParameterizedTest(name = "{0} x {1}, seed {2}")
    @MethodSource("boardSizesAndSeeds")
    void fixedTilesLieExactlyOnFixedPositions(int rows, int cols, long seed) {
        Board board = BoardGenerator.generateBoard(rows, cols, new Random(seed));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Position position = new Position(row, col);
                assertEquals(FixedTileLayout.isFixedPosition(position),
                        board.tileAt(position).fixed(), position.toString());
            }
        }
        assertFalse(board.spareTile().fixed());
    }

    @ParameterizedTest(name = "{0} x {1}, seed {2}")
    @MethodSource("boardSizesAndSeeds")
    void looseTilesFollowTileDistribution(int rows, int cols, long seed) {
        Board board = BoardGenerator.generateBoard(rows, cols, new Random(seed));

        Map<TileShape, Integer> counts = new EnumMap<>(TileShape.class);
        for (Tile tile : looseTiles(board)) {
            counts.merge(tile.shape(), 1, Integer::sum);
        }

        assertEquals(TileDistribution.looseTileCount(rows, cols), looseTiles(board).size());
        for (TileShape shape : TileShape.values()) {
            assertEquals(TileDistribution.looseTileCounts(rows, cols).get(shape),
                    counts.getOrDefault(shape, 0), shape.toString());
        }
    }

    @ParameterizedTest(name = "{0} x {1}, seed {2}")
    @MethodSource("boardSizesAndSeeds")
    void cornersAndBorderTilesMatchFixedLayout(int rows, int cols, long seed) {
        Board board = BoardGenerator.generateBoard(rows, cols, new Random(seed));
        Random unused = new Random(0);

        for (int row = 0; row < rows; row += 2) {
            for (int col = 0; col < cols; col += 2) {
                boolean onBorder = row == 0 || row == rows - 1 || col == 0 || col == cols - 1;
                if (!onBorder) {
                    continue;
                }
                Position position = new Position(row, col);
                assertEquals(FixedTileLayout.fixedTileAt(position, rows, cols, unused),
                        board.tileAt(position), position.toString());
            }
        }
    }

    @ParameterizedTest(name = "{0} x {1}, seed {2}")
    @MethodSource("boardSizesAndSeeds")
    void generatedBoardHasNoTreasuresOrBonusesYet(int rows, int cols, long seed) {
        Board board = BoardGenerator.generateBoard(rows, cols, new Random(seed));

        for (Tile tile : allTiles(board)) {
            assertNull(tile.treasure());
            assertNull(tile.bonus());
        }
    }

    @Test
    void looseTilesAreShuffledAndRotated() {
        Board board = BoardGenerator.generateBoard(15, 15, new Random(42));

        Set<Integer> rotations = new HashSet<>();
        Set<TileShape> shapesInFirstLooseRow = new HashSet<>();
        for (Tile tile : looseTiles(board)) {
            rotations.add(tile.rotation());
        }
        for (int col = 0; col < 15; col++) {
            shapesInFirstLooseRow.add(board.tileAt(new Position(1, col)).shape());
        }

        assertEquals(Set.of(0, 1, 2, 3), rotations);
        assertEquals(3, shapesInFirstLooseRow.size(),
                "loose tiles must be mixed, not grouped by shape");
    }

    @ParameterizedTest(name = "seed {0}")
    @MethodSource("seeds")
    @Tag("NF-11")
    void sameSeedGivesSameBoard(long seed) {
        assertEquals(BoardGenerator.generateBoard(7, 7, new Random(seed)),
                BoardGenerator.generateBoard(7, 7, new Random(seed)));
    }

    @Test
    @Tag("NF-11")
    void differentSeedsGiveDifferentBoards() {
        assertNotEquals(BoardGenerator.generateBoard(7, 7, new Random(42)),
                BoardGenerator.generateBoard(7, 7, new Random(43)));
    }

    @ParameterizedTest(name = "{0} x {1}")
    @CsvSource({"1, 7", "7, 1", "4, 7", "7, 4", "17, 7", "7, 17"})
    void rejectsInvalidBoardSize(int rows, int cols) {
        assertThrows(IllegalArgumentException.class,
                () -> BoardGenerator.generateBoard(rows, cols, new Random(1)));
    }

    @Test
    void rejectsMissingRandomSource() {
        assertThrows(NullPointerException.class, () -> BoardGenerator.generateBoard(7, 7, null));
    }

    private static List<Tile> allTiles(Board board) {
        List<Tile> tiles = new ArrayList<>();
        for (Tile[] row : board.tiles()) {
            tiles.addAll(List.of(row));
        }
        tiles.add(board.spareTile());
        return tiles;
    }

    private static List<Tile> looseTiles(Board board) {
        return allTiles(board).stream().filter(tile -> !tile.fixed()).toList();
    }

    private static Stream<Arguments> boardSizesAndSeeds() {
        return IntStream.iterate(3, size -> size <= 15, size -> size + 2).boxed()
                .flatMap(rows -> IntStream.iterate(3, size -> size <= 15, size -> size + 2)
                        .boxed()
                        .flatMap(cols -> LongStream.of(SEEDS)
                                .mapToObj(seed -> Arguments.of(rows, cols, seed))));
    }

    private static LongStream seeds() {
        return LongStream.of(SEEDS);
    }
}

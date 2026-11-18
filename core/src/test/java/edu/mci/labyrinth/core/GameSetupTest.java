package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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
import org.junit.jupiter.params.provider.MethodSource;

@Tag("F-210")
class GameSetupTest {

    private static final long[] SEEDS = {0, 42};

    @ParameterizedTest(name = "{0}")
    @MethodSource("configsAndSeeds")
    void placesEveryTreasureOfTheGameExactlyOnce(GameConfig config, long seed) {
        GameSetup setup = BoardGenerator.generate(config, seed);

        List<Treasure> placed = placedTreasures(setup.board());

        assertEquals(config.totalTreasures(), placed.size());
        assertEquals(placed.size(), new HashSet<>(placed).size(), "treasures must be distinct");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("configsAndSeeds")
    void cornersNeverHoldTreasures(GameConfig config, long seed) {
        Board board = BoardGenerator.generate(config, seed).board();
        int last = config.rows() - 1;
        int right = config.cols() - 1;

        for (Position corner : List.of(new Position(0, 0), new Position(0, right),
                new Position(last, 0), new Position(last, right))) {
            assertNull(board.tileAt(corner).treasure(), corner.toString());
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("configsAndSeeds")
    void playersGetColorsAndStartCornersInJoinOrder(GameConfig config, long seed) {
        GameSetup setup = BoardGenerator.generate(config, seed);
        int last = config.rows() - 1;
        int right = config.cols() - 1;
        List<Position> expectedCorners = List.of(new Position(0, 0), new Position(last, right),
                new Position(0, right), new Position(last, 0));

        assertEquals(List.of(PlayerColor.values()).subList(0, config.playerCount()),
                setup.players());
        for (int i = 0; i < config.playerCount(); i++) {
            assertEquals(expectedCorners.get(i), setup.startPositions().get(setup.players().get(i)));
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("configsAndSeeds")
    void decksSplitThePlacedTreasuresEvenly(GameConfig config, long seed) {
        GameSetup setup = BoardGenerator.generate(config, seed);

        List<Treasure> dealt = new ArrayList<>();
        for (PlayerColor player : setup.players()) {
            List<Treasure> deck = setup.treasureDecks().get(player);
            assertEquals(config.treasuresPerPlayer(), deck.size(), player.toString());
            dealt.addAll(deck);
        }

        assertEquals(new HashSet<>(placedTreasures(setup.board())), new HashSet<>(dealt));
        assertEquals(dealt.size(), new HashSet<>(dealt).size(), "decks must not share treasures");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("configsAndSeeds")
    void turnOrderContainsEveryPlayerOnce(GameConfig config, long seed) {
        GameSetup setup = BoardGenerator.generate(config, seed);

        assertEquals(Set.copyOf(setup.players()), Set.copyOf(setup.turnOrder()));
        assertEquals(setup.players().size(), setup.turnOrder().size());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("configsAndSeeds")
    @Tag("NF-11")
    void treasuresDoNotChangeTheGeneratedTiles(GameConfig config, long seed) {
        Board withTreasures = BoardGenerator.generate(config, seed).board();
        Board withoutTreasures =
                BoardGenerator.generateBoard(config.rows(), config.cols(), new Random(seed));

        assertEquals(withoutTreasures, removeTreasures(withTreasures));
    }

    @Test
    void smallestBoardPutsTreasuresOnAllTilesExceptCorners() {
        Board board = BoardGenerator.generate(new GameConfig(3, 3, 2, 3), 7).board();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                boolean corner = (row == 0 || row == 2) && (col == 0 || col == 2);
                Treasure treasure = board.tileAt(new Position(row, col)).treasure();
                assertEquals(corner, treasure == null, "(" + row + ", " + col + ")");
            }
        }
        assertNotNull(board.spareTile().treasure());
    }

    @Test
    void largestGameUsesAllTwentyFourTreasures() {
        GameSetup setup = BoardGenerator.generate(new GameConfig(15, 15, 4, 6), 7);

        assertEquals(Set.copyOf(allTreasures()), Set.copyOf(placedTreasures(setup.board())));
    }

    @Test
    void turnOrderDependsOnSeed() {
        GameConfig config = new GameConfig(7, 7, 4, 6);
        Set<List<PlayerColor>> orders = new HashSet<>();

        for (long seed = 0; seed < 20; seed++) {
            orders.add(BoardGenerator.generate(config, seed).turnOrder());
        }

        assertTrue(orders.size() > 1, "turn order must be drawn, not fixed");
    }

    @Test
    @Tag("NF-11")
    void sameSeedGivesSameSetup() {
        GameConfig config = new GameConfig(7, 7, 4, 6);

        assertEquals(BoardGenerator.generate(config, 2026), BoardGenerator.generate(config, 2026));
    }

    @Test
    @Tag("NF-11")
    void differentSeedsGiveDifferentSetups() {
        GameConfig config = new GameConfig(7, 7, 4, 6);

        assertNotEquals(BoardGenerator.generate(config, 1), BoardGenerator.generate(config, 2));
    }

    @Test
    void setupCannotBeModified() {
        GameSetup setup = BoardGenerator.generate(new GameConfig(7, 7, 2, 6), 1);

        assertThrows(UnsupportedOperationException.class, () -> setup.players().clear());
        assertThrows(UnsupportedOperationException.class, () -> setup.turnOrder().clear());
        assertThrows(UnsupportedOperationException.class, () -> setup.startPositions().clear());
        assertThrows(UnsupportedOperationException.class, () -> setup.treasureDecks().clear());
        assertThrows(UnsupportedOperationException.class,
                () -> setup.treasureDecks().get(PlayerColor.RED).clear());
    }

    @Test
    void rejectsTurnOrderWithRepeatedPlayer() {
        GameSetup valid = BoardGenerator.generate(new GameConfig(7, 7, 2, 6), 1);

        assertThrows(IllegalArgumentException.class, () -> new GameSetup(valid.board(),
                valid.players(), valid.startPositions(), valid.treasureDecks(),
                List.of(PlayerColor.RED, PlayerColor.RED)));
    }

    @Test
    void rejectsMissingStartPosition() {
        GameSetup valid = BoardGenerator.generate(new GameConfig(7, 7, 2, 6), 1);

        assertThrows(IllegalArgumentException.class, () -> new GameSetup(valid.board(),
                valid.players(), Map.of(PlayerColor.RED, new Position(0, 0)),
                valid.treasureDecks(), valid.turnOrder()));
    }

    @Test
    void rejectsMissingTreasureDeck() {
        GameSetup valid = BoardGenerator.generate(new GameConfig(7, 7, 2, 6), 1);

        assertThrows(IllegalArgumentException.class, () -> new GameSetup(valid.board(),
                valid.players(), valid.startPositions(),
                Map.of(PlayerColor.RED, List.of()), valid.turnOrder()));
    }

    @Test
    void rejectsDuplicateOrMissingPlayers() {
        GameSetup valid = BoardGenerator.generate(new GameConfig(7, 7, 2, 6), 1);

        assertThrows(IllegalArgumentException.class, () -> new GameSetup(valid.board(),
                List.of(PlayerColor.RED, PlayerColor.RED), valid.startPositions(),
                valid.treasureDecks(), valid.turnOrder()));
        assertThrows(IllegalArgumentException.class, () -> new GameSetup(valid.board(),
                List.of(), valid.startPositions(), valid.treasureDecks(), valid.turnOrder()));
    }

    private static List<Treasure> placedTreasures(Board board) {
        List<Treasure> treasures = new ArrayList<>();
        for (Tile[] row : board.tiles()) {
            for (Tile tile : row) {
                if (tile.treasure() != null) {
                    treasures.add(tile.treasure());
                }
            }
        }
        if (board.spareTile().treasure() != null) {
            treasures.add(board.spareTile().treasure());
        }
        return treasures;
    }

    private static Board removeTreasures(Board board) {
        Tile[][] tiles = board.tiles();
        for (Tile[] row : tiles) {
            for (int col = 0; col < row.length; col++) {
                row[col] = withoutTreasure(row[col]);
            }
        }
        return new Board(board.rows(), board.cols(), tiles, withoutTreasure(board.spareTile()));
    }

    private static Tile withoutTreasure(Tile tile) {
        return new Tile(tile.shape(), tile.rotation(), tile.fixed(), null, tile.bonus());
    }

    private static List<Treasure> allTreasures() {
        return IntStream.range(0, Treasure.NAMES.size()).mapToObj(Treasure::of).toList();
    }

    private static Stream<Arguments> configsAndSeeds() {
        return IntStream.iterate(3, size -> size <= 15, size -> size + 2).boxed()
                .flatMap(rows -> IntStream.iterate(3, size -> size <= 15, size -> size + 2)
                        .boxed()
                        .flatMap(cols -> IntStream.rangeClosed(2, 4)
                                .mapToObj(players -> configFor(rows, cols, players))))
                .flatMap(config -> LongStream.of(SEEDS)
                        .mapToObj(seed -> Arguments.of(config, seed)));
    }

    /** Default of 6 treasures per player, reduced where the board or the 24 symbols allow less. */
    private static GameConfig configFor(int rows, int cols, int players) {
        int treasures = Math.min(6, Math.min(24 / players, (rows * cols - 3) / players));
        return new GameConfig(rows, cols, players, treasures);
    }
}

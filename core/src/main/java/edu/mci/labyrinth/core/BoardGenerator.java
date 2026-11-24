package edu.mci.labyrinth.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.random.RandomGenerator;

/**
 * Builds the starting state of a game from a seed (Pflichtenheft 6.1).
 *
 * <p>The random values are always drawn in the same order, so the same seed produces the same
 * game (NF-11):
 * <ol>
 *   <li>fixed tiles, row by row: only inner fixed tiles draw a rotation,</li>
 *   <li>loose tiles, created in the order of {@link TileShape}, are shuffled,</li>
 *   <li>each loose tile in shuffled order draws its rotation,</li>
 *   <li>loose tiles fill the free fields row by row; the last one becomes the spare tile,</li>
 *   <li>the 24 treasures are shuffled and the first {@code playerCount * treasuresPerPlayer}
 *       are used,</li>
 *   <li>the tiles that may hold a treasure are shuffled and receive the treasures in order,</li>
 *   <li>the turn order is shuffled.</li>
 * </ol>
 */
public final class BoardGenerator {

    private static final int ROTATIONS = 4;

    private BoardGenerator() {
    }

    /**
     * Generates the starting state of a game.
     *
     * <p>Treasures lie on any tile except the four corners, the spare tile included (A-04).
     * Players keep the colors the server assigned when they joined (A-06), so the list may have
     * gaps, for example {@code [BLUE, GREEN]} after a player left the lobby. Start corners follow
     * the position in the list: top left, bottom right, top right, bottom left; two players
     * therefore always start diagonally. Each player gets {@code treasuresPerPlayer} treasures of
     * the game as hidden deck.
     *
     * @param config validated configuration of the game
     * @param players colors of the players in join order, one per player of {@code config}
     * @param seed start value of all random decisions; the same seed gives the same game
     * @return board with treasures, start corners, treasure decks and turn order
     * @throws IllegalArgumentException if the number of colors does not match the configuration
     *     or a color occurs twice
     */
    public static GameSetup generate(GameConfig config, List<PlayerColor> players, long seed) {
        Objects.requireNonNull(config, "Config must not be null");
        players = List.copyOf(players);
        if (players.size() != config.playerCount()) {
            throw new IllegalArgumentException("Expected " + config.playerCount()
                    + " player colors, got " + players.size());
        }
        if (Set.copyOf(players).size() != players.size()) {
            throw new IllegalArgumentException("Player colors must be distinct: " + players);
        }

        int rows = config.rows();
        int cols = config.cols();
        RandomGenerator random = new Random(seed);

        Board board = generateBoard(rows, cols, random);

        List<Treasure> treasures = new ArrayList<>();
        for (int id = 0; id < Treasure.NAMES.size(); id++) {
            treasures.add(Treasure.of(id));
        }
        Collections.shuffle(treasures, random);
        treasures = List.copyOf(treasures.subList(0, config.totalTreasures()));

        List<Position> corners = startCorners(rows, cols);
        int spareIndex = rows * cols;
        List<Integer> treasureTiles = new ArrayList<>();
        for (int index = 0; index <= spareIndex; index++) {
            if (index == spareIndex || !corners.contains(position(index, cols))) {
                treasureTiles.add(index);
            }
        }
        Collections.shuffle(treasureTiles, random);

        Tile[][] tiles = board.tiles();
        Tile spareTile = board.spareTile();
        for (int i = 0; i < treasures.size(); i++) {
            int index = treasureTiles.get(i);
            if (index == spareIndex) {
                spareTile = withTreasure(spareTile, treasures.get(i));
            } else {
                Position position = position(index, cols);
                tiles[position.row()][position.column()] =
                        withTreasure(tiles[position.row()][position.column()], treasures.get(i));
            }
        }

        Map<PlayerColor, Position> startPositions = new EnumMap<>(PlayerColor.class);
        Map<PlayerColor, List<Treasure>> decks = new EnumMap<>(PlayerColor.class);
        int perPlayer = config.treasuresPerPlayer();
        for (int i = 0; i < players.size(); i++) {
            startPositions.put(players.get(i), corners.get(i));
            decks.put(players.get(i), treasures.subList(i * perPlayer, (i + 1) * perPlayer));
        }

        List<PlayerColor> turnOrder = new ArrayList<>(players);
        Collections.shuffle(turnOrder, random);

        return new GameSetup(new Board(rows, cols, tiles, spareTile),
                players, startPositions, decks, turnOrder);
    }

    /**
     * Generates a board with fixed and loose tiles but without treasures and bonuses.
     *
     * @param rows number of rows, odd and between 3 and 15
     * @param cols number of columns, odd and between 3 and 15
     * @param random source of all random decisions
     * @return the generated board including its spare tile
     * @throws IllegalArgumentException if a dimension is not a valid board size
     */
    public static Board generateBoard(int rows, int cols, RandomGenerator random) {
        Objects.requireNonNull(random, "Random must not be null");
        Map<TileShape, Integer> looseCounts = TileDistribution.looseTileCounts(rows, cols);

        Tile[][] tiles = new Tile[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Position position = new Position(row, col);
                if (FixedTileLayout.isFixedPosition(position)) {
                    tiles[row][col] = FixedTileLayout.fixedTileAt(position, rows, cols, random);
                }
            }
        }

        List<Tile> looseTiles = shuffledLooseTiles(looseCounts, random);
        int next = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (tiles[row][col] == null) {
                    tiles[row][col] = looseTiles.get(next++);
                }
            }
        }

        return new Board(rows, cols, tiles, looseTiles.get(next));
    }

    private static List<Position> startCorners(int rows, int cols) {
        return List.of(
                new Position(0, 0),
                new Position(rows - 1, cols - 1),
                new Position(0, cols - 1),
                new Position(rows - 1, 0));
    }

    private static Position position(int index, int cols) {
        return new Position(index / cols, index % cols);
    }

    private static Tile withTreasure(Tile tile, Treasure treasure) {
        return new Tile(tile.shape(), tile.rotation(), tile.fixed(), treasure, tile.bonus());
    }

    private static List<Tile> shuffledLooseTiles(
            Map<TileShape, Integer> looseCounts, RandomGenerator random) {
        List<TileShape> shapes = new ArrayList<>();
        for (TileShape shape : TileShape.values()) {
            shapes.addAll(Collections.nCopies(looseCounts.get(shape), shape));
        }

        Collections.shuffle(shapes, random);

        List<Tile> tiles = new ArrayList<>(shapes.size());
        for (TileShape shape : shapes) {
            tiles.add(new Tile(shape, random.nextInt(ROTATIONS), false, null, null));
        }
        return tiles;
    }
}

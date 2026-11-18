package edu.mci.labyrinth.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Builds the starting board of a game from a random source (Pflichtenheft 6.1).
 *
 * <p>The random values are always drawn in the same order, so the same seed produces the same
 * board (NF-11):
 * <ol>
 *   <li>fixed tiles, row by row: only inner fixed tiles draw a rotation,</li>
 *   <li>loose tiles, created in the order of {@link TileShape}, are shuffled,</li>
 *   <li>each loose tile in shuffled order draws its rotation,</li>
 *   <li>loose tiles fill the free fields row by row; the last one becomes the spare tile.</li>
 * </ol>
 */
public final class BoardGenerator {

    private static final int ROTATIONS = 4;

    private BoardGenerator() {
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

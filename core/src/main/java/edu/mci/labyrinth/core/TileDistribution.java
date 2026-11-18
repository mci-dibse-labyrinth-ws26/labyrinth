package edu.mci.labyrinth.core;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Number of fixed and loose tiles for a board size (Pflichtenheft 6.1, A-03).
 *
 * <p>A board of {@code rows x cols} fields uses {@code rows * cols + 1} tiles: one per field
 * plus the spare tile. Fixed tiles lie on all fields whose row and column are both even. The
 * remaining loose tiles are split into straights, corners and T-junctions in the ratio
 * 12 : 16 : 6 of the original game.
 */
public final class TileDistribution {

    private static final int STRAIGHT_SHARE = 12;
    private static final int CORNER_SHARE = 16;
    private static final int T_JUNCTION_SHARE = 6;
    private static final int TOTAL_SHARE = STRAIGHT_SHARE + CORNER_SHARE + T_JUNCTION_SHARE;

    private TileDistribution() {
    }

    /**
     * Returns the number of fixed tiles, one on every field with an even row and column.
     *
     * @param rows number of rows, odd and between 3 and 15
     * @param cols number of columns, odd and between 3 and 15
     * @return number of fixed tiles
     * @throws IllegalArgumentException if a dimension is not a valid board size
     */
    public static int fixedTileCount(int rows, int cols) {
        requireValidSize(rows);
        requireValidSize(cols);
        return ((rows + 1) / 2) * ((cols + 1) / 2);
    }

    /**
     * Returns the number of loose tiles, including the spare tile.
     *
     * @param rows number of rows, odd and between 3 and 15
     * @param cols number of columns, odd and between 3 and 15
     * @return number of loose tiles
     * @throws IllegalArgumentException if a dimension is not a valid board size
     */
    public static int looseTileCount(int rows, int cols) {
        return rows * cols + 1 - fixedTileCount(rows, cols);
    }

    /**
     * Splits the loose tiles into shapes in the ratio 12 : 16 : 6 (A-03).
     *
     * <p>Uses the largest remainder method: every shape first gets the whole part of its share,
     * then the tiles left over go to the shapes with the largest remainders. On equal remainders
     * the shape declared first in {@link TileShape} wins; no valid board size produces a tie.
     *
     * @param rows number of rows, odd and between 3 and 15
     * @param cols number of columns, odd and between 3 and 15
     * @return unmodifiable map from each shape to its number of loose tiles
     * @throws IllegalArgumentException if a dimension is not a valid board size
     */
    public static Map<TileShape, Integer> looseTileCounts(int rows, int cols) {
        int loose = looseTileCount(rows, cols);

        Map<TileShape, Integer> counts = new EnumMap<>(TileShape.class);
        Map<TileShape, Integer> remainders = new EnumMap<>(TileShape.class);
        int assigned = 0;

        for (TileShape shape : TileShape.values()) {
            int weighted = loose * share(shape);
            counts.put(shape, weighted / TOTAL_SHARE);
            remainders.put(shape, weighted % TOTAL_SHARE);
            assigned += weighted / TOTAL_SHARE;
        }

        for (int left = loose - assigned; left > 0; left--) {
            TileShape largest = null;
            for (TileShape shape : TileShape.values()) {
                if (largest == null || remainders.get(shape) > remainders.get(largest)) {
                    largest = shape;
                }
            }
            counts.merge(largest, 1, Integer::sum);
            remainders.put(largest, -1);
        }

        return Collections.unmodifiableMap(counts);
    }

    private static int share(TileShape shape) {
        return switch (shape) {
            case STRAIGHT -> STRAIGHT_SHARE;
            case CORNER -> CORNER_SHARE;
            case T_JUNCTION -> T_JUNCTION_SHARE;
        };
    }

    private static void requireValidSize(int size) {
        if (size < GameConfig.MIN_BOARD_SIZE || size > GameConfig.MAX_BOARD_SIZE
                || size % 2 == 0) {
            throw new IllegalArgumentException("Board size must be odd and between "
                    + GameConfig.MIN_BOARD_SIZE + " and " + GameConfig.MAX_BOARD_SIZE
                    + ", was " + size);
        }
    }
}

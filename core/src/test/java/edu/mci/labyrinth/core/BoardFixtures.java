package edu.mci.labyrinth.core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Builds boards from a text picture for tests with a fixed starting position.
 *
 * <p>Each character is one field and shows the openings of its tile:
 * <pre>
 *   │ ─          straights
 *   └ ┌ ┐ ┘      corners
 *   ├ ┬ ┤ ┴      T-junctions
 * </pre>
 * Fields with an even row and column become fixed tiles, all others loose tiles. Treasures and
 * bonuses are not placed.
 *
 * <p>Example: a ring around a centre field that is closed towards all four neighbours.
 * <pre>{@code
 * Board board = BoardFixtures.board(
 *         "┌─┐",
 *         "│─│",
 *         "└─┘");
 * }</pre>
 */
final class BoardFixtures {

    private static final Map<Character, Tile> TILES_BY_GLYPH = tilesByGlyph();

    private BoardFixtures() {
    }

    /**
     * Builds a board whose spare tile is a vertical straight.
     *
     * @param rows one string per board row, all of the same length
     * @return the board described by the text
     */
    static Board board(String... rows) {
        return board('│', rows);
    }

    /**
     * Builds a board with the given spare tile.
     *
     * @param spareGlyph character of the spare tile
     * @param rows one string per board row, all of the same length
     * @return the board described by the text
     */
    static Board board(char spareGlyph, String... rows) {
        int cols = rows[0].length();
        Tile[][] tiles = new Tile[rows.length][cols];

        for (int row = 0; row < rows.length; row++) {
            if (rows[row].length() != cols) {
                throw new IllegalArgumentException("Row " + row + " has " + rows[row].length()
                        + " fields, expected " + cols);
            }
            for (int col = 0; col < cols; col++) {
                boolean fixed = FixedTileLayout.isFixedPosition(new Position(row, col));
                tiles[row][col] = tile(rows[row].charAt(col), fixed, "(" + row + ", " + col + ")");
            }
        }

        return new Board(rows.length, cols, tiles, tile(spareGlyph, false, "spare tile"));
    }

    private static Tile tile(char glyph, boolean fixed, String where) {
        Tile tile = TILES_BY_GLYPH.get(glyph);
        if (tile == null) {
            throw new IllegalArgumentException(
                    "Unknown tile '" + glyph + "' at " + where + ", allowed: "
                            + TILES_BY_GLYPH.keySet());
        }
        return new Tile(tile.shape(), tile.rotation(), fixed, null, null);
    }

    private static Map<Character, Tile> tilesByGlyph() {
        Map<Character, Tile> tiles = new HashMap<>();
        for (TileShape shape : TileShape.values()) {
            for (int rotation = 0; rotation < 4; rotation++) {
                Tile tile = new Tile(shape, rotation, false, null, null);
                tiles.putIfAbsent(glyph(tile.openings()), tile);
            }
        }
        return Map.copyOf(tiles);
    }

    private static char glyph(Set<Direction> openings) {
        Set<Direction> open = EnumSet.copyOf(openings);
        boolean up = open.contains(Direction.UP);
        boolean right = open.contains(Direction.RIGHT);
        boolean down = open.contains(Direction.DOWN);
        boolean left = open.contains(Direction.LEFT);

        if (open.size() == 3) {
            return !down ? '┴' : !left ? '├' : !up ? '┬' : '┤';
        }
        if (up && down) {
            return '│';
        }
        if (left && right) {
            return '─';
        }
        if (up) {
            return right ? '└' : '┘';
        }
        return right ? '┌' : '┐';
    }
}

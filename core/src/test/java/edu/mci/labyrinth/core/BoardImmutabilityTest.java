
package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tests that external modifications cannot alter
 * the internal state of a board.
 */
class BoardImmutabilityTest {

    @Test
    @Tag("A-29")
    void modifyingOriginalArrayDoesNotChangeBoard() {

        Tile original = new Tile(
            TileShape.STRAIGHT, 0, false, null, null);

        Tile replacement = new Tile(
            TileShape.CORNER, 0, false, null, null);

        Tile[][] tiles = createTiles(original);

        Board board = new Board(3, 3, tiles, original);

        // Modify the original array after board creation
        tiles[0][0] = replacement;

        // Board must retain its original tile
        assertEquals(original,
            board.tileAt(new Position(0, 0)));
    }

    @Test
    @Tag("A-29")
    void modifyingReturnedArrayDoesNotChangeBoard() {

        Tile original = new Tile(
            TileShape.STRAIGHT, 0, false, null, null);

        Tile replacement = new Tile(
            TileShape.CORNER, 0, false, null, null);

        Board board = new Board(
            3, 3, createTiles(original), original);

        // Attempt to modify the board through its getter
        Tile[][] returnedTiles = board.tiles();
        returnedTiles[0][0] = replacement;

        assertEquals(original,
            board.tileAt(new Position(0, 0)));
    }

    private static Tile[][] createTiles(Tile tile) {
        Tile[][] tiles = new Tile[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                tiles[row][col] = tile;
            }
        }

        return tiles;
    }
}

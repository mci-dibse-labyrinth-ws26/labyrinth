
package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tests structural equality and hashing of boards.
 */
@Tag("A-29")
class BoardEqualityTest {

    private static final Tile TILE =
        new Tile(TileShape.STRAIGHT, 0, false, null, null);

    private Board createBoard() {
        Tile[][] tiles = new Tile[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                tiles[row][col] = TILE;
            }
        }

        return new Board(3, 3, tiles, TILE);
    }

    @Test
    void identicalBoardsAreEqual() {
        Board first = createBoard();
        Board second = createBoard();

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void differentBoardsAreNotEqual() {
        Board first = createBoard();

        Tile[][] tiles = first.tiles();
        tiles[0][0] = new Tile(
            TileShape.CORNER, 0, false, null, null);

        Board second = new Board(3, 3, tiles, TILE);

        assertNotEquals(first, second);
    }

    @Test
    void boardEqualsItself() {
        Board board = createBoard();

        assertEquals(board, board);
    }

    @Test
    void boardDoesNotEqualNullOrOtherTypes() {
        Board board = createBoard();

        assertNotEquals(null, board);
        assertNotEquals("not a board", board);
    }
}

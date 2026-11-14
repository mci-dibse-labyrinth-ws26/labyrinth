
package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tests board dimension validation according to A-01.
 */
@Tag("A-01")
class BoardValidationTest {

    private static final Tile TILE =
        new Tile(TileShape.STRAIGHT, 0, false, null, null);

    @Test
    void acceptsMinimumBoardSize() {
        Board board = createBoard(3, 3);

        assertEquals(3, board.rows());
        assertEquals(3, board.cols());
    }

    @Test
    void acceptsMaximumBoardSize() {
        Board board = createBoard(15, 15);

        assertEquals(15, board.rows());
        assertEquals(15, board.cols());
    }

    @Test
    void acceptsRectangularBoard() {
        Board board = createBoard(5, 9);

        assertEquals(5, board.rows());
        assertEquals(9, board.cols());
    }

    @Test
    void rejectsEvenDimensions() {
        assertThrows(IllegalArgumentException.class,
            () -> createBoard(4, 7));

        assertThrows(IllegalArgumentException.class,
            () -> createBoard(7, 4));
    }

    @Test
    void rejectsDimensionsOutsideAllowedRange() {
        assertThrows(IllegalArgumentException.class,
            () -> createBoard(1, 7));

        assertThrows(IllegalArgumentException.class,
            () -> createBoard(7, 17));
    }

    @Test
    void containsOnlyPositionsInsideBoard() {
        Board board = createBoard(3, 5);

        assertTrue(board.contains(new Position(0, 0)));
        assertTrue(board.contains(new Position(2, 4)));

        assertFalse(board.contains(new Position(3, 0)));
        assertFalse(board.contains(new Position(0, 5)));
    }

    private static Board createBoard(int rows, int cols) {
        Tile[][] tiles = new Tile[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                tiles[row][col] = TILE;
            }
        }

        return new Board(rows, cols, tiles, TILE);
    }
}

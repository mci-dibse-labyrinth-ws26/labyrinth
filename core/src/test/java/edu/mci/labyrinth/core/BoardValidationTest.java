
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

        // Negative coordinates must be outside the board.
        assertFalse(board.contains(new Position(-1, 0)));
        assertFalse(board.contains(new Position(0, -1)));
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

    @Test
    @Tag("V-05")
    void acceptsValidPushIndices() {
        Board board = createPushTestBoard(7, 7);

        for (Direction direction : Direction.values()) {
            for (int index : new int[]{1, 3, 5}) {
                assertDoesNotThrow(() ->
                    RuleValidator.validatePushIndex(
                        board, index, direction));
            }
        }
    }

    @Test
    @Tag("V-05")
    void rejectsInvalidPushIndices() {
        Board board = createPushTestBoard(7, 7);

        for (Direction direction : Direction.values()) {
            for (int index : new int[]{-1, 0, 2, 4, 6, 7}) {
                assertThrows(IllegalArgumentException.class, () ->
                    RuleValidator.validatePushIndex(
                        board, index, direction));
            }
        }
    }

    @Test
    @Tag("V-05")
    void validatesRectangularBoardDimensions() {
        Board board = createPushTestBoard(3, 5);

        // Horizontal pushes use row indices.
        assertDoesNotThrow(() ->
            RuleValidator.validatePushIndex(
                board, 1, Direction.RIGHT));

        assertThrows(IllegalArgumentException.class, () ->
            RuleValidator.validatePushIndex(
                board, 3, Direction.RIGHT));

        // Vertical pushes use column indices.
        assertDoesNotThrow(() ->
            RuleValidator.validatePushIndex(
                board, 3, Direction.DOWN));

        assertThrows(IllegalArgumentException.class, () ->
            RuleValidator.validatePushIndex(
                board, 5, Direction.DOWN));
    }

    private static Board createPushTestBoard(int rows, int cols) {
        Tile tile = new Tile(
            TileShape.STRAIGHT, 0, false, null, null);

        Tile[][] tiles = new Tile[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                tiles[row][col] = tile;
            }
        }

        return new Board(rows, cols, tiles, tile);
    }

    @Test
    @Tag("V-06")
    void acceptsPushWithoutPreviousPush() {
        Push next = new Push(3, Direction.RIGHT);

        assertDoesNotThrow(() ->
            RuleValidator.validateReversePush(null, next));
    }

    @Test
    @Tag("V-06")
    void rejectsImmediateReversePush() {
        for (Direction direction : Direction.values()) {
            Push previous = new Push(3, direction);
            Push reverse = new Push(3, direction.opposite());

            assertThrows(IllegalArgumentException.class, () ->
                RuleValidator.validateReversePush(previous, reverse));
        }
    }

    @Test
    @Tag("V-06")
    void acceptsNonReversingPushes() {
        Push previous = new Push(3, Direction.RIGHT);

        // Same row, same direction.
        assertDoesNotThrow(() ->
            RuleValidator.validateReversePush(
                previous, new Push(3, Direction.RIGHT)));

        // Different row, opposite direction.
        assertDoesNotThrow(() ->
            RuleValidator.validateReversePush(
                previous, new Push(1, Direction.LEFT)));

        // Different axis.
        assertDoesNotThrow(() ->
            RuleValidator.validateReversePush(
                previous, new Push(3, Direction.UP)));
    }

    @Test
    @Tag("V-07")
    void acceptsValidPushRotations() {
        for (int rotation = 0; rotation <= 3; rotation++) {
            int value = rotation;

            assertDoesNotThrow(() ->
                RuleValidator.validatePushRotation(value));
        }
    }

    @Test
    @Tag("V-07")
    void rejectsInvalidPushRotations() {
        for (int rotation : new int[]{-1, 4, 90}) {
            assertThrows(IllegalArgumentException.class, () ->
                RuleValidator.validatePushRotation(rotation));
        }
    }

}

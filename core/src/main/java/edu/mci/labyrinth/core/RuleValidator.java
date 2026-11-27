package edu.mci.labyrinth.core;

/**
 * Checks of the server's check catalog that belong to the game core (Pflichtenheft 6.5).
 *
 * <p>The checks are kept apart from the game logic (Pflichtenheft 4.4), so each one can be
 * tested on its own with full branch coverage.
 */
public final class RuleValidator {

    /** Start corners never hold a treasure (A-04). */
    private static final int START_CORNERS = 4;

    private RuleValidator() {
    }

    /**
     * Checks that a game configuration lies within the allowed limits (V-15).
     *
     * <p>The number of players is the actual number taking part when the game starts (A-36).
     *
     * @param rows number of rows, odd and between 3 and 15 (A-01)
     * @param cols number of columns, odd and between 3 and 15 (A-01)
     * @param playerCount number of players taking part, between 2 and 4
     * @param treasuresPerPlayer number of treasure cards per player, at least 1 (A-04)
     * @throws InvalidConfigException if a value is outside the allowed limits
     */
    public static void validateConfig(int rows, int cols, int playerCount, int treasuresPerPlayer) {
        requireValidBoardSize("rows", rows);
        requireValidBoardSize("cols", cols);

        if (playerCount < GameConfig.MIN_PLAYERS || playerCount > GameConfig.MAX_PLAYERS) {
            throw new InvalidConfigException("playerCount must be between "
                    + GameConfig.MIN_PLAYERS + " and " + GameConfig.MAX_PLAYERS
                    + ", was " + playerCount);
        }

        if (treasuresPerPlayer < 1) {
            throw new InvalidConfigException(
                    "treasuresPerPlayer must be at least 1, was " + treasuresPerPlayer);
        }

        int totalTreasures = playerCount * treasuresPerPlayer;

        if (totalTreasures > GameConfig.MAX_TREASURES) {
            throw new InvalidConfigException("playerCount * treasuresPerPlayer must not exceed "
                    + GameConfig.MAX_TREASURES + ", was " + totalTreasures);
        }

        // All tiles including the spare tile, except the start corners: rows * cols - 3.
        int treasureTiles = rows * cols + 1 - START_CORNERS;

        if (totalTreasures > treasureTiles) {
            throw new InvalidConfigException("playerCount * treasuresPerPlayer must not exceed "
                    + "rows * cols - 3 = " + treasureTiles + ", was " + totalTreasures);
        }
    }

    private static void requireValidBoardSize(String name, int size) {
        if (!GameConfig.isValidBoardSize(size)) {
            throw new InvalidConfigException(name + " must be odd and between "
                    + GameConfig.MIN_BOARD_SIZE + " and " + GameConfig.MAX_BOARD_SIZE
                    + ", was " + size);
        }
    }

    /**
     * Validates whether a row or column can be pushed (V-05).
     *
     * Normal pushes are only allowed on odd indices
     * inside the board boundaries.
     *
     * @param board the current game board
     * @param index the row or column index to push
     * @param direction the pushing direction
     * @throws IllegalArgumentException if the push index is invalid
     */
    public static void validatePushIndex(
        Board board, int index, Direction direction) {

        java.util.Objects.requireNonNull(board, "Board must not be null");
        java.util.Objects.requireNonNull(direction, "Direction must not be null");

        int size = switch (direction) {
            case LEFT, RIGHT -> board.rows();
            case UP, DOWN -> board.cols();
        };

        if (index <= 0 || index >= size - 1 || index % 2 == 0) {
            throw new IllegalArgumentException(
                "Invalid push index: " + index);
        }
    }

    /**
     * Validates that a push does not reverse the previous push (V-06).
     *
     * <p>The opposite direction on the same row or column
     * is forbidden for the next push.</p>
     *
     * @param previousPush the previous push, or null if none exists
     * @param nextPush the proposed push
     * @throws IllegalArgumentException if the push reverses the previous one
     * @throws NullPointerException if nextPush is null
     */
    public static void validateReversePush(
        Push previousPush, Push nextPush) {

        java.util.Objects.requireNonNull(
            nextPush, "Next push must not be null");

        if (previousPush != null && previousPush.isReversedBy(nextPush)) {
            throw new IllegalArgumentException(
                "Reversing the previous push is forbidden");
        }
    }

    /**
     * Validates the absolute tile rotation (V-07).
     *
     * @param rotation the absolute orientation in clockwise quarter-turns
     * @throws IllegalArgumentException if rotation is outside 0 to 3
     */
    public static void validatePushRotation(int rotation) {
        if (rotation < 0 || rotation > 3) {
            throw new IllegalArgumentException(
                "Push rotation must be between 0 and 3");
        }
    }
}

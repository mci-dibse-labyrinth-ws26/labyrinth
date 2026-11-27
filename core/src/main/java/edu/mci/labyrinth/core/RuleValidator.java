package edu.mci.labyrinth.core;

import java.util.Map;
import java.util.Objects;

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

    /**
     * Checks that a figure may move to the target field and returns the steps it takes.
     *
     * <p>The target must lie on the board and be reachable over connected tiles (V-08), and no
     * other figure may stand on it (V-09). Staying on the own field is allowed (A-10), and the
     * path may pass fields occupied by other figures (A-12). The steps are the length of the
     * shortest path (A-11).
     *
     * @param board the board after the push
     * @param playerPositions positions of all figures still on the board
     * @param player the player who moves
     * @param target the requested target field
     * @return number of steps from the current field to the target
     * @throws TargetUnreachableException if the target is outside the board or not reachable
     * @throws PositionOccupiedException if another figure stands on the target
     * @throws IllegalArgumentException if {@code player} has no position
     */
    public static int validateMoveTarget(Board board, Map<PlayerColor, Position> playerPositions,
            PlayerColor player, Position target) {
        Objects.requireNonNull(board, "Board must not be null");
        Objects.requireNonNull(playerPositions, "Player positions must not be null");
        Objects.requireNonNull(player, "Player must not be null");
        Objects.requireNonNull(target, "Target must not be null");

        Position start = playerPositions.get(player);
        if (start == null) {
            throw new IllegalArgumentException("Player " + player + " has no position");
        }

        if (!board.contains(target)) {
            throw new TargetUnreachableException("Target " + target + " is outside the board");
        }

        Integer steps = PathFinder.distancesFrom(board, start).get(target);
        if (steps == null) {
            throw new TargetUnreachableException(
                    "Target " + target + " cannot be reached from " + start);
        }

        for (Map.Entry<PlayerColor, Position> figure : playerPositions.entrySet()) {
            if (figure.getKey() != player && figure.getValue().equals(target)) {
                throw new PositionOccupiedException(
                        "Target " + target + " is occupied by " + figure.getKey());
            }
        }

        return steps;
    }

    private static void requireValidBoardSize(String name, int size) {
        if (!GameConfig.isValidBoardSize(size)) {
            throw new InvalidConfigException(name + " must be odd and between "
                    + GameConfig.MIN_BOARD_SIZE + " and " + GameConfig.MAX_BOARD_SIZE
                    + ", was " + size);
        }
    }
}

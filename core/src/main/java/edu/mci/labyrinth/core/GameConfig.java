package edu.mci.labyrinth.core;

/**
 * Configuration of a single game: board size, number of players and number
 * of treasures per player.
 *
 * <p>A configuration is validated on creation (V-15), so every instance
 * describes a game that can be set up.
 *
 * @param rows number of rows, odd and between 3 and 15 (A-01)
 * @param cols number of columns, odd and between 3 and 15 (A-01)
 * @param playerCount number of players taking part, between 2 and 4
 * @param treasuresPerPlayer number of treasure cards per player, at least 1 (A-04)
 */
public record GameConfig(int rows, int cols, int playerCount, int treasuresPerPlayer) {

    /** Smallest allowed number of rows or columns (A-01). */
    public static final int MIN_BOARD_SIZE = 3;

    /** Largest allowed number of rows or columns (A-01). */
    public static final int MAX_BOARD_SIZE = 15;

    /** Smallest number of players for a game. */
    public static final int MIN_PLAYERS = 2;

    /** Largest number of players for a game. */
    public static final int MAX_PLAYERS = 4;

    /** Largest number of treasures in a game, one per treasure symbol (A-04). */
    public static final int MAX_TREASURES = Treasure.NAMES.size();

    /** Start corners never hold a treasure (A-04). */
    private static final int START_CORNERS = 4;

    /**
     * Validates the configuration.
     *
     * @throws InvalidConfigException if a value is outside the allowed limits
     */
    public GameConfig {
        requireValidBoardSize("rows", rows);
        requireValidBoardSize("cols", cols);

        if (playerCount < MIN_PLAYERS || playerCount > MAX_PLAYERS) {
            throw new InvalidConfigException("playerCount must be between "
                    + MIN_PLAYERS + " and " + MAX_PLAYERS + ", was " + playerCount);
        }

        if (treasuresPerPlayer < 1) {
            throw new InvalidConfigException(
                    "treasuresPerPlayer must be at least 1, was " + treasuresPerPlayer);
        }

        int totalTreasures = playerCount * treasuresPerPlayer;

        if (totalTreasures > MAX_TREASURES) {
            throw new InvalidConfigException("playerCount * treasuresPerPlayer must not exceed "
                    + MAX_TREASURES + ", was " + totalTreasures);
        }

        // All tiles including the spare tile, except the start corners: rows * cols - 3.
        int treasureTiles = rows * cols + 1 - START_CORNERS;

        if (totalTreasures > treasureTiles) {
            throw new InvalidConfigException("playerCount * treasuresPerPlayer must not exceed "
                    + "rows * cols - 3 = " + treasureTiles + ", was " + totalTreasures);
        }
    }

    /**
     * Returns the total number of treasures in the game.
     *
     * @return number of players multiplied by treasures per player
     */
    public int totalTreasures() {
        return playerCount * treasuresPerPlayer;
    }

    private static void requireValidBoardSize(String name, int size) {
        if (size < MIN_BOARD_SIZE || size > MAX_BOARD_SIZE || size % 2 == 0) {
            throw new InvalidConfigException(name + " must be odd and between "
                    + MIN_BOARD_SIZE + " and " + MAX_BOARD_SIZE + ", was " + size);
        }
    }
}

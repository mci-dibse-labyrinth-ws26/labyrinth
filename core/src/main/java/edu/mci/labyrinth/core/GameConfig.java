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

    /**
     * Validates the configuration with {@link RuleValidator#validateConfig}.
     *
     * @throws InvalidConfigException if a value is outside the allowed limits
     */
    public GameConfig {
        RuleValidator.validateConfig(rows, cols, playerCount, treasuresPerPlayer);
    }

    /**
     * Checks whether a number of rows or columns is allowed (A-01).
     *
     * @param size number of rows or columns
     * @return {@code true} if the size is odd and between {@link #MIN_BOARD_SIZE} and
     *     {@link #MAX_BOARD_SIZE}
     */
    public static boolean isValidBoardSize(int size) {
        return size >= MIN_BOARD_SIZE && size <= MAX_BOARD_SIZE && size % 2 == 1;
    }

    /**
     * Returns the total number of treasures in the game.
     *
     * @return number of players multiplied by treasures per player
     */
    public int totalTreasures() {
        return playerCount * treasuresPerPlayer;
    }
}

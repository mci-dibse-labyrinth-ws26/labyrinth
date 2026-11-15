package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Tag("V-15")
class GameConfigTest {

    @ParameterizedTest(name = "{0} x {1}, {2} players, {3} treasures each")
    @CsvSource({
        "7, 7, 4, 6",
        "3, 3, 2, 1",
        "15, 15, 4, 6",
        "5, 9, 3, 4",
        "7, 7, 2, 12",
        "3, 3, 2, 3",
        "5, 5, 2, 11",
    })
    void acceptsConfigurationWithinLimits(int rows, int cols, int players, int treasures) {
        GameConfig config = new GameConfig(rows, cols, players, treasures);

        assertEquals(players * treasures, config.totalTreasures());
    }

    @ParameterizedTest(name = "{0} x {1}")
    @CsvSource({
        "1, 7",
        "7, 1",
        "17, 7",
        "7, 17",
        "6, 7",
        "7, 6",
        "-7, 7",
    })
    void rejectsInvalidBoardSize(int rows, int cols) {
        assertThrows(InvalidConfigException.class, () -> new GameConfig(rows, cols, 2, 6));
    }

    @ParameterizedTest(name = "{0} players")
    @CsvSource({"0", "1", "5"})
    void rejectsPlayerCountOutsideTwoToFour(int players) {
        assertThrows(InvalidConfigException.class, () -> new GameConfig(7, 7, players, 1));
    }

    @ParameterizedTest(name = "{0} treasures each")
    @CsvSource({"0", "-1"})
    void rejectsLessThanOneTreasurePerPlayer(int treasures) {
        assertThrows(InvalidConfigException.class, () -> new GameConfig(7, 7, 2, treasures));
    }

    @Test
    void rejectsMoreThanTwentyFourTreasuresInTotal() {
        assertThrows(InvalidConfigException.class, () -> new GameConfig(15, 15, 4, 7));
    }

    @Test
    void rejectsMoreTreasuresThanTilesOutsideStartCorners() {
        assertThrows(InvalidConfigException.class, () -> new GameConfig(3, 3, 2, 4));
        assertThrows(InvalidConfigException.class, () -> new GameConfig(5, 5, 2, 12));
    }

    @Test
    void invalidConfigIsAnIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new GameConfig(4, 7, 2, 6));
    }
}

package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Tag("V-15")
class RuleValidatorTest {

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
        assertDoesNotThrow(() -> RuleValidator.validateConfig(rows, cols, players, treasures));
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
        assertThrows(InvalidConfigException.class,
                () -> RuleValidator.validateConfig(rows, cols, 2, 6));
    }

    @ParameterizedTest(name = "{0} players")
    @CsvSource({"0", "1", "5"})
    void rejectsPlayerCountOutsideTwoToFour(int players) {
        assertThrows(InvalidConfigException.class,
                () -> RuleValidator.validateConfig(7, 7, players, 1));
    }

    @ParameterizedTest(name = "{0} treasures each")
    @CsvSource({"0", "-1"})
    void rejectsLessThanOneTreasurePerPlayer(int treasures) {
        assertThrows(InvalidConfigException.class,
                () -> RuleValidator.validateConfig(7, 7, 2, treasures));
    }

    @Test
    void rejectsMoreThanTwentyFourTreasuresInTotal() {
        assertThrows(InvalidConfigException.class,
                () -> RuleValidator.validateConfig(15, 15, 4, 7));
    }

    @Test
    void rejectsMoreTreasuresThanTilesOutsideStartCorners() {
        assertThrows(InvalidConfigException.class,
                () -> RuleValidator.validateConfig(3, 3, 2, 4));
        assertThrows(InvalidConfigException.class,
                () -> RuleValidator.validateConfig(5, 5, 2, 12));
    }

    @Test
    void invalidConfigIsAnIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> RuleValidator.validateConfig(4, 7, 2, 6));
    }
}

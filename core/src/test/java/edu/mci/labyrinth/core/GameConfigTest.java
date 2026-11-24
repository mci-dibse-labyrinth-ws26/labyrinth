package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Tag("V-15")
class GameConfigTest {

    @Test
    void keepsValuesOfValidConfiguration() {
        GameConfig config = new GameConfig(5, 9, 3, 4);

        assertEquals(5, config.rows());
        assertEquals(9, config.cols());
        assertEquals(3, config.playerCount());
        assertEquals(4, config.treasuresPerPlayer());
        assertEquals(12, config.totalTreasures());
    }

    @ParameterizedTest(name = "{0}: {1}")
    @CsvSource({"3, true", "7, true", "15, true", "1, false", "17, false", "6, false",
        "-3, false"})
    @Tag("A-01")
    void checksBoardSize(int size, boolean valid) {
        assertEquals(valid, GameConfig.isValidBoardSize(size));
    }

    @Test
    void cannotBeCreatedWithInvalidValues() {
        assertThrows(InvalidConfigException.class, () -> new GameConfig(4, 7, 2, 6));
        assertThrows(InvalidConfigException.class, () -> new GameConfig(7, 7, 5, 1));
        assertThrows(InvalidConfigException.class, () -> new GameConfig(3, 3, 2, 4));
    }
}

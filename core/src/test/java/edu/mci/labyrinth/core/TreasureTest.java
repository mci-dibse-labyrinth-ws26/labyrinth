
package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tests treasure identifiers and names according to A-34.
 */
@Tag("A-34")
class TreasureTest {

    @Test
    void containsExactly24Treasures() {
        assertEquals(24, Treasure.NAMES.size());

        for (int id = 0; id < 24; id++) {
            Treasure treasure = Treasure.of(id);

            assertEquals(id, treasure.id());
            assertEquals(Treasure.NAMES.get(id), treasure.name());
        }
    }

    @Test
    void firstAndLastTreasureHaveCorrectNames() {
        assertEquals("Schlüssel", Treasure.of(0).name());
        assertEquals("Glocke", Treasure.of(23).name());
    }

    @Test
    void rejectsInvalidTreasureIds() {
        assertThrows(IllegalArgumentException.class,
            () -> Treasure.of(-1));

        assertThrows(IllegalArgumentException.class,
            () -> Treasure.of(24));
    }

    @Test
    void rejectsIncorrectTreasureName() {
        assertThrows(IllegalArgumentException.class,
            () -> new Treasure(0, "Glocke"));
    }
}

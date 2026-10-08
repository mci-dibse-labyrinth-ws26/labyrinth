package edu.mci.labyrinth.protocol;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class ProtocolVersionTest {

    @Test
    void currentVersionIsSet() {
        assertFalse(ProtocolVersion.CURRENT.isBlank());
    }
}

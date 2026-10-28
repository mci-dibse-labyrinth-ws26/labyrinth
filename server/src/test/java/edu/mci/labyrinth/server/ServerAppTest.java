package edu.mci.labyrinth.server;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ServerAppTest {

    @Test
    void bannerNamesTheServer() {
        assertTrue(ServerApp.banner().startsWith("Labyrinth-Server"));
    }

    @Test
    @Tag("F-237")
    void recognizesHeadlessOption() {
        assertTrue(ServerApp.isHeadless(new String[] {"--headless"}));
    }

    @Test
    @Tag("F-237")
    void isNotHeadlessWithoutOptions() {
        assertFalse(ServerApp.isHeadless(new String[0]));
    }
}

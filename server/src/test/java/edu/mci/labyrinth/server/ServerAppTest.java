package edu.mci.labyrinth.server;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ServerAppTest {

    @Test
    void bannerNamesTheServer() {
        assertTrue(ServerApp.banner().startsWith("Labyrinth-Server"));
    }
}

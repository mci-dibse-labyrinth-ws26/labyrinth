package edu.mci.labyrinth.client;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ClientAppTest {

    @Test
    void statusTextNamesTheClient() {
        assertTrue(ClientApp.statusText().startsWith("Labyrinth-Client"));
    }
}

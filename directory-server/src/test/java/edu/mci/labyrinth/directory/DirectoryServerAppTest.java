package edu.mci.labyrinth.directory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DirectoryServerAppTest {

    @Test
    void bannerNamesTheDirectoryServer() {
        assertTrue(DirectoryServerApp.banner().startsWith("Labyrinth-Verzeichnisserver"));
    }
}

package edu.mci.labyrinth.directory;

import edu.mci.labyrinth.protocol.ProtocolVersion;

/**
 * Startpunkt des Verzeichnisservers.
 *
 * <p>Platzhalter aus dem Projektgerüst. Zeigt, dass der Verzeichnisserver das
 * Modul {@code protocol} benutzen kann.
 */
public final class DirectoryServerApp {

    private DirectoryServerApp() {
    }

    public static void main(String[] args) {
        System.out.println(banner());
    }

    static String banner() {
        return "Labyrinth-Verzeichnisserver (Protokoll " + ProtocolVersion.CURRENT + ")";
    }
}

package edu.mci.labyrinth.server;

import edu.mci.labyrinth.core.Board;
import edu.mci.labyrinth.protocol.ProtocolVersion;

/**
 * Startpunkt des Spielservers.
 *
 * <p>Platzhalter aus dem Projektgerüst. Zeigt, dass der Server die Module
 * {@code protocol} und {@code core} benutzen kann.
 */
public final class ServerApp {

    private ServerApp() {
    }

    public static void main(String[] args) {
        System.out.println(banner());
    }

    static String banner() {
        return "Labyrinth-Server (Protokoll " + ProtocolVersion.CURRENT
                + ", Spielbrett " + Board.SIZE + "x" + Board.SIZE + ")";
    }
}

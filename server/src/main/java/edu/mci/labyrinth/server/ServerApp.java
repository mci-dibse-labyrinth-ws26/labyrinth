package edu.mci.labyrinth.server;

import edu.mci.labyrinth.core.Board;
import edu.mci.labyrinth.protocol.ProtocolVersion;
import java.util.Arrays;
import javafx.application.Application;

/**
 * Entry point of the game server.
 *
 * <p>Opens the server window by default. With the command line option
 * {@code --headless} the server runs without a window (S-04, F-237).
 */
public final class ServerApp {

    static final String HEADLESS_OPTION = "--headless";

    private ServerApp() {
    }

    /**
     * Starts the game server.
     *
     * @param args command line arguments; {@code --headless} starts the
     *             server without a window
     */
    public static void main(String[] args) {
        if (isHeadless(args)) {
            System.out.println(banner());
        } else {
            Application.launch(ServerWindow.class, args);
        }
    }

    static boolean isHeadless(String[] args) {
        return Arrays.asList(args).contains(HEADLESS_OPTION);
    }

    static String banner() {
        return "Labyrinth-Server (Protokoll " + ProtocolVersion.CURRENT
                + ", Spielbrett " + Board.DEFAULT_SIZE + "x" + Board.DEFAULT_SIZE + ")";
    }
}

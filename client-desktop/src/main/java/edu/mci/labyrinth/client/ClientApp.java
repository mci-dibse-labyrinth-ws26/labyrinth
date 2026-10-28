package edu.mci.labyrinth.client;

import edu.mci.labyrinth.core.Board;
import edu.mci.labyrinth.protocol.ProtocolVersion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Entry point of the desktop client.
 */
public class ClientApp extends Application {

    /**
     * Creates the client application. Called by the JavaFX launcher.
     */
    public ClientApp() {
    }

    /**
     * Starts the desktop client.
     *
     * @param args command line arguments, passed on to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Labyrinth");
        stage.setScene(new Scene(new StackPane(new Label(statusText())), 480, 320));
        stage.show();
    }

    static String statusText() {
        return "Labyrinth-Client (Protokoll " + ProtocolVersion.CURRENT
                + ", Spielbrett " + Board.DEFAULT_SIZE + "x" + Board.DEFAULT_SIZE + ")";
    }
}

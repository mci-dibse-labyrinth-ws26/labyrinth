package edu.mci.labyrinth.client;

import edu.mci.labyrinth.core.Board;
import edu.mci.labyrinth.protocol.ProtocolVersion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Startpunkt des Desktop-Clients.
 *
 * <p>Platzhalter aus dem Projektgerüst. Öffnet ein leeres Fenster und zeigt,
 * dass der Client JavaFX sowie die Module {@code protocol} und {@code core}
 * benutzen kann.
 */
public class ClientApp extends Application {

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
                + ", Spielbrett " + Board.SIZE + "x" + Board.SIZE + ")";
    }
}

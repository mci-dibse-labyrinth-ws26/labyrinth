package edu.mci.labyrinth.server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main window of the game server (M-01).
 */
public final class ServerWindow extends Application {

    /**
     * Creates the server window. Called by the JavaFX launcher.
     */
    public ServerWindow() {
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Labyrinth-Server");
        stage.setScene(new Scene(new StackPane(new Label(ServerApp.banner())), 640, 400));
        stage.show();
    }
}

package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.mci.labyrinth.protocol.message.Message;
import edu.mci.labyrinth.protocol.model.Coordinates;

/**
 * A client command for moving the player's figure.
 * @param data movement payload
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MoveFigureMessage(Data data) implements Message {

    /**
     * The requested movement.
     * @param targetCoordinates target board position
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(Coordinates targetCoordinates) {
    }
}

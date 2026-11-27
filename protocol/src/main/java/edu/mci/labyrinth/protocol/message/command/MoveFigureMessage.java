package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.mci.labyrinth.protocol.message.Message;
import edu.mci.labyrinth.protocol.model.Coordinates;

/**
 * A client command for moving the player's figure.
 * @param data movement payload
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MoveFigureMessage(
    @JsonProperty(value = "data", required = true)
    Data data)
    implements Message {
    /**
     * Validates the message payload.
     * @throws IllegalArgumentException if data is null
     */
    public MoveFigureMessage {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
    }

    /**
     * The requested movement.
     * @param targetCoordinates target board position
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
        @JsonProperty(value = "targetCoordinates", required = true)
        Coordinates targetCoordinates) {
        /**
         * Validates the movement payload.
         * @throws IllegalArgumentException if targetCoordinates is null
         */
        public Data {
            if (targetCoordinates == null) {
                throw new IllegalArgumentException(
                    "targetCoordinates must not be null"
                );
            }
        }
    }
}

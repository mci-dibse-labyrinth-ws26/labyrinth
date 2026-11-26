package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.mci.labyrinth.protocol.message.Message;
import edu.mci.labyrinth.protocol.model.Push;

/**
 * A client command for inserting the spare tile.
 * @param data push parameters
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PushTileMessage(
    @JsonProperty(value = "data", required = true)
    Push data
) implements Message {

    /**
     * Validates the message payload.
     *
     * @throws IllegalArgumentException if data is null
     */
    public PushTileMessage {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
    }
}

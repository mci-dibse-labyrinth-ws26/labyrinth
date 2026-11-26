package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.mci.labyrinth.protocol.message.Message;

/**
 * A client command for setting rematch readiness.
 * @param data rematch payload
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RematchRequestMessage(
    @JsonProperty(value = "data", required = true)
    Data data) implements Message {

    /**
     * Validates the message payload.
     * @throws IllegalArgumentException if data is null
     */
    public RematchRequestMessage {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
    }

    /**
     * The requested rematch readiness.
     * @param ready whether the player is ready for a rematch
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
        @JsonProperty(value = "ready", required = true)
        boolean ready) {
    }
}

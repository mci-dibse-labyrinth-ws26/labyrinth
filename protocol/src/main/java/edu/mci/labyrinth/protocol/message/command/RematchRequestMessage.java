package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.mci.labyrinth.protocol.message.Message;

/**
 * A client command for setting rematch readiness.
 * @param data rematch payload
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RematchRequestMessage(Data data) implements Message {

    /**
     * The requested rematch readiness.
     * @param ready whether the player is ready for a rematch
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(boolean ready) {
    }
}

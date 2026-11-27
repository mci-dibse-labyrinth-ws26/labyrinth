package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.mci.labyrinth.protocol.message.Message;

/**
 * A client command for enabling or disabling client-side AI control.
 * @param data AI configuration
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SetAiModeMessage(
    @JsonProperty(value = "data", required = true)
    Data data)
    implements Message {
    /**
     * Validates the message payload.
     * @throws IllegalArgumentException if data is null
     */
    public SetAiModeMessage {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
    }

    /**
     * The requested AI mode.
     * @param enabled whether client-side AI control is enabled
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
        @JsonProperty(value = "enabled", required = true)
        boolean enabled) {
    }
}

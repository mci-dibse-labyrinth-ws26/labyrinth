package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.mci.labyrinth.protocol.message.Message;

/**
 * A client command for enabling or disabling client-side AI control.
 * @param data AI configuration
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SetAiModeMessage(Data data) implements Message {

    /**
     * The requested AI mode.
     * @param enabled whether client-side AI control is enabled
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(boolean enabled) {
    }
}

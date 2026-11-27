package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.mci.labyrinth.protocol.message.Message;

/**
 * A client command for skipping the bonus phase or ending a turn.
 * @param data empty payload
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PassMoveMessage(
    @JsonProperty(value = "data", required = true)
    Data data
) implements Message {

    /**
     * Validates the message payload.
     * @throws IllegalArgumentException if data is null
     */
    public PassMoveMessage {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
    }

    /**
     * An explicitly empty payload.
     */
    public record Data() {

        /**
         * Rejects fields because this payload must be empty.
         * @param name unexpected field name
         * @param value unexpected field value
         * @throws IllegalArgumentException if a field is provided
         */
        @JsonAnySetter
        public void rejectField(String name, Object value) {
            throw new IllegalArgumentException(
                "PASS_MOVE data must be empty"
            );
        }
    }
}

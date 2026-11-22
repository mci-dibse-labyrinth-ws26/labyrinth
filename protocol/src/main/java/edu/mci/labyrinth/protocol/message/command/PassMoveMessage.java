package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.mci.labyrinth.protocol.message.Message;

/**
 * A client command for skipping the bonus phase or ending a turn.
 * @param data empty payload
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PassMoveMessage(Data data) implements Message {

    /**
     * An explicitly empty payload.
     */
    public record Data() {

        /**
         * Rejects fields because this payload must be empty.
         * @param name unexpected field name
         * @param value unexpected field value
         */
        @JsonAnySetter
        public void rejectField(String name, Object value) {
            throw new IllegalArgumentException(
                "PASS_MOVE data must be empty"
            );
        }
    }
}

package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.mci.labyrinth.protocol.message.Message;

/**
 * A client command for joining a lobby or reconnecting to a session.
 * @param data connection payload
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ConnectMessage(
    @JsonProperty(value = "data", required = true)
    Data data) implements Message {

    /**
     * Validates the message payload.
     * @throws IllegalArgumentException if data is null
     */
    public ConnectMessage {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
    }

    /**
     * The connection payload.
     * @param username player name containing between 1 and 50 characters
     * @param identifierToken session token for reconnecting,or null when joining
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
        @JsonProperty(value = "username", required = true)
        String username,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String identifierToken
    ) {

        /**
         * Validates the username required by the protocol
         * @throws IllegalArgumentException if the username is missing or its length is invalid
         */
        public Data {
            if (username == null
                || username.codePointCount(0, username.length()) < 1
                || username.codePointCount(0, username.length()) > 50) {
                throw new IllegalArgumentException(
                    "username must contain between 1 and 50 characters"
                );
            }
        }
    }
}

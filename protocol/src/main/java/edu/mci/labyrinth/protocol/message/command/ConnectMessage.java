package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import edu.mci.labyrinth.protocol.message.Message;

/**
 * A client command for joining a lobby or reconnecting to a session.
 * @param data connection payload
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ConnectMessage(Data data) implements Message {

    /**
     * The connection payload.
     * @param username player name
     * @param identifierToken session token for reconnecting, or null when joining
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
        String username,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String identifierToken
    ) {


        /**
         * Validates the username required by the protocol.
         * @throws IllegalArgumentException if the username is missing
         * or its length is invalid
         */
        public Data
        {
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

package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.mci.labyrinth.protocol.message.Message;

/**
 * A client command for configuring and starting a game.
 * @param data game configuration
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StartGameMessage(
    @JsonProperty(value = "data", required = true)
    Data data
) implements Message {

    /**
     * Validates the message payload.
     * @throws IllegalArgumentException if data is null
     */
    public StartGameMessage {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
    }

    /**
     * The requested game configuration.
     * @param boardSize requested board dimensions
     * @param maxPlayers player limit, or null when omitted
     * @param treasureCardCount treasures per player, or null when omitted
     * @param gameDurationInSeconds game duration, or null when omitted
     * @param turnTimeoutInSeconds turn timeout, or null when omitted
     * @param bonusSpawnInterval round interval, or null when omitted
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Data(
        @JsonProperty(value = "boardSize", required = true)
        BoardSize boardSize,

        Integer maxPlayers,
        Integer treasureCardCount,
        Integer gameDurationInSeconds,
        Integer turnTimeoutInSeconds,
        Integer bonusSpawnInterval
    ) {

        /**
         * Validates the required board size.
         *
         * @throws IllegalArgumentException if boardSize is null
         */
        public Data {
            if (boardSize == null) {
                throw new IllegalArgumentException(
                    "boardSize must not be null"
                );
            }
        }
    }

    /**
     * The requested board dimensions.
     * @param rows number of rows; odd and at least three
     * @param cols number of columns; odd and at least three
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BoardSize(
        @JsonProperty(value = "rows", required = true)
        int rows,

        @JsonProperty(value = "cols", required = true)
        int cols
    ) {
    }
}

package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import edu.mci.labyrinth.protocol.message.Message;

/**
 * A client command for configuring and starting a game.
 * @param data game configuration
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StartGameMessage(Data data) implements Message {

    /**
     * The requested game configuration.
     * @param boardSize requested board dimensions
     * @param maxPlayers player limit, or null when omitted
     * @param treasureCardCount treasures per player, or null when omitted
     * @param gameDurationInSeconds game duration; zero means unlimited,null means omitted
     * @param turnTimeoutInSeconds turn timeout, or null when omitted
     * @param bonusSpawnInterval round interval, or null when omitted
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Data(
        BoardSize boardSize,
        Integer maxPlayers,
        Integer treasureCardCount,
        Integer gameDurationInSeconds,
        Integer turnTimeoutInSeconds,
        Integer bonusSpawnInterval
    ) {
    }

    /**
     * The requested board dimensions.
     * @param rows number of rows; must be odd and at least three
     * @param cols number of columns; must be odd and at least three
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BoardSize(int rows, int cols) {
    }
}

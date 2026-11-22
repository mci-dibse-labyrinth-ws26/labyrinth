package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.mci.labyrinth.protocol.message.Message;
import edu.mci.labyrinth.protocol.model.Direction;

/**
 * A client command for inserting the spare tile.
 * @param data push parameters
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PushTileMessage(Data data) implements Message {

    /**
     * The requested push.
     * @param rowIndex row of the insertion position
     * @param columnIndex column of the insertion position
     * @param rotation absolute clockwise orientation from zero to three
     * @param direction direction of movement into the board
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
        int rowIndex,
        int columnIndex,
        int rotation,
        Direction direction
    ) {
    }
}

package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
        @JsonProperty(value = "rowIndex", required = true)
        int rowIndex,
        @JsonProperty(value = "columnIndex", required = true)
        int columnIndex,
        @JsonProperty(value = "rotation", required = true)
        int rotation,
        @JsonProperty(value = "direction", required = true)
        Direction direction
    ) {
        /**
         * Validates the push parameters.
         * @throws IllegalArgumentException
         * if coordinates are negative, rotation is invalid, or direction is null
         */
        public Data {
            if (rowIndex < 0 || columnIndex < 0) {
                throw new IllegalArgumentException(
                    "rowIndex and columnIndex must not be negative"
                );
            }

            if (rotation < 0 || rotation > 3) {
                throw new IllegalArgumentException(
                    "rotation must be between 0 and 3"
                );
            }

            if (direction == null) {
                throw new IllegalArgumentException(
                    "direction must not be null"
                );
            }
        }
    }
}

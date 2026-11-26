package edu.mci.labyrinth.protocol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Shared push parameters for commands and board state.
 * @param rowIndex row of the insertion position
 * @param columnIndex column of the insertion position
 * @param rotation absolute clockwise orientation from zero to three
 * @param direction direction of movement into the board
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Push(
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
     * Validates the shared push parameters.
     * @throws IllegalArgumentException if coordinates are negative,
     *                                  rotation is invalid, or direction is null
     */
    public Push {
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
            throw new IllegalArgumentException("direction must not be null");
        }
    }
}

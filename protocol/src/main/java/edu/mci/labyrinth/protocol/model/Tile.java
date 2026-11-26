package edu.mci.labyrinth.protocol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A tile represented in the shared protocol.
 * @param entrances actual open directions of the tile
 * @param rotation absolute clockwise orientation from zero to three
 * @param treasure treasure on the tile, or null
 * @param bonus bonus on the tile, or null
 * @param isFixed whether the tile is fixed
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Tile(
    @JsonProperty(value = "entrances", required = true)
    List<Direction> entrances,

    @JsonProperty(value = "rotation", required = true)
    int rotation,

    @JsonProperty(value = "treasure", required = true)
    Treasure treasure,

    @JsonProperty(value = "bonus", required = true)
    BonusType bonus,

    @JsonProperty(value = "isFixed", required = true)
    boolean isFixed
) {

    /**
     * Validates and defensively copies the tile properties.
     * @throws IllegalArgumentException if entrances is null, contains null or duplicate directions,
     * rotation is invalid, or treasure and bonus are both present
     */
    public Tile {
        if (entrances == null) {
            throw new IllegalArgumentException(
                "entrances must not be null"
            );
        }

        if (entrances.stream().anyMatch(direction -> direction == null)) {
            throw new IllegalArgumentException(
                "entrances must not contain null"
            );
        }

        if (entrances.stream().distinct().count() != entrances.size()) {
            throw new IllegalArgumentException(
                "entrances must not contain duplicate directions"
            );
        }

        if (rotation < 0 || rotation > 3) {
            throw new IllegalArgumentException(
                "rotation must be between 0 and 3"
            );
        }

        if (treasure != null && bonus != null) {
            throw new IllegalArgumentException(
                "A tile must not contain both a treasure and a bonus"
            );
        }

        entrances = List.copyOf(entrances);
    }
}

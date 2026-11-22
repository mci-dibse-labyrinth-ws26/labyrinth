package edu.mci.labyrinth.protocol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A tile represented in the shared protocol.
 *
 * @param entrances actual open directions of the tile
 * @param rotation absolute clockwise orientation:
 *                 0 = 0 degrees, 1 = 90 degrees,
 *                 2 = 180 degrees, 3 = 270 degrees
 * @param treasure treasure on the tile, or null
 * @param bonus bonus on the tile, or null
 * @param isFixed whether the tile is fixed
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Tile(
    List<Direction> entrances,
    int rotation,
    Treasure treasure,
    BonusType bonus,
    @JsonProperty("isFixed") boolean isFixed
) {
}

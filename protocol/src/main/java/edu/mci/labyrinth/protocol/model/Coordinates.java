package edu.mci.labyrinth.protocol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * A board position with its origin at the top-left corner.
 * 0/0 -> position top-left!
 * @param row zero-based row index
 * @param column zero-based column index
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Coordinates(
    @JsonProperty(value = "row", required = true)
    int row,

    @JsonProperty(value = "column", required = true)
    int column
) { }

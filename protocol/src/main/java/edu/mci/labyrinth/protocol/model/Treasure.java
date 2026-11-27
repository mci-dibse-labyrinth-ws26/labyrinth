package edu.mci.labyrinth.protocol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A treasure represented in the shared protocol.
 * @param id shared treasure identifier from zero to 23
 * @param name treasure name
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Treasure(
    @JsonProperty(value = "id", required = true)
    int id,

    @JsonProperty(value = "name", required = true)
    String name
) {

    /**
     * Validates the treasure properties.
     * @throws IllegalArgumentException if the identifier is outside, the range zero to 23, or name is null
     */
    public Treasure {
        if (id < 0 || id > 23) {
            throw new IllegalArgumentException(
                "id must be between 0 and 23"
            );
        }

        if (name == null) {
            throw new IllegalArgumentException(
                "name must not be null"
            );
        }
    }
}

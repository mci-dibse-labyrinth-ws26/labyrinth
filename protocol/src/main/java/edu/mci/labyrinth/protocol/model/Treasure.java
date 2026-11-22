package edu.mci.labyrinth.protocol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A treasure represented in the shared protocol.
 *
 * @param id shared treasure identifier from 0 to 23
 * @param name treasure name
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Treasure(int id, String name) {
}

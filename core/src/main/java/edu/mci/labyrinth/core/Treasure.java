
package edu.mci.labyrinth.core;

import java.util.List;

/**
 * Represents a treasure with a unique identifier and name.
 *
 * @param id the treasure identifier from 0 to 23
 * @param name the treasure name
 */
public record Treasure(int id, String name) {

    /**
     * Names of the 24 treasures in identifier order.
     */
    public static final List<String> NAMES = List.of(
        "Schlüssel",
        "Krone",
        "Truhe",
        "Ring",
        "Kelch",
        "Schwert",
        "Helm",
        "Buch",
        "Karte",
        "Kompass",
        "Laterne",
        "Juwel",
        "Münzbeutel",
        "Zepter",
        "Amulett",
        "Totenkopf",
        "Kerze",
        "Sanduhr",
        "Fernrohr",
        "Harfe",
        "Maske",
        "Trank",
        "Feder",
        "Glocke"
    );

    /**
     * Validates the treasure identifier and name.
     *
     * @throws IllegalArgumentException if the identifier or
     *                                  name is invalid
     */
    public Treasure {
        if (id < 0 || id >= NAMES.size()) {
            throw new IllegalArgumentException(
                "Treasure ID must be between 0 and 23");
        }

        if (!NAMES.get(id).equals(name)) {
            throw new IllegalArgumentException(
                "Treasure name does not match ID");
        }
    }

    /**
     * Creates a treasure using its identifier.
     *
     * @param id the treasure identifier
     * @return the corresponding treasure
     * @throws IllegalArgumentException if the ID is invalid
     */
    public static Treasure of(int id) {
        if (id < 0 || id >= NAMES.size()) {
            throw new IllegalArgumentException(
                "Treasure ID must be between 0 and 23");
        }

        return new Treasure(id, NAMES.get(id));
    }
}

package edu.mci.labyrinth.core;

/**
 * Signals that another figure already stands on the requested target field (V-09).
 *
 * <p>The game server reports this to clients as {@code POSITION_OCCUPIED}.
 */
public class PositionOccupiedException extends IllegalArgumentException {

    /** Version of the serialized form. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception with a description of the occupied field.
     *
     * @param message description of the occupied field
     */
    public PositionOccupiedException(String message) {
        super(message);
    }
}

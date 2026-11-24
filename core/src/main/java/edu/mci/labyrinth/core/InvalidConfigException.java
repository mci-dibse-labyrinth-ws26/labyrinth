package edu.mci.labyrinth.core;

/**
 * Signals that a game configuration violates the allowed limits (V-15).
 *
 * <p>The game server reports this to clients as {@code INVALID_CONFIG}.
 */
public class InvalidConfigException extends IllegalArgumentException {

    /** Version of the serialized form. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception with a description of the violated limit.
     *
     * @param message description of the violated limit
     */
    public InvalidConfigException(String message) {
        super(message);
    }
}

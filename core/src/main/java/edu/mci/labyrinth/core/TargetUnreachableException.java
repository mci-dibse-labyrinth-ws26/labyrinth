package edu.mci.labyrinth.core;

/**
 * Signals that a figure cannot reach the requested target field (V-08).
 *
 * <p>The game server reports this to clients as {@code TARGET_UNREACHABLE}.
 */
public class TargetUnreachableException extends IllegalArgumentException {

    /** Version of the serialized form. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception with a description of the rejected target.
     *
     * @param message description of the rejected target
     */
    public TargetUnreachableException(String message) {
        super(message);
    }
}

package edu.mci.labyrinth.protocol.codec;

/**
 * Indicates a failure when encoding or decoding a protocol message.
 */
public final class InvalidMessageException extends Exception {

    /**
     * Creates an exception for an invalid message.
     * @param message description of the failure
     */
    public InvalidMessageException(String message) {
        super(message);
    }

    /**
     * Creates an exception with its original cause.
     * @param message description of the failure
     * @param cause original exception
     */
    public InvalidMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}

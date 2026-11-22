package edu.mci.labyrinth.protocol.model;

/**
 * The bonus types defined by the shared protocol.
 */
public enum BonusType {

    /** Teleports the player's figure to a target position. */
    BEAM,

    /** Exchanges positions with another player. */
    SWAP,

    /** Allows pushing a normally fixed inner row or column. */
    PUSH_FIXED,

    /** Allows an additional push. */
    PUSH_TWICE
}


package edu.mci.labyrinth.core;

/**
 * Defines the available bonus types in the labyrinth game.
 */
public enum BonusType {

    /** Teleports a player to an available board position. */
    BEAM,

    /** Exchanges the positions of two players. */
    SWAP,

    /** Allows pushing a row or column containing fixed tiles. */
    PUSH_FIXED,

    /** Allows an additional tile push during the turn. */
    PUSH_TWICE
}

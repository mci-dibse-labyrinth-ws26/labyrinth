package edu.mci.labyrinth.protocol.model;

/**
 * The turn phases defined by the shared protocol.
 */
public enum TurnState {

    /** The active player must push a tile. */
    WAITING_FOR_PUSH,

    /** The active player may use a bonus or skip the bonus phase. */
    WAITING_FOR_BONUS_USE,

    /** The active player may move or end the turn without moving. */
    WAITING_FOR_MOVE
}

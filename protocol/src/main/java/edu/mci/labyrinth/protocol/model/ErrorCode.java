package edu.mci.labyrinth.protocol.model;

/**
 * The error codes defined by the shared WebSocket protocol.
 */
public enum ErrorCode {

    /** An unexpected server error occurred. */
    GENERAL_ERROR,

    /** The command or message envelope is invalid. */
    INVALID_COMMAND,

    /** The player has not completed the connection process. */
    PLAYER_NOT_CONNECTED,

    /** The command was sent by a player whose turn is not active. */
    NOT_YOUR_TURN,

    /** The command is not allowed in the current turn phase. */
    INVALID_TURN_PHASE,

    /** The requested push is invalid. */
    INVALID_PUSH,

    /** The target cannot be reached through open paths. */
    TARGET_UNREACHABLE,

    /** The target position is occupied by another player. */
    POSITION_OCCUPIED,

    /** Reversing the movement path is forbidden. */
    REVERSING_PATH_FORBIDDEN,

    /** The requested bonus is not available. */
    BONUS_NOT_AVAILABLE,

    /** The target of the bonus is invalid. */
    INVALID_BONUS_TARGET,

    /** The lobby has reached its player limit. */
    LOBBY_FULL,

    /** The username is already in use. */
    USERNAME_TAKEN,

    /** The game has already started. */
    GAME_ALREADY_STARTED,

    /** The command requires administrator permissions. */
    NOT_ADMIN,

    /** There are not enough players to start the game. */
    NOT_ENOUGH_PLAYERS,

    /** The requested game configuration is invalid. */
    INVALID_CONFIG
}

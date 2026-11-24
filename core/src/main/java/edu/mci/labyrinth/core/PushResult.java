
package edu.mci.labyrinth.core;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Result of a successfully executed tile push.
 *
 * @param board board after the push
 * @param playerPositions updated positions of all players
 * @param lastPush the executed push, used for reverse-push validation
 */
public record PushResult(
    Board board,
    Map<PlayerColor, Position> playerPositions,
    Push lastPush
) {

    /**
     * Validates the result and defensively copies player positions.
     *
     * @throws NullPointerException if a required value is null
     */
    public PushResult {
        Objects.requireNonNull(board, "Board must not be null");
        Objects.requireNonNull(playerPositions, "Player positions must not be null");
        Objects.requireNonNull(lastPush, "Last push must not be null");

        EnumMap<PlayerColor, Position> copy =
            new EnumMap<>(PlayerColor.class);

        copy.putAll(playerPositions);

        for (Position position : copy.values()) {
            Objects.requireNonNull(position, "Player position must not be null");
            if (!board.contains(position)) {
                throw new IllegalArgumentException(
                    "Player position must be inside the board");
            }
        }

        playerPositions = Collections.unmodifiableMap(copy);
    }
}

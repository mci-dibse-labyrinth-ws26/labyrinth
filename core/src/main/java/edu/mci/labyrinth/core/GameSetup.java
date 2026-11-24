package edu.mci.labyrinth.core;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Starting state of a game: board, start corners, treasure decks and turn order (F-210).
 *
 * <p>Players are identified by their color. Colors are assigned in join order (A-06), so
 * {@code players().get(0)} is the player who joined first. The game server maps its own player
 * identifiers to these colors.
 *
 * @param board the board with treasures on its tiles
 * @param players colors of the players in join order
 * @param startPositions start corner of each player (A-06)
 * @param treasureDecks hidden treasure deck of each player; the first element is the first
 *     target (A-05)
 * @param turnOrder colors in the order in which the players take their turns
 */
public record GameSetup(
        Board board,
        List<PlayerColor> players,
        Map<PlayerColor, Position> startPositions,
        Map<PlayerColor, List<Treasure>> treasureDecks,
        List<PlayerColor> turnOrder) {

    /**
     * Validates that all parts describe the same players and copies the collections.
     *
     * @throws IllegalArgumentException if a part does not match the players
     */
    public GameSetup {
        Objects.requireNonNull(board, "Board must not be null");
        players = List.copyOf(players);
        turnOrder = List.copyOf(turnOrder);

        if (players.isEmpty()) {
            throw new IllegalArgumentException("Players must not be empty");
        }

        EnumSet<PlayerColor> colors = EnumSet.copyOf(players);
        if (colors.size() != players.size()) {
            throw new IllegalArgumentException("Players must not contain a color twice");
        }
        if (!startPositions.keySet().equals(colors)) {
            throw new IllegalArgumentException("Every player needs exactly one start position");
        }
        if (!treasureDecks.keySet().equals(colors)) {
            throw new IllegalArgumentException("Every player needs exactly one treasure deck");
        }
        if (turnOrder.size() != players.size() || !EnumSet.copyOf(turnOrder).equals(colors)) {
            throw new IllegalArgumentException("Turn order must contain every player once");
        }

        startPositions = Collections.unmodifiableMap(new EnumMap<>(startPositions));

        Map<PlayerColor, List<Treasure>> decks = new EnumMap<>(PlayerColor.class);
        treasureDecks.forEach((color, deck) -> decks.put(color, List.copyOf(deck)));
        treasureDecks = Collections.unmodifiableMap(decks);
    }
}

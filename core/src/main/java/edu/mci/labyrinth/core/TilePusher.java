
package edu.mci.labyrinth.core;

import java.util.Objects;

/**
 * Performs tile-pushing operations on an immutable board.
 */
public final class TilePusher {

    private TilePusher() {
    }

    /**
     * Pushes the spare tile into the specified row or column.
     *
     * <p>The original board remains unchanged. The ejected tile
     * becomes the new spare tile, retaining its contents.</p>
     *
     * @param board the board before the push
     * @param push the requested index and direction
     * @param rotation absolute clockwise rotation from 0 to 3
     * @param previousPush the previous push, or null for the first push
     * @return the board after the push
     * @throws IllegalArgumentException if the index or rotation is invalid
     * @throws NullPointerException if board or push is null
     */
    public static Board push(
        Board board,
        Push push,
        int rotation,
        Push previousPush) {
        Objects.requireNonNull(board, "Board must not be null");
        Objects.requireNonNull(push, "Push must not be null");

        RuleValidator.validatePushIndex(
            board, push.index(), push.direction());

        RuleValidator.validateReversePush(previousPush, push);

        RuleValidator.validatePushRotation(rotation);

        Tile[][] tiles = board.tiles();
        Tile inserted = board.spareTile().withRotation(rotation);
        Tile ejected;

        int index = push.index();

        switch (push.direction()) {
            case RIGHT -> {
                ejected = tiles[index][board.cols() - 1];

                for (int col = board.cols() - 1; col > 0; col--) {
                    tiles[index][col] = tiles[index][col - 1];
                }

                tiles[index][0] = inserted;
            }

            case LEFT -> {
                ejected = tiles[index][0];

                for (int col = 0; col < board.cols() - 1; col++) {
                    tiles[index][col] = tiles[index][col + 1];
                }

                tiles[index][board.cols() - 1] = inserted;
            }

            case DOWN -> {
                ejected = tiles[board.rows() - 1][index];

                for (int row = board.rows() - 1; row > 0; row--) {
                    tiles[row][index] = tiles[row - 1][index];
                }

                tiles[0][index] = inserted;
            }

            case UP -> {
                ejected = tiles[0][index];

                for (int row = 0; row < board.rows() - 1; row++) {
                    tiles[row][index] = tiles[row + 1][index];
                }

                tiles[board.rows() - 1][index] = inserted;
            }

            default -> throw new IllegalStateException(
                "Unexpected direction: " + push.direction());
        }

        return new Board(
            board.rows(), board.cols(), tiles, ejected);
    }

    /**
     * Returns a player's position after a tile push (A-09).
     *
     * <p>Players on the affected row or column move one field
     * in the push direction. Players on an ejected tile
     * wrap around to the opposite edge.</p>
     *
     * @param position the player's position before the push
     * @param push the executed push
     * @param board the board before the push
     * @return the player's new position
     * @throws NullPointerException if an argument is null
     */
    public static Position shiftPosition(
        Position position, Push push, Board board) {

        Objects.requireNonNull(position, "Position must not be null");
        Objects.requireNonNull(push, "Push must not be null");
        Objects.requireNonNull(board, "Board must not be null");

        if (!board.contains(position)) {
            throw new IllegalArgumentException(
                "Player position must be inside the board");
        }

        int index = push.index();

        return switch (push.direction()) {
            case RIGHT -> position.row() == index
                ? new Position(position.row(),
                (position.column() + 1) % board.cols())
                : position;

            case LEFT -> position.row() == index
                ? new Position(position.row(),
                (position.column() + board.cols() - 1)
                    % board.cols())
                : position;

            case DOWN -> position.column() == index
                ? new Position(
                (position.row() + 1) % board.rows(),
                position.column())
                : position;

            case UP -> position.column() == index
                ? new Position(
                (position.row() + board.rows() - 1)
                    % board.rows(),
                position.column())
                : position;
        };
    }

    /**
     * Executes a validated push and updates all player positions.
     *
     * <p>The board and player positions remain unchanged if
     * any validation fails (V-05, V-06, V-07).</p>
     *
     * @param board the current board
     * @param playerPositions current positions of all players
     * @param push the requested push
     * @param rotation absolute rotation from 0 to 3
     * @param previousPush the previous push, or null for the first push
     * @return the updated board, player positions and last push
     * @throws IllegalArgumentException if the push is invalid
     * @throws NullPointerException if a required argument is null
     */
    public static PushResult pushWithPlayers(
        Board board,
        java.util.Map<PlayerColor, Position> playerPositions,
        Push push,
        int rotation,
        Push previousPush) {

        Objects.requireNonNull(board, "Board must not be null");
        Objects.requireNonNull(playerPositions,
            "Player positions must not be null");
        Objects.requireNonNull(push, "Push must not be null");

        // Validates V-05, V-06 and V-07 before changing state.
        Board newBoard = push(board, push, rotation, previousPush);

        java.util.Map<PlayerColor, Position> newPositions =
            new java.util.EnumMap<>(PlayerColor.class);

        for (var entry : playerPositions.entrySet()) {
            Position shifted = shiftPosition(
                entry.getValue(), push, board);

            newPositions.put(entry.getKey(), shifted);
        }

        return new PushResult(newBoard, newPositions, push);
    }

}

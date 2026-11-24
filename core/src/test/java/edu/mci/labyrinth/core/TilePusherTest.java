
package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.EnumMap;

/**
 * Tests tile shifting according to A-07, A-09 and A-30.
 */
@Tag("A-30")
class TilePusherTest {

    private static Tile tile(int rotation) {
        return new Tile(
            TileShape.CORNER, rotation, false, null, null);
    }

    private static Board createBoard() {
        Tile[][] tiles = new Tile[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                tiles[row][col] = tile((row * 3 + col) % 4);
            }
        }

        return new Board(3, 3, tiles, tile(0));
    }

    @Test
    void pushesRight() {
        Board before = createBoard();

        Board after = TilePusher.push(before, new Push(1, Direction.RIGHT), 2, null);

        assertEquals(2, after.tileAt(new Position(1, 0)).rotation());
        assertEquals(before.tileAt(new Position(1, 0)),
            after.tileAt(new Position(1, 1)));
        assertEquals(before.tileAt(new Position(1, 1)),
            after.tileAt(new Position(1, 2)));
        assertEquals(before.tileAt(new Position(1, 2)),
            after.spareTile());
    }

    @Test
    void pushesLeft() {
        Board before = createBoard();

        Board after = TilePusher.push(
            before, new Push(1, Direction.LEFT), 1,null);

        assertEquals(1, after.tileAt(new Position(1, 2)).rotation());
        assertEquals(before.tileAt(new Position(1, 1)),
            after.tileAt(new Position(1, 0)));
        assertEquals(before.tileAt(new Position(1, 2)),
            after.tileAt(new Position(1, 1)));
        assertEquals(before.tileAt(new Position(1, 0)),
            after.spareTile());
    }

    @Test
    void pushesDown() {
        Board before = createBoard();

        Board after = TilePusher.push(
            before, new Push(1, Direction.DOWN), 3,null);

        assertEquals(3, after.tileAt(new Position(0, 1)).rotation());
        assertEquals(before.tileAt(new Position(0, 1)),
            after.tileAt(new Position(1, 1)));
        assertEquals(before.tileAt(new Position(1, 1)),
            after.tileAt(new Position(2, 1)));
        assertEquals(before.tileAt(new Position(2, 1)),
            after.spareTile());
    }

    @Test
    void pushesUp() {
        Board before = createBoard();

        Board after = TilePusher.push(
            before, new Push(1, Direction.UP), 0,null);

        assertEquals(before.spareTile(),
            after.tileAt(new Position(2, 1)));
        assertEquals(before.tileAt(new Position(1, 1)),
            after.tileAt(new Position(0, 1)));
        assertEquals(before.tileAt(new Position(2, 1)),
            after.tileAt(new Position(1, 1)));
        assertEquals(before.tileAt(new Position(0, 1)),
            after.spareTile());
    }

    @Test
    void doesNotModifyOriginalBoard() {
        Board before = createBoard();
        Board snapshot = createBoard();

        TilePusher.push(
            before, new Push(1, Direction.RIGHT), 2,null);

        assertEquals(snapshot, before);
    }

    @Test
    void rejectsInvalidPushWithoutChangingBoard() {
        Board before = createBoard();
        Board snapshot = createBoard();

        assertThrows(IllegalArgumentException.class, () ->
            TilePusher.push(
                before, new Push(0, Direction.RIGHT), 0,null));

        assertEquals(snapshot, before);

        assertThrows(IllegalArgumentException.class, () ->
            TilePusher.push(
                before, new Push(1, Direction.RIGHT), 4,null));

        assertEquals(snapshot, before);
    }

    @Test
    @Tag("V-06")
    void rejectsReversePushWithoutChangingBoard() {
        Board before = createBoard();
        Board snapshot = createBoard();

        Push previous = new Push(1, Direction.RIGHT);
        Push reverse = new Push(1, Direction.LEFT);

        assertThrows(IllegalArgumentException.class, () ->
            TilePusher.push(before, reverse, 0, previous));

        assertEquals(snapshot, before);
    }

    @Test
    @Tag("A-09")
    void shiftsPlayersInAllFourDirections() {
        Board board = createBoard();

        assertEquals(new Position(1, 2),
            TilePusher.shiftPosition(
                new Position(1, 1),
                new Push(1, Direction.RIGHT), board));

        assertEquals(new Position(1, 0),
            TilePusher.shiftPosition(
                new Position(1, 1),
                new Push(1, Direction.LEFT), board));

        assertEquals(new Position(2, 1),
            TilePusher.shiftPosition(
                new Position(1, 1),
                new Push(1, Direction.DOWN), board));

        assertEquals(new Position(0, 1),
            TilePusher.shiftPosition(
                new Position(1, 1),
                new Push(1, Direction.UP), board));
    }

    @Test
    @Tag("A-09")
    void wrapsEjectedPlayersToInsertedTile() {
        Board board = createBoard();

        assertEquals(new Position(1, 0),
            TilePusher.shiftPosition(
                new Position(1, 2),
                new Push(1, Direction.RIGHT), board));

        assertEquals(new Position(1, 2),
            TilePusher.shiftPosition(
                new Position(1, 0),
                new Push(1, Direction.LEFT), board));

        assertEquals(new Position(0, 1),
            TilePusher.shiftPosition(
                new Position(2, 1),
                new Push(1, Direction.DOWN), board));

        assertEquals(new Position(2, 1),
            TilePusher.shiftPosition(
                new Position(0, 1),
                new Push(1, Direction.UP), board));
    }

    @Test
    @Tag("A-09")
    void doesNotMovePlayersOutsideAffectedLine() {
        Board board = createBoard();

        Position original = new Position(0, 0);

        for (Direction direction : Direction.values()) {
            assertEquals(original,
                TilePusher.shiftPosition(
                    original, new Push(1, direction), board));
        }
    }

    @Test
    @Tag("A-09")
    void rejectsPlayerPositionOutsideBoard() {
        Board board = createBoard();

        assertThrows(IllegalArgumentException.class, () ->
            TilePusher.shiftPosition(
                new Position(-1, 0),
                new Push(1, Direction.RIGHT), board));
    }

    @Test
    @Tag("A-09")
    void pushWithPlayersMovesAndWrapsPlayers() {
        Board before = createBoard();

        Map<PlayerColor, Position> players = Map.of(
            PlayerColor.RED, new Position(1, 2),
            PlayerColor.BLUE, new Position(1, 0),
            PlayerColor.GREEN, new Position(0, 0));

        Push push = new Push(1, Direction.RIGHT);

        PushResult result = TilePusher.pushWithPlayers(
            before, players, push, 2, null);

        // Ejected player wraps around to the inserted tile.
        assertEquals(new Position(1, 0),
            result.playerPositions().get(PlayerColor.RED));

        // Player on the shifted row moves right.
        assertEquals(new Position(1, 1),
            result.playerPositions().get(PlayerColor.BLUE));

        // Player outside the shifted row stays in place.
        assertEquals(new Position(0, 0),
            result.playerPositions().get(PlayerColor.GREEN));

        // The returned result remembers the last push.
        assertEquals(push, result.lastPush());
    }

    @Test
    @Tag("A-07")
    void pushPreservesTreasureAndBonus() {
        Tile[][] tiles = createBoard().tiles();

        Treasure treasure = Treasure.of(0);

        // Treasure on the tile that will be ejected.
        tiles[1][2] = new Tile(
            TileShape.CORNER, 0, false, treasure, null);

        // Bonus on a tile that will move.
        tiles[1][1] = new Tile(
            TileShape.STRAIGHT, 0, false,
            null, BonusType.BEAM);

        Board before = new Board(
            3, 3, tiles, tile(0));

        Board after = TilePusher.push(
            before, new Push(1, Direction.RIGHT), 1, null);

        assertEquals(treasure, after.spareTile().treasure());

        assertEquals(BonusType.BEAM,
            after.tileAt(new Position(1, 2)).bonus());

        // Contents must not disappear from the original board.
        assertEquals(treasure,
            before.tileAt(new Position(1, 2)).treasure());
    }

    @Test
    @Tag("V-05")
    @Tag("V-06")
    @Tag("V-07")
    void rejectedPushDoesNotModifyGameState() {
        Board before = createBoard();

        Map<PlayerColor, Position> players = new EnumMap<>(
            PlayerColor.class);
        players.put(PlayerColor.RED, new Position(1, 2));

        Board originalBoard = createBoard();
        Map<PlayerColor, Position> originalPlayers =
            Map.copyOf(players);

        Push previous = new Push(1, Direction.RIGHT);

        // V-05: invalid row index
        assertThrows(IllegalArgumentException.class, () ->
            TilePusher.pushWithPlayers(
                before, players,
                new Push(0, Direction.RIGHT), 0, previous));

        // V-06: immediate reverse push
        assertThrows(IllegalArgumentException.class, () ->
            TilePusher.pushWithPlayers(
                before, players,
                new Push(1, Direction.LEFT), 0, previous));

        // V-07: invalid rotation
        assertThrows(IllegalArgumentException.class, () ->
            TilePusher.pushWithPlayers(
                before, players,
                new Push(1, Direction.RIGHT), 4, previous));

        // No rejected operation may change the original state.
        assertEquals(originalBoard, before);
        assertEquals(originalPlayers, players);
    }

}

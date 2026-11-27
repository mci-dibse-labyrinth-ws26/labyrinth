package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.Map;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class RuleValidatorMoveTargetTest {

    /** Ring of eight fields around a centre that is closed towards all neighbours. */
    private static final Board RING = BoardFixtures.board(
            "┌─┐",
            "│─│",
            "└─┘");

    private static final Board CORRIDOR = BoardFixtures.board(
            "───────",
            "│││││││",
            "│││││││");

    private static final Position TOP_LEFT = new Position(0, 0);

    @Test
    @Tag("V-08")
    @Tag("V-09")
    @Tag("A-11")
    void acceptsReachableFreeTargetAndReturnsSteps() {
        Map<PlayerColor, Position> figures = Map.of(
                PlayerColor.RED, TOP_LEFT,
                PlayerColor.BLUE, new Position(0, 2));

        assertEquals(4, RuleValidator.validateMoveTarget(
                RING, figures, PlayerColor.RED, new Position(2, 2)));
    }

    @Test
    @Tag("V-09")
    @Tag("A-10")
    void acceptsStayingOnOwnField() {
        Map<PlayerColor, Position> figures = Map.of(PlayerColor.RED, new Position(1, 1));

        assertEquals(0, RuleValidator.validateMoveTarget(
                RING, figures, PlayerColor.RED, new Position(1, 1)));
    }

    @Test
    @Tag("V-09")
    @Tag("A-12")
    void allowsPassingFieldOccupiedByOtherFigure() {
        Map<PlayerColor, Position> figures = Map.of(
                PlayerColor.RED, TOP_LEFT,
                PlayerColor.BLUE, new Position(0, 3));

        assertEquals(6, RuleValidator.validateMoveTarget(
                CORRIDOR, figures, PlayerColor.RED, new Position(0, 6)));
    }

    @Test
    @Tag("V-08")
    void rejectsTargetOutsideBoard() {
        Map<PlayerColor, Position> figures = Map.of(PlayerColor.RED, TOP_LEFT);

        assertThrows(TargetUnreachableException.class, () -> RuleValidator.validateMoveTarget(
                RING, figures, PlayerColor.RED, new Position(-1, 0)));
        assertThrows(TargetUnreachableException.class, () -> RuleValidator.validateMoveTarget(
                RING, figures, PlayerColor.RED, new Position(0, 3)));
    }

    @Test
    @Tag("V-08")
    void rejectsTargetNotConnectedToFigure() {
        Map<PlayerColor, Position> figures = Map.of(PlayerColor.RED, TOP_LEFT);

        assertThrows(TargetUnreachableException.class, () -> RuleValidator.validateMoveTarget(
                RING, figures, PlayerColor.RED, new Position(1, 1)));
    }

    @Test
    @Tag("V-09")
    @Tag("A-12")
    void rejectsTargetOccupiedByOtherFigure() {
        Map<PlayerColor, Position> figures = Map.of(
                PlayerColor.RED, TOP_LEFT,
                PlayerColor.BLUE, new Position(2, 2));

        assertThrows(PositionOccupiedException.class, () -> RuleValidator.validateMoveTarget(
                RING, figures, PlayerColor.RED, new Position(2, 2)));
    }

    @Test
    @Tag("V-08")
    @Tag("V-09")
    void checksReachabilityBeforeOccupation() {
        Map<PlayerColor, Position> figures = Map.of(
                PlayerColor.RED, TOP_LEFT,
                PlayerColor.BLUE, new Position(1, 1));

        assertThrows(TargetUnreachableException.class, () -> RuleValidator.validateMoveTarget(
                RING, figures, PlayerColor.RED, new Position(1, 1)));
    }

    @Test
    void rejectsPlayerWithoutPosition() {
        Map<PlayerColor, Position> figures = Map.of(PlayerColor.RED, TOP_LEFT);

        assertThrowsExactly(IllegalArgumentException.class, () -> RuleValidator.validateMoveTarget(
                RING, figures, PlayerColor.BLUE, TOP_LEFT));
    }

    @Test
    void rejectsMissingArguments() {
        Map<PlayerColor, Position> figures = Map.of(PlayerColor.RED, TOP_LEFT);

        assertThrows(NullPointerException.class, () -> RuleValidator.validateMoveTarget(
                null, figures, PlayerColor.RED, TOP_LEFT));
        assertThrows(NullPointerException.class, () -> RuleValidator.validateMoveTarget(
                RING, null, PlayerColor.RED, TOP_LEFT));
        assertThrows(NullPointerException.class, () -> RuleValidator.validateMoveTarget(
                RING, figures, null, TOP_LEFT));
        assertThrows(NullPointerException.class, () -> RuleValidator.validateMoveTarget(
                RING, figures, PlayerColor.RED, null));
    }

    @Test
    void rejectionsAreIllegalArguments() {
        assertEquals(IllegalArgumentException.class,
                TargetUnreachableException.class.getSuperclass());
        assertEquals(IllegalArgumentException.class,
                PositionOccupiedException.class.getSuperclass());
    }
}

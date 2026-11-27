package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BoardFixturesTest {

    @ParameterizedTest(name = "{0} opens {1}")
    @CsvSource(delimiter = ';', value = {
        "│; UP DOWN",
        "─; RIGHT LEFT",
        "└; UP RIGHT",
        "┌; RIGHT DOWN",
        "┐; DOWN LEFT",
        "┘; UP LEFT",
        "├; UP RIGHT DOWN",
        "┬; RIGHT DOWN LEFT",
        "┤; UP DOWN LEFT",
        "┴; UP RIGHT LEFT",
    })
    void glyphShowsOpeningsOfTile(char glyph, String directions) {
        Board board = BoardFixtures.board(glyph, "│││", "│││", "│││");

        assertEquals(openings(directions), board.spareTile().openings());
    }

    @Test
    void readsFieldsRowByRow() {
        Board board = BoardFixtures.board(
                "┌─┐",
                "│─│",
                "└─┘");

        assertEquals(3, board.rows());
        assertEquals(3, board.cols());
        assertEquals(openings("RIGHT DOWN"), board.tileAt(new Position(0, 0)).openings());
        assertEquals(openings("RIGHT LEFT"), board.tileAt(new Position(1, 1)).openings());
        assertEquals(openings("UP LEFT"), board.tileAt(new Position(2, 2)).openings());
    }

    @Test
    void fixedPositionsGetFixedTiles() {
        Board board = BoardFixtures.board("┌─┐", "│─│", "└─┘");

        assertTrue(board.tileAt(new Position(0, 0)).fixed());
        assertTrue(board.tileAt(new Position(2, 2)).fixed());
        assertFalse(board.tileAt(new Position(0, 1)).fixed());
        assertFalse(board.tileAt(new Position(1, 1)).fixed());
        assertFalse(board.spareTile().fixed());
    }

    @Test
    void rejectsUnknownCharacter() {
        assertThrows(IllegalArgumentException.class,
                () -> BoardFixtures.board("┌─┐", "│┼│", "└─┘"));
    }

    @Test
    void rejectsRowsOfDifferentLength() {
        assertThrows(IllegalArgumentException.class,
                () -> BoardFixtures.board("┌─┐", "││", "└─┘"));
    }

    private static Set<Direction> openings(String directions) {
        Set<Direction> openings = EnumSet.noneOf(Direction.class);
        for (String direction : directions.split(" ")) {
            openings.add(Direction.valueOf(direction));
        }
        return openings;
    }
}

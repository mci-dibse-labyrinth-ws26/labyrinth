package edu.mci.labyrinth.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    void boardHasSevenBySevenFields() {
        assertEquals(7, Board.SIZE);
    }
}

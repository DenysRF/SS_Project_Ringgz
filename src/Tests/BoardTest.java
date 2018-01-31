package Tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import Game.Model.Field;
import Game.Players.Player;
import org.junit.Before;
import org.junit.Test;

import Game.Model.Board;
import Game.Model.Piece;
import Game.Players.HumanPlayer;

import java.util.List;

public class BoardTest {

    private Board board;
    private Player[] players;

    @Before
    public void setUp() {
        players = new HumanPlayer[2];
        players[0] = new HumanPlayer("Player1", 2);
        players[1] = new HumanPlayer("Player2", 2);
        board = new Board(players);
    }

    @Test
    public void testIndex() {
        assertEquals(board.index(0, 0), 0);
        assertEquals(board.index(4, 4), 24);
        assertEquals(board.index(2, 2), 12);
    }

    @Test
    public void testGetField() {
        Field field = board.getField(12);
        assertTrue(field != null);
        assertTrue(board.getField(2, 2) == field);
    }

//    @Test
//    public void testSetField() {
//        Piece before = board.getField(0).getFieldContent()[0];
//        Piece validBase = board.getPlayers()[0].getPrimaryPieces().get(Piece.BASE).get(0);
//        board.setField(validBase, 0);
//        Piece after = board.getField(0).getFieldContent()[0];
//        assertFalse(before == after);
//        assertTrue(validBase == after);
//
//        // The field already contains a Base, so placing a ring should not be possible
//        // TODO: check if a move is valid in Board.setField;
//        Piece invalidSmall = board.getPlayers()[1].getPrimaryPieces().get(Piece.SMALL).get(0);
//        board.setField(invalidSmall, 0);
//        Piece invalid = board.getField(0).getFieldContent()[1];
//        assertTrue(invalidSmall == invalid);
//
//
//    }

    @Test
    public void testGetAdjacentFields() {
        List<Field> corner = board.getAdjacentFields(board.getField(0));
        assertTrue(corner.size() == 3);
        for (int i = 0; i < corner.size(); i++) {
            assertTrue(corner.get(i) == board.getField(0) ||
                    corner.get(i) == board.getField(1) ||
                    corner.get(i) == board.getField(5));
            for (int j = 0; j < corner.size(); j++) {
                if (j != i) {
                    assertTrue(corner.get(i) != corner.get(j));
                }
            }
        }


        List<Field> side = board.getAdjacentFields(board.getField(1));
        assertTrue(side.size() == 4);
        for (int i = 0; i < side.size(); i++) {
            assertTrue(side.get(i) == board.getField(1) ||
                    side.get(i) == board.getField(0) ||
                    side.get(i) == board.getField(2) ||
                    side.get(i) == board.getField(6));
            for (int j = 0; j < side.size(); j++) {
                if (j != i) {
                    assertTrue(side.get(i) != side.get(j));
                }
            }
        }

        // goes wrong here
        List<Field> middle = board.getAdjacentFields(board.getField(6));
        for (int i = 0; i < middle.size(); i++) {
            assertTrue(middle.get(i) == board.getField(6) ||
                    middle.get(i) == board.getField(5) ||
                    middle.get(i) == board.getField(7) ||
                    middle.get(i) == board.getField(1) ||
                    middle.get(i) == board.getField(11));
            for (int j = 0; j < middle.size(); j++) {
                if (j != i) {
                    assertTrue(middle.get(i) != middle.get(j));
                }
            }
        }
    }

    @Test
    public void testGetValidFields() {

    }

    @Test
    public void testGameOver() {

    }

    @Test
    public void testGameOverPlayer() {

    }

    @Test
    public void testGetScore() {

    }

    @Test
    public void testReset() {
        Piece piece = new Piece(Piece.BASE, true, players[0]);
        for (int i = 0; i < Board.DIM * Board.DIM; i++) {
            board.setField(piece, i);
        }
        board.reset();
        for (int f = 0; f < Board.DIM * Board.DIM; f++) {
            for (int p = 0; p < Field.MAX_SPACE; p++) {
                assertTrue(board.getField(f).getFieldContent()[p] == null);
            }
        }
    }

    @Test
    public void testGetPlayers() {
        Player[] test = board.getPlayers();
        for (int i = 0; i < test.length; i++) {
            assertTrue(players[i] == test[i]);
        }
    }
}

package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import game.model.Field;
import game.players.Player;
import org.junit.Before;
import org.junit.Test;

import game.model.Board;
import game.model.Piece;
import game.players.HumanPlayer;

import java.util.List;
import java.util.Map;

public class BoardTest {

    private Board board;
    private Player[] players;
    private Board board2;
    private Player[] players2;

    @Before
    public void setUp() {
        players = new HumanPlayer[2];
        players[0] = new HumanPlayer("Player1", 2);
        players[1] = new HumanPlayer("Player2", 2);
        board = new Board(players);

        players2 = new HumanPlayer[2];
        players2[0] = new HumanPlayer("Player1", 2);
        players2[1] = new HumanPlayer("Player2", 2);
        board2 = new Board(players);
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
        players2[0].setStart(8, board2);
        players2[0].makeMove(3, players2[0].getPrimaryPieces().get(Piece.BASE).get(0), board2);
        Map<Field, List<Piece>> test = players2[0].getValidMoves(true, board2);
        Map<Field, List<Piece>> test2 = players2[0].getValidMoves(false, board2);
        Map<Field, List<Piece>> test3 = players2[1].getValidMoves(true, board2);
        Map<Field, List<Piece>> test4 = players2[1].getValidMoves(false, board2);
        players2[0].makeMove(7, players2[0].getPrimaryPieces().get(Piece.BASE).get(0), board2);
        players2[0].makeMove(13, players2[0].getPrimaryPieces().get(Piece.BASE).get(0), board2);
        players2[0].makeMove(9, players2[0].getSecondaryPieces().get(Piece.BASE).get(0), board2);
        test = players2[0].getValidMoves(true, board2);
        test2 = players2[0].getValidMoves(false, board2);
        test3 = players2[1].getValidMoves(true, board2);
        test4 = players2[1].getValidMoves(false, board2);

        assertTrue(board2.gameOver(players2[1]));

        System.out.println(board2.gameOver(players2[0]));
        System.out.println(board2.gameOver(players2[0]));
        System.out.println(board2.gameOver(players2[0]));
        System.out.println(board2.gameOver(players2[0]));
        System.out.println(board2.gameOver(players2[0]));
        System.out.println(board2.gameOver(players2[0]));
        System.out.println(board2.gameOver(players2[0]));
        System.out.println(board2.gameOver(players2[0]));

        assertFalse(board2.gameOver(players2[0]));

        players2[0].makeMove(4, players2[0].getSecondaryPieces().get(Piece.BASE).get(0), board2);
        players2[0].makeMove(14, players2[0].getPrimaryPieces().get(Piece.SMALL).get(0), board2);
        players2[0].makeMove(14, players2[0].getPrimaryPieces().get(Piece.MEDIUM).get(0), board2);
        players2[0].makeMove(14, players2[0].getPrimaryPieces().get(Piece.BIG).get(0), board2);
        players2[0].makeMove(14, players2[0].getPrimaryPieces().get(Piece.HUGE).get(0), board2);
        //Now player[0] does not contain any possible moves for secondary
        assertFalse(board2.gameOver(players2[0]));

        players2[0].makeMove(2, players2[0].getPrimaryPieces().get(Piece.SMALL).get(0), board2);
        players2[0].makeMove(2, players2[0].getPrimaryPieces().get(Piece.MEDIUM).get(0), board2);
        players2[0].makeMove(2, players2[0].getPrimaryPieces().get(Piece.BIG).get(0), board2);
        players2[0].makeMove(2, players2[0].getPrimaryPieces().get(Piece.HUGE).get(0), board2);

        players2[0].makeMove(12, players2[0].getPrimaryPieces().get(Piece.SMALL).get(0), board2);
        players2[0].makeMove(12, players2[0].getPrimaryPieces().get(Piece.MEDIUM).get(0), board2);
        players2[0].makeMove(12, players2[0].getPrimaryPieces().get(Piece.BIG).get(0), board2);
        players2[0].makeMove(12, players2[0].getPrimaryPieces().get(Piece.HUGE).get(0), board2);



        assertTrue(board2.gameOver(players2[0]));
//        assertTrue(board2.gameOver(players2[1]));

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

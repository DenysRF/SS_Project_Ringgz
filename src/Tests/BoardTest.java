package Tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import Game.Model.Field;
import org.junit.Before;
import org.junit.Test;

import Game.Model.Board;
import Game.Model.Piece;
import Game.Players.HumanPlayer;
import Game.Players.Player;

import java.util.ArrayList;
import java.util.List;

public class BoardTest {

    private Board board;

    @Before
    public void setUp() {
        Player[] players = new Player[2];

        for (int i = 0; i < players.length; i++) {

            List<Piece> primaryPieces;
            List<Piece> secondaryPieces;
            primaryPieces = new ArrayList<>();
            secondaryPieces = new ArrayList<>();

            for (int j = 0; j < 3; j++) {

                primaryPieces.add(new Piece(Piece.BASE, true, players[i]));
                primaryPieces.add((new Piece(Piece.SMALL, true, players[i])));
                primaryPieces.add(new Piece(Piece.MEDIUM, true, players[i]));
                primaryPieces.add(new Piece(Piece.BIG, true, players[i]));
                primaryPieces.add(new Piece(Piece.HUGE, true, players[i]));

                secondaryPieces.add(new Piece(Piece.BASE, false, players[i]));
                secondaryPieces.add((new Piece(Piece.SMALL, false, players[i])));
                secondaryPieces.add(new Piece(Piece.MEDIUM, false, players[i]));
                secondaryPieces.add(new Piece(Piece.BIG, false, players[i]));
                secondaryPieces.add(new Piece(Piece.HUGE, false, players[i]));
            }

            players[i] = new HumanPlayer("speler " + i, players.length);
        }
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
        assertTrue(field instanceof Field);
        assertTrue(board.getField(2, 2) == field);
    }

//    @Test
//    public void testSetField() {
//        Field fieldBefore = board.getField(0);
//        // ArrayList primaryPieces has a Base at index 0
//        // This is far from a good way of implementing this Collection
//        board.setField(board.getPlayers()[0].getPrimaryPieces().get(0), 0);
//        Field fieldAfter = board.getField(0);
//        assertFalse(fieldBefore == fieldAfter);
//    }
}

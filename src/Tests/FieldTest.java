package Tests;

import Game.Model.Field;
import Game.Model.Piece;
import Game.Players.HumanPlayer;
import Game.Players.Player;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class FieldTest {

    private Field[] field;
    private Player[] players;

    @Before
    public void setUp() {
        field = new Field[18];
        for (int i = 0; i < field.length; i++) {
            field[i] = new Field();
        }
    }

    @Test
    public void testGetFieldContent() {
        // TODO I guess these work alright
    }

    @Test
    public void testSetFieldContent() {
        // TODO I guess these work alright
    }

    @Test
    public void testIsValidMove() {
        field[0].setFieldContent(new Piece(Piece.BASE));
        assertFalse(field[0].isValidMove(new Piece(Piece.SMALL)));
        assertTrue(field[1].isValidMove(new Piece(Piece.SMALL)));
        field[1].setFieldContent(new Piece(Piece.SMALL));
        assertFalse(field[1].isValidMove(new Piece(Piece.SMALL)));
        assertTrue(field[1].isValidMove(new Piece(Piece.MEDIUM)));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(field[0].isEmpty());
        field[0].setFieldContent(new Piece(Piece.START));
        assertFalse(field[0].isEmpty());

    }

    // TODO; Divide into multiple tests, testMajorityBases, testMajority1Player etc.
    @Test
    public void testMajority() {
        players = new Player[2];
        for (int i = 0; i < players.length; i++) {
            players[i] = new HumanPlayer("player " + i, players.length);
        }

        Player winner;

        // Test empty
        winner = field[0].majority(players.length);
        assertTrue(winner == null);

        // Test with Bases
        field[0].setFieldContent(new Piece(Piece.START));
        winner = field[0].majority(players.length);
        assertTrue(winner == null);

        field[1].setFieldContent(players[0].getPrimaryPieces().get(Piece.BASE).get(0));
        winner = field[1].majority(players.length);
        assertTrue(winner == null);

        // Test with 1 player and primary color
        field[2].setFieldContent(players[0].getPrimaryPieces().get(Piece.SMALL).get(0));
        winner = field[2].majority(players.length);
        assertTrue(winner == players[0]);

        // Test with 1 player and both colors
        field[3].setFieldContent(players[0].getPrimaryPieces().get(Piece.SMALL).get(0));
        field[3].setFieldContent(players[0].getPrimaryPieces().get(Piece.MEDIUM).get(0));
        field[3].setFieldContent(players[0].getSecondaryPieces().get(Piece.BIG).get(0));
        winner = field[3].majority(players.length);
        assertTrue(winner == players[0]);

        field[3].setFieldContent(players[0].getSecondaryPieces().get(Piece.HUGE).get(0));
        winner = field[3].majority(players.length);
        assertTrue(winner == null);

        // Test with 2 players and both colors
        field[4].setFieldContent(players[0].getPrimaryPieces().get(Piece.SMALL).get(0));
        field[4].setFieldContent(players[0].getPrimaryPieces().get(Piece.MEDIUM).get(0));
        field[4].setFieldContent(players[1].getPrimaryPieces().get(Piece.BIG).get(0));
        field[4].setFieldContent(players[1].getSecondaryPieces().get(Piece.HUGE).get(0));
        winner = field[4].majority(players.length);
        assertTrue(winner == players[0]);

        field[5].setFieldContent(players[0].getPrimaryPieces().get(Piece.SMALL).get(0));
        field[5].setFieldContent(players[0].getPrimaryPieces().get(Piece.MEDIUM).get(0));
        field[5].setFieldContent(players[1].getSecondaryPieces().get(Piece.BIG).get(0));
        field[5].setFieldContent(players[1].getSecondaryPieces().get(Piece.HUGE).get(0));
        winner = field[5].majority(players.length);
        assertTrue(winner == null);

        // Test with 3 players
        // TODO
    }

}
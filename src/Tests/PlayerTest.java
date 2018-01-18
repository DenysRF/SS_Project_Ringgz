package Tests;

/**
 * Created by Thomas Stouten on 17-Jan-18.
 */

import Game.Model.Piece;
import Game.Players.HumanPlayer;
import Game.Players.Player;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class PlayerTest {

    Player twoPlayers = new HumanPlayer("test", 2);
    Player threePlayers = new HumanPlayer("test", 3);
    Player fourPlayers = new HumanPlayer("test", 4);

    @Test
    public void testPieces() {
        assertEquals(twoPlayers.getPrimaryPieces().get(Piece.BASE).size(), 3);
        assertEquals(twoPlayers.getPrimaryPieces().get(Piece.SMALL).size(), 3);
        assertEquals(twoPlayers.getPrimaryPieces().get(Piece.MEDIUM).size(), 3);
        assertEquals(twoPlayers.getPrimaryPieces().get(Piece.BIG).size(), 3);
        assertEquals(twoPlayers.getPrimaryPieces().get(Piece.HUGE).size(), 3);
        assertEquals(twoPlayers.getSecondaryPieces().get(Piece.BASE).size(), 3);
        assertEquals(twoPlayers.getSecondaryPieces().get(Piece.SMALL).size(), 3);
        assertEquals(twoPlayers.getSecondaryPieces().get(Piece.MEDIUM).size(), 3);
        assertEquals(twoPlayers.getSecondaryPieces().get(Piece.BIG).size(), 3);
        assertEquals(twoPlayers.getSecondaryPieces().get(Piece.HUGE).size(), 3);

        assertEquals(threePlayers.getPrimaryPieces().get(Piece.BASE).size(), 3);
        assertEquals(threePlayers.getPrimaryPieces().get(Piece.SMALL).size(), 3);
        assertEquals(threePlayers.getPrimaryPieces().get(Piece.MEDIUM).size(), 3);
        assertEquals(threePlayers.getPrimaryPieces().get(Piece.BIG).size(), 3);
        assertEquals(threePlayers.getPrimaryPieces().get(Piece.HUGE).size(), 3);
        assertEquals(threePlayers.getSecondaryPieces().get(Piece.BASE).size(), 1);
        assertEquals(threePlayers.getSecondaryPieces().get(Piece.SMALL).size(), 1);
        assertEquals(threePlayers.getSecondaryPieces().get(Piece.MEDIUM).size(), 1);
        assertEquals(threePlayers.getSecondaryPieces().get(Piece.BIG).size(), 1);
        assertEquals(threePlayers.getSecondaryPieces().get(Piece.HUGE).size(), 1);

        assertEquals(fourPlayers.getPrimaryPieces().get(Piece.BASE).size(), 3);
        assertEquals(fourPlayers.getPrimaryPieces().get(Piece.SMALL).size(), 3);
        assertEquals(fourPlayers.getPrimaryPieces().get(Piece.MEDIUM).size(), 3);
        assertEquals(fourPlayers.getPrimaryPieces().get(Piece.BIG).size(), 3);
        assertEquals(fourPlayers.getPrimaryPieces().get(Piece.HUGE).size(), 3);
        assertEquals(fourPlayers.getSecondaryPieces().get(Piece.BASE).size(), 0);
        assertEquals(fourPlayers.getSecondaryPieces().get(Piece.SMALL).size(), 0);
        assertEquals(fourPlayers.getSecondaryPieces().get(Piece.MEDIUM).size(), 0);
        assertEquals(fourPlayers.getSecondaryPieces().get(Piece.BIG).size(), 0);
        assertEquals(fourPlayers.getSecondaryPieces().get(Piece.HUGE).size(), 0);
    }
}

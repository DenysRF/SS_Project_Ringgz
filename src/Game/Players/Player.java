package Game.Players;

import Game.Model.Piece;

import java.util.List;
import java.util.Map;

// Abstract Player class, holds shared data of human- and computer player
public abstract class Player {

    // Name of the player
    protected String name;

    // Array of available pieces consisting of rings and bases
    protected Map<Integer, List<Piece>> primaryPieces;
    protected Map<Integer, List<Piece>> secondaryPieces;

    public Player(String name, int noOfPlayers) {
        this.name = name;
        makePieces(noOfPlayers);
    }

    public String getName() {
        return name;
    }

    public Map<Integer, List<Piece>> getPrimaryPieces() {
        return primaryPieces;
    }

    public Map<Integer, List<Piece>> getSecondaryPieces() {
        return secondaryPieces;
    }

    public boolean gameOver() {
        // TODO
        return false;
    }

    private void makePieces(int noOfPlayers) {
        switch (noOfPlayers) {
            case 2:
                // fill for 2 players
                return;
            case 3:
                // fill for 3 players
                return;
            case 4:
                // fill for 4 players
                return;
        }
    }

    // Tell the player to make a move
    public void makeMove() {
        // TODO
    }

    // Tell the player to set the starting Base
    public void setStart() {
        // TODO
    }
}

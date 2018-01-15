package Players;

import GameObjects.Piece;

// Abstract Player class, holds shared data of human- and computer player
public abstract class Player {

    // Name of the player
    private String name;

    // Array of available pieces consisting of rings and bases
    private Piece[] pieces;

    public Player(String name, Piece[] pieces) {
        this.name = name;
        this.pieces = pieces;
    }

    public String getName() {
        return name;
    }

    public Piece[] getPieces() {
        return pieces;
    }
}

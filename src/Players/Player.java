package Players;

import GameObjects.Piece;

// Abstract Player class, holds shared data of human- and computer player
public abstract class Player {

    // Name of the player
    private String name;

    // Array of available pieces consisting of rings and bases
    private Piece[] primaryPieces;
    private Piece[] secondaryPieces;

    public Player(String name, Piece[] primaryPieces) {
        this.name = name;
        this.primaryPieces = primaryPieces;
    }

    // 3 and 4 player games will have secondarypieces
    public Player(String name, Piece[] primaryPieces, Piece[] secondaryPieces) {
        this.name = name;
        this.primaryPieces = primaryPieces;
        this.secondaryPieces = secondaryPieces;
    }

    public String getName() {
        return name;
    }

    public Piece[] getPrimaryPieces() {
        return primaryPieces;
    }

    public Piece[] getSecondaryPieces() {
        return secondaryPieces;
    }
}

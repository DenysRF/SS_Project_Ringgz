package Players;

import GameObjects.Piece;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, Piece[] pieces) {
        super(name, pieces);
    }

    public HumanPlayer(String name, Piece[] primaryPieces, Piece[] secondaryPieces) {
        super(name, primaryPieces, secondaryPieces);
    }
}

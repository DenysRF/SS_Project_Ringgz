package Players;

import GameObjects.Piece;

import java.util.List;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, List<Piece> pieces) {
        super(name, pieces);
    }

    public HumanPlayer(String name, List<Piece> primaryPieces, List<Piece> secondaryPieces) {
        super(name, primaryPieces, secondaryPieces);
    }
}

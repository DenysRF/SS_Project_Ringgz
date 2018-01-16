package Players;

import GameObjects.Piece;

import java.util.List;

public class ComputerPlayer extends Player {

    public ComputerPlayer(String name, List<Piece> pieces) {
        super(name, pieces);
    }

    public ComputerPlayer(String name, List<Piece> primaryPieces, List<Piece> secondaryPieces) {
        super(name, primaryPieces, secondaryPieces);
    }

}

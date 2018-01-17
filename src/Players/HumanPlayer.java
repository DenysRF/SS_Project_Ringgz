package Players;

import GameObjects.Piece;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, int noOfPlayers) {
        super(name, noOfPlayers);
    }

    public void printPieceCollection() {
        for (Integer p : primaryPieces.keySet()) {
            System.out.println(p + ": " + primaryPieces.get(p).size());
        }
    }
}

package Game.Players;

import Game.Model.Board;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, int noOfPlayers) {
        super(name, noOfPlayers);
    }

    @Override
    public int determineMove(Board board) {
        // TODO ask for move
        return 0;
    }

    public void printPieceCollection(int NoOfPlayers) {
        System.out.println(name + "'s pieces:\nPrimary:");
        for (Integer p : primaryPieces.keySet()) {
            System.out.println("\t" + p + ": " + primaryPieces.get(p).size());
        }
        if (NoOfPlayers != 4) {
            System.out.println("Secondary:");
            for (Integer p : secondaryPieces.keySet()) {
                System.out.println("\t" + p + ": " + secondaryPieces.get(p).size());
            }
        }
    }
}

package Game.Players;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, int noOfPlayers) {
        super(name, noOfPlayers);
    }

    public void printPieceCollection() {
        System.out.println(name + "'s pieces:\nPrimary:");
        for (Integer p : primaryPieces.keySet()) {
            System.out.println("\t" + p + ": " + primaryPieces.get(p).size());
        }
        System.out.println("Secondary:");
        for (Integer p : secondaryPieces.keySet()) {
            System.out.println("\t" + p + ": " + secondaryPieces.get(p).size());
        }
    }
}

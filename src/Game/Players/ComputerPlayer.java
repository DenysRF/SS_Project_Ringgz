package Game.Players;

import Game.Model.Board;

public class ComputerPlayer extends Player {

    public ComputerPlayer(String name, int noOfPlayers) {
        super(name, noOfPlayers);
    }

    @Override
    public int determineMove(Board board) {
        return 0;
    }

}

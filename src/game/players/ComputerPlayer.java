package game.players;

import game.model.Board;
import game.model.Piece;
import game.strategies.Strategy;
import javafx.util.Pair;

public class ComputerPlayer extends Player {

    private Strategy strategy;

    public ComputerPlayer(int noOfPlayers, Strategy strategy) {
        super(strategy.getName(), noOfPlayers);
        this.strategy = strategy;
    }

//    @Override
    public Pair<Integer, Piece> doMove(int noOfPlayers, Board board) {
//        this.printPieceCollection(noOfPlayers);
        return strategy.doMove(noOfPlayers, this, board);
    }

    public Pair<Integer, Piece> doStart() {
        return strategy.setStart();
    }

}

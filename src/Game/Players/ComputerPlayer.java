package Game.Players;

import Game.Model.Board;
import Game.Model.Piece;
import Game.Strategies.Strategy;
import javafx.util.Pair;

public class ComputerPlayer extends Player {

    Strategy strategy;

    public ComputerPlayer(String name, int noOfPlayers, Strategy strategy) {
        super(strategy.getName(), noOfPlayers);
        this.strategy = strategy;
    }

//    @Override
    public Pair<Integer, Piece> doMove(int noOfPlayers, Board board) {
//        this.printPieceCollection(noOfPlayers);
        return strategy.doMove(noOfPlayers, this, board);
    }

//    @Override
    public void setStart(Board board) {
        strategy.setStart(board);
    }



}

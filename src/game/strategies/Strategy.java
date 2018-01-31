package game.strategies;

import game.model.Board;
import game.model.Piece;
import game.players.Player;
import javafx.util.Pair;

public interface Strategy {

    String getName();

    Pair<Integer, Piece> doMove(int noOfPlayers, Player player, Board b);

    Pair<Integer, Piece> setStart();
}

package Game.Strategies;

import Game.Model.Board;
import Game.Model.Piece;
import Game.Players.Player;
import javafx.util.Pair;

public interface Strategy {

    public String getName();
    public Pair<Integer, Piece> doMove(int noOfPlayers, Player player, Board b);

    public void setStart(Board board);
}

package Game.Strategies;

import Game.Model.Board;
import Game.Players.Player;

public interface Strategy {

    public String getName();
    public boolean doMove(int noOfPlayers, Player player, Board b);

    public void setStart(Board board);
}

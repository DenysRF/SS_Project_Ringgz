package Game.Strategies;

import Game.Model.Board;

public interface Strategy {

    public String getName();
    public int[] determineMove(Board b);

}

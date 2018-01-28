package Game.Strategies;

import Game.Model.Board;
import Game.Model.Field;
import Game.Model.Piece;
import Game.Players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RandomStrategy implements Strategy {

    private String name;

    public RandomStrategy() {
        name = "Random";
    }

    public String getName() {
        return name;
    }

    public void setStart(Board board) {
        int randomStart = (int) (Math.random() * 9);
        Piece startBase = new Piece(Piece.START);
        switch (randomStart) {
            case 0:
                board.setField(startBase, 6);
                break;
            case 1:
                board.setField(startBase, 7);
                break;
            case 2:
                board.setField(startBase, 8);
                break;
            case 3:
                board.setField(startBase, 11);
                break;
            case 4:
                board.setField(startBase, 12);
                break;
            case 5:
                board.setField(startBase, 13);
                break;
            case 6:
                board.setField(startBase, 16);
                break;
            case 7:
                board.setField(startBase, 17);
                break;
            case 8:
                board.setField(startBase, 18);
                break;
        }
    }

    public boolean doMove(int noOfPlayers, Player player, Board board) {
        List<Integer> piece = new ArrayList<Integer>();
        boolean move = false;
        boolean color;

        if (noOfPlayers == 4){
            Map<Field, List<Piece>> validMove = player.getValidMoves(player, true, board);
            Set<Field> possibleMoves = validMove.keySet();
        }
        return false;
    }
}

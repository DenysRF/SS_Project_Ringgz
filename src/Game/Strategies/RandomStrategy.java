package Game.Strategies;

import Game.Model.Board;
import Game.Model.Piece;
import Game.Players.Player;

import java.util.ArrayList;
import java.util.List;

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
        if (noOfPlayers == 2)
            while (!move) {
                if (!board.getValidFields(player, true).isEmpty() && !board.getValidFields(player, false).isEmpty()) {
                    color = Math.random() > 0.5;
                    if (color) {
                        int tempField = (int) (board.getValidFields(player, true).size() * Math.random());
                        for (int i = 0; i < Piece.START; i++) {
                            if (player.getSecondaryPieces() != null && player.getPrimaryPieces().containsKey(i) && player.getPrimaryPieces().get(i) != null &&  player.isValidMove(board, tempField, player.getPrimaryPieces().get(i).get(0))) {
                                piece.add(i);
                            }
                        }
                        if (piece.size() != 0) {
                            int chosenPiece = piece.get((int) (Math.random() * piece.size()));
                            player.makeMove(chosenPiece, player.getPrimaryPieces().get(chosenPiece).get(0), board);
                            move = true;
                            break;
                        }
                    } else {
                        int tempField = (int) (board.getValidFields(player, false).size() * Math.random());
                        for (int i = 0; i < Piece.START; i++) {
                            if (player.getSecondaryPieces().containsKey(i) && player.getSecondaryPieces().get(i) != null && player.isValidMove(board, tempField, player.getSecondaryPieces().get(i).get(0))) {
                                piece.add(i);
                            }
                        }
                        if (piece.size() != 0) {
                            int chosenPiece = piece.get((int) (Math.random() * piece.size()));
                            player.makeMove(chosenPiece, player.getSecondaryPieces().get(chosenPiece).get(0), board);
                            move = true;
                            break;
                        }
                    }
                } else if (!board.getValidFields(player, true).isEmpty()) {
                    int tempField = (int) (board.getValidFields(player, true).size() * Math.random());
                    for (int i = 0; i < Piece.START; i++) {
                        if (player.getPrimaryPieces().containsKey(i) && player.getPrimaryPieces().get(i) != null && player.isValidMove(board, tempField, player.getPrimaryPieces().get(i).get(0))) {
                            piece.add(i);
                        }
                    }
                    if (piece.size() != 0) {
                        int chosenPiece = piece.get((int) (Math.random() * piece.size()));
                        player.makeMove(chosenPiece, player.getPrimaryPieces().get(chosenPiece).get(0), board);
                        move = true;
                        return true;
                    }
                } else if (!board.getValidFields(player, false).isEmpty()) {
                    int tempField = (int) (board.getValidFields(player, false).size() * Math.random());
                    for (int i = 0; i < Piece.START; i++) {
                        if (player.getSecondaryPieces().containsKey(i) && player.getSecondaryPieces().get(i) != null && player.isValidMove(board, tempField, player.getSecondaryPieces().get(i).get(0))) {
                            piece.add(i);
                        }
                    }
                    if (piece.size() != 0) {
                        int chosenPiece = piece.get((int) (Math.random() * piece.size()));
                        player.makeMove(chosenPiece, player.getSecondaryPieces().get(chosenPiece).get(0), board);
                        move = true;
                        return true;
                    }
                }
            }

        return false;
    }
}

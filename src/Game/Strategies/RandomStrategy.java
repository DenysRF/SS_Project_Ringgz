package Game.Strategies;

import Game.Model.Board;
import Game.Model.Field;
import Game.Model.Piece;
import Game.Players.Player;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RandomStrategy implements Strategy {

//    protected Map<Integer, List<Piece>> primaryPieces;
//    protected Map<Integer, List<Piece>> secondaryPieces;


    private String name;

    public RandomStrategy(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Pair<Integer, Piece> setStart() {
        int randomStart = (int) (Math.random() * 9);
        Piece startBase = new Piece(Piece.START);
        int index = 12;
        switch (randomStart) {
            case 0:
                index = 6;
                break;
            case 1:
                index = 7;
                break;
            case 2:
                index = 8;
                break;
            case 3:
                index = 11;
                break;
            case 4:
                index = 12;
                break;
            case 5:
                index = 13;
                break;
            case 6:
                index = 16;
                break;
            case 7:
                index = 17;
                break;
            case 8:
                index = 18;
                break;
        }
        return new Pair<>(index, startBase);
    }

    public Pair<Integer, Piece> doMove(int noOfPlayers, Player player, Board board) {

        if (noOfPlayers == 4) {
            Map<Field, List<Piece>> validMove = player.getValidMoves(true, board);
            List<Field> keys = new ArrayList<>(validMove.keySet());
            Field tempField = keys.get((int) (Math.random() * keys.size()));
            Piece tempPiece = validMove.get(tempField).get((int) (Math.random() * validMove.get(tempField).size()));
            int tempIndex = 30;
            for (int i = 0; i < Board.DIM * Board.DIM; i++) {
                if (tempField.equals(board.getField(i))) {
                    tempIndex = i;
                    break;
                }
            }
            return new Pair<>(tempIndex, tempPiece);

        } else if (noOfPlayers == 2 || noOfPlayers == 3) {
            Map<Field, List<Piece>> validMovePrimary = player.getValidMoves(true, board);
            Map<Field, List<Piece>> validMoveSecondary = player.getValidMoves(false, board);

            if (!validMovePrimary.isEmpty() && !validMoveSecondary.isEmpty()) {
                boolean isPrimeColour = Math.random() < 0.5;
                Map<Field, List<Piece>> validMove = player.getValidMoves(isPrimeColour, board);
                List<Field> keys = new ArrayList<>(validMove.keySet());
                Field tempField = keys.get((int) (Math.random() * keys.size()));
                Piece tempPiece;
                tempPiece = validMove.get(tempField).get((int) (Math.random() * validMove.get(tempField).size()));
                int tempIndex = 30;
                for (int i = 0; i < Board.DIM * Board.DIM; i++) {
                    if (tempField.equals(board.getField(i))) {
                        tempIndex = i;
                        break;
                    }
                }
                return new Pair<>(tempIndex, tempPiece);
            }
            if (!validMovePrimary.isEmpty()) {
                Map<Field, List<Piece>> validMove = player.getValidMoves(true, board);
                List<Field> keys = new ArrayList<>(validMove.keySet());
                Field tempField = keys.get((int) (Math.random() * keys.size()));
                Piece tempPiece = validMove.get(tempField).get((int) (Math.random() * validMove.get(tempField).size()));
                int tempIndex = 30;
                for (int i = 0; i < Board.DIM * Board.DIM; i++) {
                    if (tempField.equals(board.getField(i))) {
                        tempIndex = i;
                        break;
                    }
                }
                return new Pair<>(tempIndex, tempPiece);
            }
            if (!validMoveSecondary.isEmpty()) {
                Map<Field, List<Piece>> validMove = player.getValidMoves(false, board);
                List<Field> keys = new ArrayList<>(validMove.keySet());
                Field tempField = keys.get((int) (Math.random() * keys.size()));
                Piece tempPiece = validMove.get(tempField).get((int) (Math.random() * validMove.get(tempField).size()));
                int tempIndex = 30;
                for (int i = 0; i < Board.DIM * Board.DIM; i++) {
                    if (tempField.equals(board.getField(i))) {
                        tempIndex = i;
                        break;
                    }
                }
                return new Pair<>(tempIndex, tempPiece);
            }
        }
        return null;
    }

//    public void printPieceCollection(int NoOfPlayers) {
//        System.out.println(name + "'s pieces:\nPrimary:");
//        for (Integer p : primaryPieces.keySet()) {
//            System.out.println("\t" + p + ": " + primaryPieces.get(p).size());
//        }
//        if (NoOfPlayers != 4) {
//            System.out.println("Secondary:");
//            for (Integer p : secondaryPieces.keySet()) {
//                System.out.println("\t" + p + ": " + secondaryPieces.get(p).size());
//            }
//        }
//    }
}

package Game.Players;

import Game.Model.Board;
import Game.Model.Field;
import Game.Model.Piece;
import javafx.util.Pair;

import java.util.*;

import static Game.Model.Board.DIM;

// Abstract Player class, holds shared data of human- and computer player
public abstract class Player extends Observable {

    // Name of the player
    protected String name;

    // Array of available pieces consisting of rings and bases
    protected Map<Integer, List<Piece>> primaryPieces;
    protected Map<Integer, List<Piece>> secondaryPieces;

    //max amount of copies of each piece
    protected static final int MAX_PER_PIECE = 3;

    public Player(String name, int noOfPlayers) {
        this.name = name;
        makePieces(noOfPlayers);
    }

    public String getName() {
        return name;
    }

    public Map<Integer, List<Piece>> getPrimaryPieces() {
        return primaryPieces;
    }

    public Map<Integer, List<Piece>> getSecondaryPieces() {
        return secondaryPieces;
    }

    private void makePieces(int noOfPlayers) {
        List<Piece> pBase = new ArrayList<>();
        List<Piece> pSmall = new ArrayList<>();
        List<Piece> pMedium = new ArrayList<>();
        List<Piece> pBig = new ArrayList<>();
        List<Piece> pHuge = new ArrayList<>();
        List<Piece> sBase = new ArrayList<>();
        List<Piece> sSmall = new ArrayList<>();
        List<Piece> sMedium = new ArrayList<>();
        List<Piece> sBig = new ArrayList<>();
        List<Piece> sHuge = new ArrayList<>();

        // Every Player always gets a full primary collection
        primaryPieces = new HashMap<>();
        for (int i = 0; i < MAX_PER_PIECE; i++) {
            pBase.add(new Piece(Piece.BASE, true, this));
            pSmall.add(new Piece(Piece.SMALL, true, this));
            pMedium.add(new Piece(Piece.MEDIUM, true, this));
            pBig.add(new Piece(Piece.BIG, true, this));
            pHuge.add(new Piece(Piece.HUGE, true, this));
        }

        switch (noOfPlayers) {
            case 2: // fill for 2 players
                secondaryPieces = new HashMap<>();
                for (int i = 0; i < MAX_PER_PIECE; i++) {
                    sBase.add(new Piece(Piece.BASE, false, this));
                    sSmall.add(new Piece(Piece.SMALL, false, this));
                    sMedium.add(new Piece(Piece.MEDIUM, false, this));
                    sBig.add(new Piece(Piece.BIG, false, this));
                    sHuge.add(new Piece(Piece.HUGE, false, this));

                    secondaryPieces.put(Piece.BASE, sBase);
                    secondaryPieces.put(Piece.SMALL, sSmall);
                    secondaryPieces.put(Piece.MEDIUM, sMedium);
                    secondaryPieces.put(Piece.BIG, sBig);
                    secondaryPieces.put(Piece.HUGE, sHuge);
                }
                break;
            case 3: // fill for 3 players
                secondaryPieces = new HashMap<>();
                sBase.add(new Piece(Piece.BASE, false, this));
                sSmall.add(new Piece(Piece.SMALL, false, this));
                sMedium.add(new Piece(Piece.MEDIUM, false, this));
                sBig.add(new Piece(Piece.BIG, false, this));
                sHuge.add(new Piece(Piece.HUGE, false, this));

                secondaryPieces.put(Piece.BASE, sBase);
                secondaryPieces.put(Piece.SMALL, sSmall);
                secondaryPieces.put(Piece.MEDIUM, sMedium);
                secondaryPieces.put(Piece.BIG, sBig);
                secondaryPieces.put(Piece.HUGE, sHuge);
                break;
            case 4:
                //secondaryPieces = new HashMap<>();
                break;
        }

        primaryPieces.put(Piece.BASE, pBase);
        primaryPieces.put(Piece.SMALL, pSmall);
        primaryPieces.put(Piece.MEDIUM, pMedium);
        primaryPieces.put(Piece.BIG, pBig);
        primaryPieces.put(Piece.HUGE, pHuge);
    }

    // Tell the player to make a move
    public void makeMove(int i, Piece p, Board board) {
        board.setField(p, i);
        if (p.isPrimary()) {
            primaryPieces.get(p.getSize()).remove(0);
        } else {
            secondaryPieces.get(p.getSize()).remove(0);
        }
        setChanged();
        notifyObservers();
    }

//    public boolean makeMove(int noOfPlayers, Board board) {
//        Pair<Integer, Piece> chosenMove = null;
//        while (chosenMove == null) {
//
//            chosenMove = doMove(noOfPlayers, board);
//        }
//        chosenMove.getKey();
//        if (board.setField(chosenMove.getValue(), chosenMove.getKey())) {
//            if (chosenMove.getValue().isPrimary()) {
//                primaryPieces.get(chosenMove.getValue().getSize()).remove(0);
//            } else {
//                secondaryPieces.get(chosenMove.getValue().getSize()).remove(0);
//            }
//            setChanged();
//            notifyObservers();
//            return true;
//        }
//        System.err.println("this is an invalid move");
//        System.out.println("place: " + chosenMove.getKey() + "piece: " + chosenMove.getValue().getSize());
//        return false;
//    }


    public boolean validStart(int i) {
        return (i >= (1 + DIM) && i <= 3 + DIM || i >= 1 + 2 * DIM && i <= 3 + 2 * DIM || i >= 1 + 3 * DIM && i <= 3 + 3 * DIM);
    }

    // Tell the player to set the starting Base
    public void setStart(int i, Board board) {
        // start only at middle fields
        if (validStart(i)) {
            Piece startBase = new Piece(Piece.START);
            board.setField(startBase, i);
        }
        setChanged();
        notifyObservers();
    }

//    public abstract Pair<Integer, Piece> doMove(int noOfPlayers, Board board);


    public boolean isValidMove(Board board, int temp, Piece piece) {
        boolean noAdjecentBase = true;
        for (int i = 0; i < board.getAdjacentFields(board.getField(temp)).size(); i++) {
            if (board.getAdjacentFields(board.getField(temp)).get(i).getFieldContent()[Piece.BASE] != null && board.getAdjacentFields(board.getField(temp)).get(i).getFieldContent()[Piece.BASE].getOwner() == this &&
                    board.getAdjacentFields(board.getField(temp)).get(i).getFieldContent()[Piece.BASE].isPrimary() == piece.isPrimary()) {
                noAdjecentBase = false;
                break;
            }
        }
        return noAdjecentBase;
    }

    public boolean isValidMove(Board board, Field field, Boolean color) {
        boolean adjacentBase = false;
        List<Field> adjacentField = board.getAdjacentFields(field);
        for (int i = 0; i < board.getAdjacentFields(field).size(); i++) {
            if (adjacentField.get(i).getFieldContent()[Piece.BASE] != null && adjacentField.get(i).getFieldContent()[Piece.BASE].getOwner() == this &&
                    adjacentField.get(i).getFieldContent()[Piece.BASE].isPrimary() == color) {
                adjacentBase = true;
                break;
            }
        }
        return adjacentBase;
    }

    public Map<Field, List<Piece>> getValidMoves(Boolean color, Board board) {

        Map<Field, List<Piece>> possibleMoves = new HashMap<>();
        //iterate over the fields
        List<Field> validFields = board.getValidFields(this, color);

        for (Field field : validFields) {
            List<Piece> pieces = new ArrayList<>();
            boolean hasAdjacentBase;
            List<Field> fields = board.getAdjacentFields(field);
            //iterate over the pieces
//            for (Field ForField: fields) {
//                if (ForField.getFieldContent()[Piece.BASE] != null && ForField.getFieldContent()[Piece.BASE].getOwner() == this && ForField.getFieldContent()[Piece.BASE].isPrimary() == color) {
//                    hasAdjacentBase = true;
//                }
//            }
            hasAdjacentBase = isValidMove(board, field, color);
            for (int size = 0; size < Piece.START; size++) {
                if ((size == 0 && hasAdjacentBase)) {
                } else if (color) {
                    if (!this.getPrimaryPieces().isEmpty() && this.getPrimaryPieces().containsKey(size) && !this.getPrimaryPieces().get(size).isEmpty() && this.getPrimaryPieces().get(size).get(0) != null) {
                        if (((field.getFieldContent()[size] == null) && (field.getFieldContent()[Piece.START] == null) && (field.getFieldContent()[Piece.BASE] == null)) && size != 0 || size == 0 && field.isEmpty()) {
                            pieces.add(this.getPrimaryPieces().get(size).get(0));
                        }
                    }
                } else {
                    if (!this.getSecondaryPieces().isEmpty() && this.getSecondaryPieces().containsKey(size) && !this.getSecondaryPieces().get(size).isEmpty() && this.getSecondaryPieces().get(size).get(0) != null) {
                        if (field.getFieldContent()[size] == null && field.getFieldContent()[Piece.START] == null && field.getFieldContent()[Piece.BASE] == null && size != 0 || ((size == 0) && field.isEmpty())) {
                            pieces.add(this.getSecondaryPieces().get(size).get(0));
                        }
                    }
                }

            }
//            boolean test = (!pieces.isEmpty());
            if (pieces.size() > 0) {
                possibleMoves.put(field, pieces);
            }
        }
        return possibleMoves;
    }

    public void printPieceCollection(int NoOfPlayers) {
        System.out.println(name + "'s pieces:\nPrimary:");
        for (Integer p : primaryPieces.keySet()) {
            System.out.println("\t" + p + ": " + primaryPieces.get(p).size());
        }
        if (NoOfPlayers != 4) {
            System.out.println("Secondary:");
            for (Integer p : secondaryPieces.keySet()) {
                System.out.println("\t" + p + ": " + secondaryPieces.get(p).size());
            }
        }
        setChanged();
        notifyObservers();
    }

    public int ringCount() {
        int count = 0;
        for (Integer i : primaryPieces.keySet()) {
            count += primaryPieces.get(i).size();
        }
        if (secondaryPieces != null) {
            for (Integer i : primaryPieces.keySet()) {
                count += primaryPieces.get(i).size();
            }
        }
        return count;
    }
}

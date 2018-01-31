package Game.Players;

import Game.Model.Board;
import Game.Model.Field;
import Game.Model.Piece;

import java.util.*;

import static Game.Model.Board.DIM;

// Abstract Player class, holds shared data of human- and computer player
public abstract class Player extends Observable {

    // Name of the player
    protected String name;

    // Array of available pieces consisting of rings and bases
    private Map<Integer, List<Piece>> primaryPieces;
    private Map<Integer, List<Piece>> secondaryPieces;

    //max amount of copies of each piece
    private static final int MAX_PER_PIECE = 3;

    public Player(String name, int noOfPlayers) {
        this.name = name;
        makePieces(noOfPlayers);
    }

    //@ ensures \result == name;
    /*@pure*/

    /**
     * This method returns the name of the player
     * @return String name
     */
    public String getName() {
        return name;
    }

    //@ ensures \result == primaryPieces;
    /*@pure*/

    /**
     * this returns a map of all the primary pieces
     * @return Map<Integer, List<Piece>> primaryPieces
     */
    public Map<Integer, List<Piece>> getPrimaryPieces() {
        return primaryPieces;
    }

    //@ ensures \result == secondaryPieces;
    /*@pure*/
    /**
     * this returns a map of all the decondary pieces
     * @return Map<Integer, List<Piece>> secondaryPieces
     */
    public Map<Integer, List<Piece>> getSecondaryPieces() {
        return secondaryPieces;
    }

    //@ requires noOfPlayers == 2 || noOfPlayers == 1 || noOfPlayers == 4;
    //@ ensures if (noOfPlayers == 2 | noOfPlayers == 3) {!getPrimaryPieces.isEmpty() && !getSecondaryPieces.isEmptie()};
    //@ensures if (noOfPlayers == 4) { !getPrimaryPieces.isEmpty && getSecondaryPieces.isEmpty();
    /**
     * this method creates the Piece arrays in different ways
     * for eacht amount of players
     * 2 players: a primary and secondary map is created
     * 3 players: primary pieces created and 1 of each secondary piece is created
     * 4 players: only the primary pieces map is created
     * @param noOfPlayers the number of players playing the game
     */
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
    //@requires p.getOwner.equals(this);
    //@requires p != null && i != null && board != null;
    //@requires piece.getOwner().equals(this);
    //@ensures this.getPrimaryPieces().equals(\old(this.getPrimaryPieces())) || this.getSecondaryPieces().equals(\old(this.getSecondaryPieces()));

    /**
     * This method sets a field and removes the piece from the corresponding piece map
     * @param i the index
     * @param p the piece which will be placed on the index
     * @param board the board on which the piece will be placed
     */
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


    //@requires i != null;
    /*@pure*/

    /**
     * this method checks if the given index is one of the middle 9 fields
     * @param i index
     * @return true if the the index is a valid start field
     */
    public boolean validStart(int i) {
        return i >= (1 + DIM) && i <= 3 + DIM || i >= 1 + 2 * DIM && i <= 3 + 2 * DIM ||
                i >= 1 + 3 * DIM && i <= 3 + 3 * DIM;
    }

    // Tell the player to set the starting Base
    //@requires i != null;
    //@requires board != null;
    //@requires validStart(i);

    /**
     * places the start piece on the board if the index is a valid start field
     * @param i the index of the specified field
     * @param board the board on which the start piece will be put
     */
    public void setStart(int i, Board board) {
        // start only at middle fields
        if (validStart(i)) {
            Piece startBase = new Piece(Piece.START);
            board.setField(startBase, i);
        }
        setChanged();
        notifyObservers();
    }

    //@requires board != null;
    //@requires field != null;
    //@requires piece != piece;
    //@requires piece.getOwner().equals(this);
    //@requires !field.isEmpty();
    /*@pure*/

    /**
     * this method returns true if a move is valid when looking if the piece fits on the field
     * and if you want to place a base and there is no base of
     * the same color in an adjacent field
     * @param board the board on which we check if there is a valid move
     * @param field the field for which we check if a piece can fit
     * @param color to specify whether piece is this players primary or secondary color
     * @return true if a piece from this player of said color fits in the field
     */
    private boolean isValidMove(Board board, Field field, Boolean color) {
        boolean adjacentBase = false;
        List<Field> adjacentField = board.getAdjacentFields(field);
        for (Field anAdjacentField : adjacentField) {
            if (anAdjacentField.getFieldContent()[Piece.BASE] != null &&
                    anAdjacentField.getFieldContent()[Piece.BASE].getOwner() == this &&
                    anAdjacentField.getFieldContent()[Piece.BASE].isPrimary() == color) {
                adjacentBase = true;
                break;
            }
        }
        return adjacentBase;
    }


    //@requires color != null;
    //@requires board != null;
    /*@pure*/

    /**
     * this method creates a map with all possible moves
     * @param color to specify whether piece is this players primary or secondary color
     * @param board the board on which we check if there is a valid move
     * @return a Map in the form Map<Field, List<Piece>>, a field with all the possible pieces that are available
     */
    public Map<Field, List<Piece>> getValidMoves(Boolean color, Board board) {

        Map<Field, List<Piece>> possibleMoves = new HashMap<>();
        //iterate over the fields
        List<Field> validFields = board.getValidFields(this, color);

        for (Field field : validFields) {
            List<Piece> pieces = new ArrayList<>();
            boolean hasAdjacentBase;
            //iterate over the pieces
            hasAdjacentBase = isValidMove(board, field, color);
            for (int size = 0; size < Piece.START; size++) {
                // TODO: rewrite to eliminate empty statement
                if ((size == 0 && hasAdjacentBase)) {}
                else {
                    if (color) {
                        Map<Integer, List<Piece>> primePiece = this.getPrimaryPieces();
                        if (!primePiece.isEmpty() && primePiece.containsKey(size) &&
                                !primePiece.get(size).isEmpty() &&
                                primePiece.get(size).get(0) != null) {
                            if (((field.getFieldContent()[size] == null) &&
                                    (field.getFieldContent()[Piece.START] == null) &&
                                    (field.getFieldContent()[Piece.BASE] == null)) &&
                                    size != 0 || size == 0 && field.isEmpty()) {
                                pieces.add(this.getPrimaryPieces().get(size).get(0));
                            }
                        }
                    } else {
                        Map<Integer, List<Piece>> secondPiece = this.getSecondaryPieces();
                        if (!secondPiece.isEmpty() &&
                                secondPiece.containsKey(size) &&
                                !secondPiece.get(size).isEmpty() &&
                                secondPiece.get(size).get(0) != null) {
                            if (((field.getFieldContent()[size] == null) &&
                                    (field.getFieldContent()[Piece.START] == null) &&
                                    (field.getFieldContent()[Piece.BASE] == null)) &&
                                    size != 0 || size == 0 && field.isEmpty()) {
                                pieces.add(this.getSecondaryPieces().get(size).get(0));
                            }
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

    //@requires noOfPlayers == 2 || noOfPlayers == 3 || noOfPlayers ==4;
    /*@pure*/
    public void printPieceCollection(int noOfPlayers) {
        System.out.println(name + "'s pieces:\nPrimary:");
        for (Integer p : primaryPieces.keySet()) {
            System.out.println("\t" + p + ": " + primaryPieces.get(p).size());
        }
        if (noOfPlayers != 4) {
            System.out.println("Secondary:");
            for (Integer p : secondaryPieces.keySet()) {
                System.out.println("\t" + p + ": " + secondaryPieces.get(p).size());
            }
        }
        setChanged();
        notifyObservers();
    }

    //@requires primaryPieces != null
    /*@pure*/
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

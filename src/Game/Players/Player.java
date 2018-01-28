package Game.Players;

import Game.Model.Board;
import Game.Model.Field;
import Game.Model.Piece;

import java.util.*;

import static Game.Model.Board.DIM;

// Abstract Player class, holds shared data of human- and computer player
public abstract class Player {

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
                secondaryPieces = new HashMap<>();
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
    }

    public void makeMove(Field field, Piece piece, Board board) {
        board.setField(piece, field);
        if (piece.isPrimary()) {
            primaryPieces.get(piece.getSize()).remove(0);
        } else {
            secondaryPieces.get(piece.getSize()).remove(0);
        }
    }


    public boolean validStart(int i) {
        return (i >= (1 + DIM) && i <= 3 + DIM || i >= 1 + 2 * DIM && i <= 3 + 2 * DIM || i >= 1 + 3 * DIM && i <= 3 + 3 * DIM);
    }

    // Tell the player to set the starting Base
    public void setStart(Board board) {
    }

    public boolean doMove(int noOfPlayers, Board board) {
        return false;
    }

    ;

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

    public boolean isValidMove(Board board, Field field, Piece piece) {
        boolean noAdjecentBase = true;
        for (int i = 0; i < board.getAdjacentFields(field).size(); i++) {
            if (board.getAdjacentFields(field).get(i).getFieldContent()[Piece.BASE] != null && board.getAdjacentFields(field).get(i).getFieldContent()[Piece.BASE].getOwner() == this &&
                    board.getAdjacentFields(field).get(i).getFieldContent()[Piece.BASE].isPrimary() == piece.isPrimary()) {
                noAdjecentBase = false;
                break;
            }
        }
        return noAdjecentBase;
    }

    public Map<Field, List<Piece>> getValidMoves(Boolean color, Board board) {
        List<Piece> pieces = new ArrayList<>();
        Map<Field, List<Piece>> possibleMoves = new HashMap<>();

        for (int i = 0; i < board.getValidFields(this, color).size(); i++) {
            pieces.clear();
            boolean hasAdjacentBase = false;
            Field field = board.getValidFields(this, color).get(i);
            List<Field> fields = board.getAdjacentFields(field);
            for (int j = 0; j < fields.size(); j++) {
                if (fields.get(j).getFieldContent()[Piece.BASE] != null && fields.get(j).getFieldContent()[Piece.BASE].getOwner() == this && fields.get(j).getFieldContent()[Piece.BASE].isPrimary() == color) {
                    hasAdjacentBase = true;
                }
            }
            for (int size = 0; size < Piece.START; size++) {
                if (size == 0 && hasAdjacentBase) {
                } else {
                    if (color) {
                        if (!this.getPrimaryPieces().isEmpty() && this.getPrimaryPieces().containsKey(size) && !this.getPrimaryPieces().get(size).isEmpty()) {
                            pieces.add(this.getPrimaryPieces().get(size).get(0));
                        }
                    } else {
                        if (!this.getSecondaryPieces().isEmpty() && this.getSecondaryPieces().containsKey(size) && !this.getSecondaryPieces().get(size).isEmpty()) {
                            pieces.add(this.getSecondaryPieces().get(size).get(0));
                        }
                    }
                }
            }
//            boolean test = (!pieces.isEmpty());
            if (!pieces.isEmpty()) {
                possibleMoves.put(field, pieces);
            }
        }

        return possibleMoves;
    }
}

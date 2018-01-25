package Game.Players;

import Game.Model.Board;
import Game.Model.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public abstract int determineMove(Board board);


    public boolean validStart(int i) {
        return (i >= (1 + DIM) && i <= 3 + DIM || i >= 1 + 2 * DIM && i <= 3 + 2 * DIM || i >= 1 + 3 * DIM && i <= 3 + 3 * DIM);
    }

    // Tell the player to set the starting Base
    public void setStart(int i, Board board) {
        // start only at middle fields
        if (i >= (1 + DIM) && i <= 3 + DIM || i >= 1 + 2 * DIM && i <= 3 + 2 * DIM || i >= 1 + 3 * DIM && i <= 3 + 3 * DIM) {
            Piece startBase = new Piece(Piece.START);
            board.setField(startBase, i);
        }
    }
}

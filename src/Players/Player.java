package Players;

import GameObjects.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Abstract Player class, holds shared data of human- and computer player
public abstract class Player {

    // Name of the player
    protected String name;

    // Array of available pieces consisting of rings and bases
    protected Map<Integer, List<Piece>> primaryPieces = new HashMap<>();
    protected Map<Integer, List<Piece>> secondaryPieces = new HashMap<>();

    //max amount of copies of each piece
    protected int maxPerPiece = 3;


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

    public boolean gameOver() {
        // TODO
        return false;
    }

    public void makePieces(int noOfPlayers) {
        List<Piece> Base = new ArrayList<>();
        List<Piece> Small = new ArrayList<>();
        List<Piece> Medium = new ArrayList<>();
        List<Piece> Big = new ArrayList<>();
        List<Piece> Huge = new ArrayList<>();
        List<Piece> Base2 = new ArrayList<>();
        List<Piece> Small2 = new ArrayList<>();
        List<Piece> Medium2 = new ArrayList<>();
        List<Piece> Big2 = new ArrayList<>();
        List<Piece> Huge2 = new ArrayList<>();


        for (int i = 0; i < maxPerPiece; i++) {
            Base.add(new Piece(0, true));
            Small.add(new Piece(1, true));
            Medium.add(new Piece(2, true));
            Big.add(new Piece(3, true));
            Huge.add(new Piece(4, true));
        }
        switch (noOfPlayers) {
            case 2:

                for (int i = 0; i < maxPerPiece; i++) {
                    Base2.add(new Piece(0, false));
                    Small2.add(new Piece(1, false));
                    Medium2.add(new Piece(2, false));
                    Big2.add(new Piece(3, false));
                    Huge2.add(new Piece(4, false));
                }
                break;
            case 3:

                Base2.add(new Piece(0, false));
                Small2.add(new Piece(1, false));
                Medium2.add(new Piece(2, false));
                Big2.add(new Piece(3, false));
                Huge2.add(new Piece(4, false));
                break;
            case 4:
                //only the first thing happens, so nothing is really implemented here
                //except for the fact that we can catch the 4 player option
                break;
        }
        primaryPieces.put(0, Base);
        primaryPieces.put(1, Small);
        primaryPieces.put(2, Medium);
        primaryPieces.put(3, Big);
        primaryPieces.put(4, Huge);
        secondaryPieces.put(0, Base2);
        secondaryPieces.put(1, Small2);
        secondaryPieces.put(2, Medium2);
        secondaryPieces.put(3, Big2);
        secondaryPieces.put(4, Huge2);
    }

    // Tell the player to make a move
    public void makeMove() {
        // TODO
    }

    // Tell the player to set the starting Base
    public void setStart() {
        // TODO
    }
}
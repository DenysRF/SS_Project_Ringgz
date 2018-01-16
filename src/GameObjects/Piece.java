package GameObjects;

import Players.Player;

public class Piece {


    private Player owner;
    private int size;
    private int colour;
    public static final int BASE = 0;
    public static final int SMALL = 1;
    public static final int MEDIUM = 2;
    public static final int BIG = 3;
    public static final int HUGE = 4;
    public static final int START = 5;


    public Piece(Player owner, int Size, int colour) {
        this.owner = owner;
        this.size = size;
        this.colour = colour;
    }

    public Player getOwner() {
        return owner;
    }

    public int getSize() {
        return size;
    }

    public int getColour() {
        return colour;
    }

}

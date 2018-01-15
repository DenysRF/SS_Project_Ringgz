package GameObjects;

import Players.Player;

public abstract class Piece {

    private Player owner;
    private int size;
    private int colour;

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

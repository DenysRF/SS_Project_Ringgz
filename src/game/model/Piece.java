package game.model;

import game.players.Player;

public class Piece {

    public static final int BASE = 0;
    public static final int SMALL = 1;
    public static final int MEDIUM = 2;
    public static final int BIG = 3;
    public static final int HUGE = 4;
    public static final int START = 5;

    private Player owner;
    private int size;
    private boolean primary;

    public Piece(int size, boolean primary, Player owner) {
        this.size = size;
        this.primary = primary;
        this.owner = owner;
    }

    // Only used for Start
    public Piece(int size) {
        this.size = size;
    }

    public Player getOwner() {
        return owner;
    }

    public int getSize() {
        return size;
    }

    public boolean isPrimary() {
        return primary;
    }
}

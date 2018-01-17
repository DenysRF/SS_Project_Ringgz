package Game.Model;

import Game.Players.Player;

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

    public Piece(int size, boolean primary) {
        this.size = size;
        this.primary = primary;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getSize() {
        return size;
    }

    public boolean isPrimary() {
        return primary;
    }
}

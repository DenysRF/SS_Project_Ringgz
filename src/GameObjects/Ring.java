package GameObjects;

import Players.Player;

public class Ring extends Piece {

    public static final int SMALL = 1;
    public static final int MEDIUM = 2;
    public static final int LARGE = 3;
    public static final int HUGE = 4;

    private int size;

    public Ring(Player owner, int size) {
        super(owner);
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}

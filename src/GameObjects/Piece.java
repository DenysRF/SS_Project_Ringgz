package GameObjects;

import Players.Player;

public abstract class Piece {

    private Player owner;

    public Piece(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}

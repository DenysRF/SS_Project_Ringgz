package GameObjects;

import Players.Player;

public class Board {

    public static final int DIM = 5;

    private Field[] fields;

    // Constructor fills the board with empty fields
    public Board() {
        fields = new Field[DIM*DIM];
        for (int i = 0; i < DIM*DIM; i++) {
            fields[i] = new Field();
        }
    }

    // Convert coordinates into index
    public int index(int x, int y) {
        // TODO
        return 0;
    }

    // Return the field at index i
    public Field getField(int i) {
        // TODO
        return null;
    }

    // Return the field at coordinates x,y
    public Field getField(int x, int y) {
        // TODO
        return null;
    }


    // Set a piece in a field at index i
    public void setField(Piece piece, int i) {
        Field field = getField(i);
        if (field.isValidMove(piece)) {

        }
    }

    /*
    Return Field[] where a player given their pieces
    can place a piece
     */
    public Field[] getValidFields(Player player) {
        for (Field f : fields) {

        }
        return null;
    }

    // Return true if no players are able to make a move
    public boolean gameOver() {
        // TODO
        return false;
    }

    // Return the current point score of a player
    public int getScore(Player player) {
        // TODO
        return 0;
    }
}

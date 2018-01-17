package Game.Model;

import Game.Players.Player;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int DIM = 5;

    private Field[] fields;
    private Player[] players;

        // Constructor fills the board with empty fields
    public Board(Player[] players) {
            fields = new Field[DIM*DIM];
            this.players = players;
            for (int i = 0; i < DIM*DIM; i++) {
                fields[i] = new Field();
            }
        }

        // Convert coordinates into index
        public int index(int x, int y) {
            return x * DIM + y;
        }

        // Return the field at index i
        public Field getField(int i) {
            return fields[i];
        }

        // Return the field at coordinates x,y
        public Field getField(int x, int y) {
            return fields[index(x, y)];
    }


    // Set a piece in a field at index i
    public void setField(Piece piece, int i) {
        if (getField(i).isValidMove(piece)) {
            fields[i].setFieldContent(piece);
        }
    }

    public List<Field> getAdjacentFields(Field field) {
        List<Field> adjacentFields = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].equals(field)) {

                //adjacentFields.add(fields[])
            }
        }
        return adjacentFields;
    }

    /*
    Return Field[] where a player given their pieces
    can place a piece
     */
    public List<Field> getValidFields(Player player) {
        List<Field> validFields = new ArrayList<>();
        return validFields;
    }

    // Return true if no players are able to make a move
    public boolean gameOver() {
        for (Player p : players) {
            if (!p.gameOver()) {
                return false;
            }
        }
        return true;
    }

    // Return the current point score of a player
    public int getScore(Player player) {
        int score = 0;
        for (Field f : fields) {
            if (f.majority().equals(player)) {
                score++;
            }
        }
        return score;
    }

    // Reset this board
    public void reset() {
        fields = new Field[DIM*DIM];
        for (int i = 0; i < DIM*DIM; i++) {
            fields[i] = new Field();
        }
    }

    public Player[] getPlayers() {
        return players;
    }
}

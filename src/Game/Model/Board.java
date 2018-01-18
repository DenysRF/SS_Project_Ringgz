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
        fields = new Field[DIM * DIM];
        this.players = players;
        for (int i = 0; i < DIM * DIM; i++) {
            fields[i] = new Field();
        }
    }

    // Convert coordinates into index
    public int index(int x, int y) {
        return x + (y * DIM);
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
        //if (getField(i).isValidMove(piece)) {
            fields[i].setFieldContent(piece);
        //}
    }

    // Get the all fields that make up the "territory of a player"
    public Field[] getAdjacentFields(Field field) {
        // TODO
        Field[] adjacentFields = null;
        // Look for field that matches argument
        for (int x = 0; x < DIM; x++) {
            for (int y = 0 ; y < DIM; y++) {
                if (getField(x, y).equals(field)) {
                    // TODO Initialize Field[]
                    // and get adjacent fields

                }
            }
        }

//        for (int i = 0; i < fields.length; i++) {
//            if (fields[i].equals(field)) {
//
//                adjacentFields.
//            }
//        }
        return adjacentFields;
    }

    /*
    Return Field[] where a player given their pieces
    can place a piece
     */
    public Field[] getValidFields(Player player) {
        // TODO
        Field[] validFields = null;
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
        fields = new Field[DIM * DIM];
        for (int i = 0; i < DIM * DIM; i++) {
            fields[i] = new Field();
        }
    }

    public Player[] getPlayers() {
        return players;
    }

    public void printBoard(Player[] players, String[][] colors) {
        for (int i = 0; i < DIM * DIM; i++) {
            System.out.print("[");

            // A field can hold 6 different pieces
            // iterators: i for fields in board, j for pieces in field
            // and o comparing player[] with the owner of the piece
            // "_" for empty space
            for (int j = 0; j < 6; j++) {
                String mark = "_";
                // Start Base has no owner and is stored at index 5
                if (fields[i].getFieldContent()[j] != null) {
                    if (j == 5) {
                        if (fields[i].getFieldContent()[j] != null) {
                            mark = "O";
                        }
                    } else {
                        for (int o = 0; o < players.length; o++) {
                            if (fields[i].getFieldContent()[j].getOwner() == players[o]) {
                                if (fields[i].getFieldContent()[j].isPrimary()) {
                                    mark = colors[o][0];
                                } else if (!fields[i].getFieldContent()[j].isPrimary()) {
                                    mark = colors[o][1];
                                }
                            }
                        }
                    }
                }
                System.out.print(mark);
                if (!(j % 6 == 6 - 1)) {
                    System.out.print(".");
                }
            }
            if (i % DIM == DIM - 1) {
                System.out.println("]");
            } else {
                System.out.print("]");
            }
        }
    }
}

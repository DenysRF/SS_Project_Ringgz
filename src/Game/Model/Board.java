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
    //@ requires x > DIM;
    //@ requires y > DIM;
    //@ensures \result < DIM8DIM;
    /*@pure*/

    /**
     * This method calculates the index when one is using a x,y coordinate system.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the index in a normal int
     */
    public int index(int x, int y) {
        return x + (y * DIM);
    }

    // Return the field at index i
    //@requires i<DIM*DIM;
    //@ensures \result != null;
    /*@pure*/

    /**
     * this method returns the field on index i.
     * @param i index of the field[]
     * @return field this returns a field if the int i is in the field[]
     */
    public Field getField(int i) {
        if (fields.length > i) {
            return fields[i];
        }
        return null;
    }

    // Return the field at coordinates x,y
    //@requires x < DIM;
    //@requires y < DIM;
    /*@pure*/

    /**
     * this method returns the field on x,y.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return field this returns a field if the int i is in the field[]
     */
    public Field getField(int x, int y) {
        return fields[index(x, y)];
    }


    // Set a piece in a field at index i
    //@requires piece != null && i < DIM*DIM;

    /**
     * this method sets a field if it is a valid move.
     * @param piece the piece chosen by the player
     * @param i the index of field[]
     * @return boolean; true if the field has been set
     */
    public boolean setField(Piece piece, int i) {
        if (getField(i).isValidMove(piece)) {
            fields[i].setFieldContent(piece);
            return true;
        } else {
            return false;
        }
    }


    // Get the all fields that make up the "territory of a player"
    //@requires field != null;
    //@ensures \result != null;
    /*@pure*/

    /**
     * this method returns all the adjacent fields of a specified field
     * @param field a field of which you want to get the adjacent fields
     * @return List<Field> field, this is a list which contains
     * all fields which are adjacent to field
     */
    public List<Field> getAdjacentFields(Field field) {
        List<Field> adjacentFields = new ArrayList<>();
        // Look for field that matches argument

        //possibly we want to refactor this code
        for (int x = 0; x < DIM; x++) {
            for (int y = 0; y < DIM; y++) {
                if (getField(x, y).equals(field)) {
                    if (x == 0 && y == 0) {
                        adjacentFields.add(getField(x, y));
                        adjacentFields.add(getField(x, y + 1));
                        adjacentFields.add(getField(x + 1, y));
                    } else if (x == 0 && (y > 0 && y < DIM - 1)) {
                        adjacentFields.add(getField(x, y - 1));
                        adjacentFields.add(getField(x, y));
                        adjacentFields.add(getField(x, y + 1));
                        adjacentFields.add(getField(x + 1, y));
                    } else if ((x > 0 && x < DIM - 1) && y == 0) {
                        adjacentFields.add(getField(x - 1, y));
                        adjacentFields.add(getField(x, y));
                        adjacentFields.add(getField(x + 1, y));
                        adjacentFields.add(getField(x, y + 1));
                    } else if (x == DIM - 1 && y == 0) {
                        adjacentFields.add(getField(DIM - 2, 0));
                        adjacentFields.add(getField(DIM - 1, 0));
                        adjacentFields.add(getField(DIM - 1, 1));
                    } else if (x == 0 && y == DIM - 1) {
                        adjacentFields.add(getField(x, y - 1));
                        adjacentFields.add(getField(x, y));
                        adjacentFields.add(getField(x + 1, y));
                    } else if (x == DIM - 1 && (y > 0 && y < DIM - 1)) {
                        adjacentFields.add(getField(x, y - 1));
                        adjacentFields.add(getField(x, y));
                        adjacentFields.add(getField(x, y + 1));
                        adjacentFields.add(getField(x - 1, y));
                    } else if ((x > 0 && x < DIM - 1) && y == DIM - 1) {
                        adjacentFields.add(getField(x - 1, y));
                        adjacentFields.add(getField(x, y));
                        adjacentFields.add(getField(x + 1, y));
                        adjacentFields.add(getField(x, y - 1));
                    } else if (x == DIM - 1 && y == DIM - 1) {
                        adjacentFields.add(getField(x - 1, y));
                        adjacentFields.add(getField(x, y));
                        adjacentFields.add(getField(x, y - 1));
                    } else if (x != 0 && x != DIM - 1 && y != 0 && y != DIM - 1) {
                        adjacentFields.add(getField(x, y - 1));
                        adjacentFields.add(getField(x - 1, y));
                        adjacentFields.add(getField(x, y));
                        adjacentFields.add(getField(x + 1, y));
                        adjacentFields.add(getField(x, y + 1));
                    }

                }
            }
        }
        return adjacentFields;
    }


    /*
    Return Field[] where a player given their pieces
    can place a piece
     */
    //@ensures \!result.isEmpty;
    /*@pure*/

    /**
     * this method returns all field a player could potentially place
     * his pieces, not all rules are checked yet.
     * @param player this is the player for who the valdFields are generated
     * @param color true if you chose the first color of player
     * @return a list of all possible fields
     */
    public List<Field> getValidFields(Player player, boolean color) {
        List<Field> validFields = new ArrayList<>();
        boolean add = true;
        //first you iterate over every field
        for (int x = 0; x < DIM; x++) {
            for (int y = 0; y < DIM; y++) {
                //now you iterate over every piece in a field
                for (int i = 0; i < Field.MAX_SPACE; i++) {
                    if ((getField(x, y).getFieldContent()[i] != null) &&
                            ((getField(x, y).getFieldContent()[i].getSize() == Piece.START) ||
                             (getField(x, y).getFieldContent()[i].getOwner().equals(player) &&
                              getField(x, y).getFieldContent()[i].isPrimary() == color))) {
                        for (int k = 0; k < getAdjacentFields(getField(x, y)).size(); k++) {
                            for (Field validField : validFields) {
                                if (validField.equals(getAdjacentFields(getField(x, y)).
                                        get(k))) {
                                    add = false;
                                }
                            }
                            if (add) {
                                validFields.add(getAdjacentFields(getField(x, y)).get(k));
                            }
                            add = true;
                        }
                    }
                }
            }
        }
        return validFields;
    }

    // Return true if this Player cannot make any moves
    public boolean gameOver(Player player) {
        return player.getValidMoves(true, this).isEmpty() &&
                player.getValidMoves(false, this).isEmpty();
    }

    // Return the current point score of a player
    /*@pure*/

    /**
     * this method returns the score of a player by testing the majority.
     * for each method
     * @param player the current player
     * @return the score of this player
     */
    public int getScore(Player player) {
        int score = 0;
        for (Field f : fields) {
            if (f.majority(players.length) != null && f.majority(players.length).equals(player)) {
                score++;
            }
        }
        return score;
    }

    // Reset this board

    /**
     * this method resets the board
     */
    public void reset() {
        fields = new Field[DIM * DIM];
        for (int i = 0; i < DIM * DIM; i++) {
            fields[i] = new Field();
        }
    }

    /*@pure*/

    /**
     * this method is used to get all the current players.
     * @return the array which contains all the current players
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * this method prints the current board.
     * (unused since GUI implementation)
     * @param activePlayers all the players which are in this game
     * @param colors A string presentation of all the color
     */
    public void printBoard(Player[] activePlayers, String[][] colors) {
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
                            if (fields[i].getFieldContent()[j].getOwner() == activePlayers[o]) {
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
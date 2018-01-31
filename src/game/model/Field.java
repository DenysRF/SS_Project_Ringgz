package game.model;

import game.players.HumanPlayer;
import game.players.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Field {

    public static final int MAX_SPACE = 6;

    private Piece[] fieldContent;

    // Constructor initializes fieldContent at size MAX_SPACE with values null
    public Field() {
        fieldContent = new Piece[MAX_SPACE];
        Arrays.fill(fieldContent, null);
    }

    /**
     * This method is created to returns a piece array which are present in the field
     * @return Piece[]
     */
    // Return the contents of a field
    //@ensures \result == fieldContent;
    /*@ pure*/
    public Piece[] getFieldContent() {
        return fieldContent;
    }

    // Set the content of a field
    //@ requires piece != null;
    //@ensures getFieldContent().contains(piece);

    /**
     * This method places a piece in this field if there is space for this piece.
     * @param piece the piece to be placed in the field
     * this is a piece which has an owner, boolean to select the color and the size
     */
    public void setFieldContent(Piece piece) {
        if (isValidMove(piece)) {
            fieldContent[piece.getSize()] = piece;
        }
    }

    // Checks whether the piece fits in the field
    //@ requires piece != null;
    // /*@ pure*/

    /**
     * this method returns true if a specified piece can be placed in this field.
     * @param piece the piece you want put in this field
     * @return true if the piece can be placed
     */
    public boolean isValidMove(Piece piece) {
        return isEmpty() || piece.getSize() == Piece.BASE && isEmpty() ||
                piece.getSize() != Piece.BASE &&
                ((fieldContent[piece.getSize()] == null) &&
                (fieldContent[Piece.BASE] == null) &&
                (fieldContent[Piece.START] == null));
    }


    // Returns true if a field is empty
    //@requires fieldContent!= null;
    //@ensures \result == fieldContend.isempty;
    /*@ pure*/

    /**
     * this method returns if the field is empty.
     * @return true if the field indeed is empty
     */
    public boolean isEmpty() {
        for (Piece p : fieldContent) {
            if (p != null) {
                return false;
            }
        }
        return true;
    }

    // Returns the player with the majority of the field or null
    //@requires noOfPlayers == 2 || noOfPlayers == 3 || noOfPlayers == 4;
    /*@ pure*/

    /**
     * This method checks who is the winner of a field.
     * @param noOfPlayers the number of players playing the game
     * @return Player || null. player if there is a winner, null if there is no winner on this field
     */
    public Player majority(int noOfPlayers) {

        Player owner;
        Player extraColour = new HumanPlayer("extraColour", 3);
        int colour;
        int tempHighscore = 0;
        Player tempWinner = null;
        boolean hasWinner = false;

        if (noOfPlayers == 2 || noOfPlayers == 4) {
            Map<Player, int[]> score = new HashMap<>();

            if (fieldContent[Piece.BASE] == null && fieldContent[Piece.START] == null) {
                for (int count = 1; count < MAX_SPACE - 1; count++) {
                    if (fieldContent[count] != null) {
                        owner = fieldContent[count].getOwner();
                        if (fieldContent[count].isPrimary()) {
                            colour = 0;
                        } else {
                            colour = 1;
                        }
                        if (score.containsKey(owner)) {
                            int[] tempscore = score.get(owner);
                            tempscore[colour] = tempscore[colour] + 1;
                            score.put(owner, tempscore);
                        } else {
                            int[] tempscore = {0, 0};
                            tempscore[colour] = 1;
                            score.put(owner, tempscore);
                        }
                    }
                }

                for (Player p : score.keySet()) {
                    for (int i = 0; i < 2; i++) {
                        if (score.get(p)[i] > tempHighscore) {
                            tempWinner = p;
                            tempHighscore = score.get(tempWinner)[i];
                            hasWinner = true;
                        } else if (score.get(p)[i] == tempHighscore) {
                            hasWinner = false;
                        }
                    }
                }


            }

        } else if (noOfPlayers == 3) {
            Map<Player, Integer> score = new HashMap<>();
            if (fieldContent[Piece.BASE] == null && fieldContent[Piece.START] == null) {
                for (int count = 1; count < MAX_SPACE - 1; count++) {
                    if (fieldContent[count] != null && fieldContent[count].getOwner() != null) {
                        owner = fieldContent[count].getOwner();
                        if (fieldContent[count].isPrimary()) {
                            if (score.containsKey(owner)) {
                                score.put(owner, score.get(owner) + 1);
                            } else {
                                score.put(owner, 1);
                            }
                        } else if (!fieldContent[count].isPrimary()) {
                            if (score.containsKey(extraColour)) {
                                score.put(extraColour, score.get(extraColour) + 1);
                            } else {
                                score.put(extraColour, 1);
                            }
                        }
                    }
                }
            }

            for (Player p : score.keySet()) {
                for (int i = 0; i <= 1; i++) {
                    if (score.get(p) > tempHighscore && !p.getName().equals("extraColour")) {
                        tempWinner = p;
                        tempHighscore = score.get(p);
                        hasWinner = true;
                    } else if (score.get(p) == tempHighscore) {
                        hasWinner = false;
                    }
                }
            }
        }
        if (hasWinner) {
            return tempWinner;
        } else {
            return null;
        }
    }
}

package Game.Model;

import Game.Players.HumanPlayer;
import Game.Players.Player;

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

    // Return the contents of a field
    public Piece[] getFieldContent() {
        return fieldContent;
    }

    // Set the content of a field
    public void setFieldContent(Piece piece) {
        if (isValidMove(piece)) {
            fieldContent[piece.getSize()] = piece;
        }
    }

    // Checks whether the piece fits in the field
    public boolean isValidMove(Piece piece) {
        if (isEmpty() || ((fieldContent[piece.getSize()] == null) && (fieldContent[Piece.BASE] == null) && (fieldContent[Piece.START] == null))) {
            return true;
        } else {
            return false;
        }
    }


    // Returns true if a field is empty
    public boolean isEmpty() {
        for (Piece p : fieldContent) {
            if (!(p == null)) {
                return false;
            }
        }
        return true;
    }

    // Returns the player with the majority of the field or null
    public Player majority(int noOfPlayers) {

        Player owner;
        Player extraColour = new HumanPlayer("extraColour", 3);
        int colour;
        int[] amount = {0, 0};
        int tempHighscore = 0;
        boolean hasWinner = false;
        Player tempWinner = null;

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
                            int[] tempscore = amount;
                            tempscore[colour] = 1;
                            score.put(owner, tempscore);
                        }
                    }
                }

                for (Player p : score.keySet()) {
                    for (int i = 0; i <= 1; i++)
                        if (score.get(p)[i] > tempHighscore) {
                            tempWinner = p;
                            tempHighscore = score.get(p)[i];
                            hasWinner = true;
                        } else if (score.get(p)[i] == tempHighscore) {
                            hasWinner = false;
                        }
                }


            }

        } else if (noOfPlayers == 3) {
            Map<Player, Integer> score = new HashMap<>();
            if (fieldContent[Piece.BASE] == null && fieldContent[Piece.START] == null) {
                for (int count = 1; count < MAX_SPACE - 1; count++) {
                    if (fieldContent[count] != null) {
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
                for (int i = 0; i <= 1; i++)
                    if (score.get(p) > tempHighscore && !p.getName().equals("extraColour")) {
                        tempWinner = p;
                        tempHighscore = score.get(p);
                        hasWinner = true;
                    } else if (score.get(p) == tempHighscore) {
                        hasWinner = false;
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

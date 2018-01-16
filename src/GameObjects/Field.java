package GameObjects;

import Players.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Field {

    public static final int MAX_SPACE = 6;

    private Piece[] fieldContent;
    private int place;

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
        place = piece.getSize();
        if (isValidMove(piece)) {
            fieldContent[place] = piece;
        }
    }


    // Checks whether the piece fits in the field
    public boolean isValidMove(Piece piece) {
        place = piece.getSize();
        if (isEmpty() || ((fieldContent[place] == null) && (fieldContent[Piece.BASE] == null) && (fieldContent[Piece.START] == null))) {
            return true;
        } else {
            return false;
        }
    }


    // Returns true if a field is empty
    public boolean isEmpty() {
        for (Piece p : fieldContent) {
            if (!p.equals(null)) {
                return false;
            }
        }
        return true;
    }

    // Returns the player with the majority of the field or null
    public Player majority() {
        Player owner;
        int colour;
        int[] amount = {0, 0};
        Map<Player, int[]> score = new HashMap<>();
        int tempHighscore = 0;
        Player tempwinner = null;
        boolean hasWinner = false;

        if (fieldContent[Piece.BASE].equals(null) && fieldContent[Piece.START].equals(null)) {
            for (int count = 1; count < MAX_SPACE - 1; count++) {
                if (!fieldContent[count].getOwner().equals(null)) {
                    owner = fieldContent[count].getOwner();
                    colour = fieldContent[count].getColour();
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
                        tempwinner = p;
                        tempHighscore = score.get(p)[i];
                        hasWinner = true;
                    } else if (score.get(p)[i] == tempHighscore) {
                        hasWinner = false;
                    }
            }


            if (hasWinner) {
                return tempwinner;
            } else {
                return null;
            }

        }else{
            return null;
        }
    }
}

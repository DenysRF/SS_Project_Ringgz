package GameObjects;

import Players.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Field {

    public static final int MAX_SPACE = 5;

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
        if (fieldContent[place].equals(null)) {
            fieldContent[place] = piece;
        }
    }


    // Checks whether the piece fits in the field
    public boolean isValidMove(Piece piece) {
        place = piece.getSize();
        if (isEmpty() || fieldContent[place].equals(null) && fieldContent[0].equals(null)) {
            return true;
        } else {
            return false;
        }
    }

    // Checks if anything fits in the field at all
    public boolean isFull() {
        int count = 0;
        for (Piece p : fieldContent) {
            if (p instanceof Base) {
                return true;
            }
            if (p instanceof Piece) {
                count++;
            }
        }
        if (count == MAX_SPACE) {
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        int count = 0;
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
        String player;
        int[] amount = new int[2];
        Map<Player, int[]> score= new HashMap<Player, int[]>();

        int count = 1;
        while (count < MAX_SPACE) {
           owner = fieldContent[count].getOwner();
           colour = fieldContent[count].getColour();
           if(!score.get(owner).equals(null)){
               int[] tempscore = score.get(owner);
               tempscore[colour] = tempscore[colour]+1;
               score.put(owner, tempscore);
           }else{
               int[] tempscore = amount;
               tempscore[colour] = 1;
               score.put(owner, tempscore);
           }
        }
        /// TODO: 15-Jan-18 itterate over hashmap to define winner 
        return null;
    }

}

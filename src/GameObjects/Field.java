package GameObjects;

import Players.Player;

import java.util.Arrays;

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
        fieldContent[0] = piece;
    }

    // Checks whether the piece fits in the field
    public boolean isValidMove(Piece piece) {
        // TODO
        return true;
    }

    // Checks if anything fits in the field at all
    public boolean isFull() {
        int count = 0;
        for (Piece p : fieldContent) {
            count++;
            if (p.getSize() == Piece.BASE || p.getSize() == Piece.START) {
                return true;
            }
        }
        if (count == MAX_SPACE) {
            return true;
        }
        return false;
    }

    // Returns the player with the majority of the field or null
    public Player majority() {
        // TODO
        return null;
    }
}

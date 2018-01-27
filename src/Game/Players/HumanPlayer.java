package Game.Players;

import Game.Model.Board;
import Game.Model.Piece;

import java.util.Scanner;

import static Game.Model.Board.DIM;

public class HumanPlayer extends Player {
    private static Scanner in;

    public HumanPlayer(String name, int noOfPlayers) {
        super(name, noOfPlayers);
        in = new Scanner(System.in);
    }

    public void printPieceCollection(int NoOfPlayers) {
        System.out.println(name + "'s pieces:\nPrimary:");
        for (Integer p : primaryPieces.keySet()) {
            System.out.println("\t" + p + ": " + primaryPieces.get(p).size());
        }
        if (NoOfPlayers != 4) {
            System.out.println("Secondary:");
            for (Integer p : secondaryPieces.keySet()) {
                System.out.println("\t" + p + ": " + secondaryPieces.get(p).size());
            }
        }
    }

//    public int[] determineMove(Board b){
//
//    }

    private static void print(String s) {
        System.out.println(s);
    }

    public Piece choosePiece() {
        int color = -1;
        int size = -1;
        boolean check = false;
        while (!(color == 0 || color == 1)) {
            print("Choose a Piece from your collection\n\tPrimary or secondary?(0/1)");
            if (in.hasNextInt()) {
                color = (in.nextInt());
                if (!(color == 0 || color == 1)) {
                    print("Enter '1' for your primary color and '2' for secondary");
                }
            } else {
                in.nextLine();
                print("please input a number \n");
            }
        }
        while (!(size >= 0 && size <= 4)) {
            print("Choose the size of the Piece\n\t" +
                    "(Base = 0, Small = 1, Medium = 2, Big = 3, Huge = 4)");
            if (in.hasNextInt()) {
                size = in.nextInt();
                if (!(size >= 0 && size <= 4)) {
                    print("Wrong input, (0, 1, 2, 3, 4)");
                }
            } else {
                in.nextLine();
                print("please input a number \n");
            }
        }

        Piece piece = null;

        if (color == 0 && this.getPrimaryPieces().get(size) != null) {
            piece = this.getPrimaryPieces().get(size).get(0);
        } else if (color == 1 && this.getSecondaryPieces().get(size) != null) {
            piece = this.getSecondaryPieces().get(size).get(0);
        }
        return piece;
    }


    public static int readMove() {
        boolean check = false;
//        print("Type index of where you want to place the piece: ");
        while (!check) {
            if (in.hasNextLine() && in.hasNextInt()) {
                check = true;
                return in.nextInt();
            } else {
                in.nextLine();
                System.err.println("please input a number \n");
            }
        }
        return -1;
    }

//    @Override
    public void setStart(Board board) {
        int temp = readMove();
        while (!this.validStart(temp)) {
            System.err.println("please enter a valid start position");
            temp = readMove();
        }
        // start only at middle fields
        if (temp >= (1 + DIM) && temp <= 3 + DIM || temp >= 1 + 2 * DIM && temp <= 3 + 2 * DIM || temp >= 1 + 3 * DIM && temp <= 3 + 3 * DIM) {
            Piece startBase = new Piece(Piece.START);
            board.setField(startBase, temp);
        }
    }
//    @Override
    public boolean doMove(int noOfPlayers, Board board) {
        boolean noAdjecentBase = true;
        this.printPieceCollection(noOfPlayers);
        Piece piece = this.choosePiece();

        print("Type index of where you want to place the piece: ");
        int temp = this.readMove();

        //this for loop tests if at least one adjacent field has a Base of the same colour
       noAdjecentBase = isValidMove(board, temp, piece);

        if (noAdjecentBase && (board.getField(temp) != null && board.getField(temp).isValidMove(piece) && board.getValidFields(this, piece.isPrimary()).contains(board.getField(temp)))) {

            this.makeMove(temp, piece, board);
            return false;
        } else {
            System.err.println("This is not a valid move, try again");
        }
        return true;
    }
}

package Game;

import Game.Model.Board;
import Game.Model.Piece;
import Game.Players.HumanPlayer;
import Game.Players.Player;

import java.util.Scanner;

public class Ringgz {

    private static void print(String s) {
        System.out.println(s);
    }

    // TODO check if the field is valid
    private static int readMove(Scanner in) {
//        print("Type index of where you want to place the piece: ");
        return Integer.parseInt(in.nextLine());
    }

    // TODO check if piece in collection
    private static Piece choosePiece(Scanner in, Player p) {
        int color = -1;
        int size = -1;
        while (!(color == 0 || color == 1)) {
            print("Choose a Piece from your collection\n\tPrimary or secondary?(0/1)");
            color = Integer.parseInt(in.nextLine());
            if (!(color == 0 || color == 1)) {
                print("Enter '1' for your primary color and '2' for secondary");
            }
        }
        while (!(size >= 0 && size <= 4)) {
            print("Choose the size of the Piece\n\t" +
                    "(Base = 0, Small = 1, Medium = 2, Big = 3, Huge = 4)");
            size = Integer.parseInt(in.nextLine());
            if (!(size >= 0 && size <= 4)) {
                print("Wrong input, (0, 1, 2, 3, 4)");
            }
        }

        Piece piece = null;

        if (color == 0) {
            piece = p.getPrimaryPieces().get(size).get(0);
        } else if (color == 1) {
            piece = p.getSecondaryPieces().get(size).get(0);
        }

        return piece;
    }


    public static void main(String[] args) {
        if (args.length < 2 || args.length > 4) {
            System.err.println("Give player names as arguments\nA game can only feature 2, 3 or 4 players");
            System.exit(0);
        }

        // Only HumanPlayers for now
        HumanPlayer[] players = new HumanPlayer[args.length];


        // Define Colors client side
        int sets = 2;
        if (args.length == 4) {
            sets = 1;
        }
        // Two-dimensional array [index of Player][0 for primary / 1 for secondary]
        String[][] colors = new String[args.length][sets];
        for (int i = 0; i < players.length; i++) {
            String p = "X";
            String s = "x";
            // Uppercase for primary, lowercase for secondary
            switch (i) {
                case 0:
                    p = "R";
                    s = "r";
                    break;
                case 1:
                    p = "B";
                    s = "b";
                    break;
                case 2:
                    p = "Y";
                    s = "y";
                    break;
                case 3:
                    p = "G";
                    s = "r";
                    break;
            }
            colors[i][0] = p;
            colors[i][1] = s;
        }

        Scanner in = new Scanner(System.in);

        boolean playAgain = true;
        while (playAgain) {
            for (int i = 0; i < players.length; i++) {
                players[i] = new HumanPlayer(args[i], players.length);
            }

            // Construct Board
            Board board = new Board(players);
            // Index of Player[] whose turn it is
            int currentPlayer = 0;

            // Announce colors
            for (int i = 0; i < players.length; i++) {
                print(players[i].getName() + " is color:\n\tprimary " + colors[i][0] + "\n\tsecondary " + colors[i][1]);
            }

            // First Player will set the StartBase
            board.printBoard(players, colors);
            print(players[currentPlayer].getName() + " may set the StartBase");
            int temp = readMove(in);
            while (!players[currentPlayer].validStart(temp)) {
                print("plese enter a valid start position");
                temp = readMove(in);
            }
            players[currentPlayer].setStart(temp, board);
            currentPlayer = (currentPlayer + 1) % players.length;

            // Play game until the Game is over
            boolean turn = true;
            while (!board.gameOver()) {
                if (!board.gameOver(players[currentPlayer])) {
                    while (turn) {
                        board.printBoard(players, colors);
                        print(players[currentPlayer].getName() + "'s turn");

                        players[currentPlayer].printPieceCollection();
                        Piece piece = choosePiece(in, players[currentPlayer]);
                        // TODO check if move is valid
                        print("Type index of where you want to place the piece: ");
                        temp = readMove(in);
                        if (board.getField(temp).isValidMove(piece) && board.getValidFields(players[currentPlayer]).contains(board.getField(temp))) {
                            players[currentPlayer].makeMove(temp, piece, board);
                            turn = false;
                        } else {
                            print("This is not a valid move, try again");
                        }

                    }
                    turn = true;
                }

                currentPlayer = (currentPlayer + 1) % players.length;
            }

            // Determine score and winner
            print("Scores:");

            for (int i = 0; i < players.length; i++) {
                int score = board.getScore(players[i]);
                print("\t" + players[i].getName() + ": " + score);
            }

            boolean answered = true;
            while (answered) {
                print("Play again? (y/n)");
                String s;
                s = in.nextLine();
                switch (s) {
                    case "y":
                        print("Playing again");
                        board.reset();
                        answered = false;
                        break;
                    case "n":
                        answered = false;
                        playAgain = false;
                        break;
                    default:
                        print("Re-type your answer");
                        break;
                }
            }
        }

        print("End of session");
        in.close();
    }
}

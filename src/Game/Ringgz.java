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


    private static int readMove(Scanner in) {
        boolean check = false;
//        print("Type index of where you want to place the piece: ");
        while (!check) {
            if (in.hasNextInt()) {
                check = true;
                return Integer.parseInt(in.nextLine());
            } else {
                in.nextLine();
                System.err.println("please input a number \n");
            }
        }
        return -1;
    }

    private static Piece choosePiece(Scanner in, Player p) {
        int color = -1;
        int size = -1;
        boolean check = false;
        while (!(color == 0 || color == 1)) {
            print("Choose a Piece from your collection\n\tPrimary or secondary?(0/1)");
            if (in.hasNextInt()) {
                color = Integer.parseInt(in.nextLine());
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
                size = Integer.parseInt(in.nextLine());
                if (!(size >= 0 && size <= 4)) {
                    print("Wrong input, (0, 1, 2, 3, 4)");
                }
            } else {
                in.nextLine();
                print("please input a number \n");
            }
        }

        Piece piece = null;

        if (color == 0 && p.getPrimaryPieces().get(size) != null) {
            piece = p.getPrimaryPieces().get(size).get(0);
        } else if (color == 1 && p.getSecondaryPieces().get(size) != null) {
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
            sets = 2;
        }
        // Two-dimensional array [index of Player][0 for primary / 1 for secondary]
        String[][] colors = new String[args.length][sets];
        initializePlayerColours(players, colors);

        Scanner in = new Scanner(System.in);

        boolean playAgain = true;
        playGame(args, players, colors, in, playAgain);

        print("End of session");
        in.close();
    }

    private static void playGame(String[] args, HumanPlayer[] players, String[][] colors, Scanner in, boolean playAgain) {
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
                print("please enter a valid start position");
                temp = readMove(in);
            }
            players[currentPlayer].setStart(temp, board);
            currentPlayer = (currentPlayer + 1) % players.length;

            // Play game until the Game is over
            boolean turn = true;
            doMove(players, colors, in, board, currentPlayer, turn);

            // Determine score and winner
            print("Scores:");

            for (HumanPlayer player : players) {
                int score = board.getScore(player);
                print("\t" + player.getName() + ": " + score);
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
    }

    private static void doMove(HumanPlayer[] players, String[][] colors, Scanner in, Board board, int currentPlayer, boolean turn) {
        int temp;
        boolean noAdjecentBase = true;
        while (!board.gameOver()) {
            if (!board.gameOver(players[currentPlayer])) {
                while (turn) {
                    board.printBoard(players, colors);
                    print(players[currentPlayer].getName() + "'s turn");

                    players[currentPlayer].printPieceCollection(players.length);
                    Piece piece = choosePiece(in, players[currentPlayer]);
                    print("Type index of where you want to place the piece: ");
                    temp = readMove(in);

                    //this for loop tests if at least one adjacent field has a Base of the same colour
                    for (int i = 0; i < board.getAdjacentFields(board.getField(temp)).size(); i++) {
                        if (board.getAdjacentFields(board.getField(temp)).get(i).getFieldContent()[Piece.BASE] != null && board.getAdjacentFields(board.getField(temp)).get(i).getFieldContent()[Piece.BASE].getOwner() == players[currentPlayer] &&
                                board.getAdjacentFields(board.getField(temp)).get(i).getFieldContent()[Piece.BASE].isPrimary() == piece.isPrimary()) {
                            noAdjecentBase = false;
                            break;
                        }
                    }

                    if (noAdjecentBase && (board.getField(temp) != null && board.getField(temp).isValidMove(piece) && board.getValidFields(players[currentPlayer], piece.isPrimary()).contains(board.getField(temp)))) {

                        players[currentPlayer].makeMove(temp, piece, board);
                        turn = false;
                    } else {
                        System.err.println("This is not a valid move, try again");
                    }

                }
                turn = true;
            }

            currentPlayer = (currentPlayer + 1) % players.length;
        }
    }

    private static void initializePlayerColours(HumanPlayer[] players, String[][] colors) {
        for (int i = 0; i < players.length; i++) {
            String p = "X";
            String s = "x";
            // Uppercase for primary, lowercase for secondary
            if (players.length != 3) {
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
            } else {
                switch (i) {
                    case 0:
                        p = "R";
                        s = "X";
                        break;
                    case 1:
                        p = "B";
                        s = "X";
                        break;
                    case 2:
                        p = "Y";
                        s = "X";
                        break;
                    case 3:
                        p = "G";
                        s = "X";
                        break;
                }
                colors[i][0] = p;
                colors[i][1] = s;
            }
        }
    }
}

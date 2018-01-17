package Game;

import Game.Model.Board;
import Game.Players.HumanPlayer;
import Game.Players.Player;

import java.util.Scanner;

public class Ringgz {

    public static void print(String s) {
        System.out.println(s);
    }


    public static void main(String[] args) {


        if (args.length < 2 || args.length > 4) {
            System.err.println("Give player names as arguments\nA game can only feature 2, 3 or 4 players");
            System.exit(0);
        }

        // Only HumanPlayers for now
        HumanPlayer[] players = new HumanPlayer[args.length];

        // TODO implement colors prints
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
                print(players[0] + " is color:\n\tprimary " + colors[i][0] + "\n\tsecondary "+ colors[i][1]);
            }

            // First Player will set the StartBase
            board.printBoard();
            print(players[currentPlayer].getName() + " may set the StartBase");
            players[currentPlayer].setStart();
            currentPlayer = (currentPlayer + 1) % players.length;

            // Play game until the Game is over
            while (!board.gameOver()) {
                if (!players[currentPlayer].gameOver()) {
                    board.printBoard();
                    print(players[currentPlayer].getName() + "'s turn");
                    players[currentPlayer].printPieceCollection();
                    players[currentPlayer].makeMove();
                }
                currentPlayer = (currentPlayer + 1) % players.length;
            }

            // Determine score and winner
            print("Scores:");

            for (int i = 0; i < players.length; i++) {
                int score = board.getScore(players[i]);
                print(players[i].getName() + ": " + score);
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

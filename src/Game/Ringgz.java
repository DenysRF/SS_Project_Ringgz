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
        }

        // Only HumanPlayers for now
        Player[] players = new Player[args.length];

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

            while (true) {
                print("Play again? (y/n)");
                String s;
                s = in.nextLine();
                switch (s) {
                    case "y":
                        print("Playing again");
                        break;
                    case "n":
                        playAgain = false;
                        break;
                    default:
                        print("Re-type your answer");
                }
            }
        }
        print("End of session");
        in.close();
    }
}

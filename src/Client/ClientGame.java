package Client;

import Game.Model.Board;
import Game.Model.Piece;
import Game.Players.ComputerPlayer;
import Game.Players.HumanPlayer;
import Game.Players.Player;
import Game.Strategies.RandomStrategy;
import Game.Strategies.Strategy;
import javafx.util.Pair;

public class ClientGame {

    private Player[] players;
    private Board board;
    private String[][] colors;


    public ClientGame(String[] names) {
        players = new Player[names.length];
        for (int i = 0; i < players.length; i++) {
            if (names[i].contains("BOT")) {
                players[i] = new ComputerPlayer(names[i], players.length, new RandomStrategy(names[i]));
            } else {
                players[i] = new HumanPlayer(names[i], players.length);
            }
        }
        board = new Board(players);

        colors = new String[players.length][2];
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


    public void doneMove(String name, int x, int y, int size, int color) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                if (size == Piece.START) {
                    player.setStart(board.index(x, y), board);
                } else {
                    if (color == 0) {
                        player.makeMove(board.index(x, y), player.getPrimaryPieces().get(size).get(0), board);
                    } else if (color == 1) {
                        player.makeMove(board.index(x, y), player.getSecondaryPieces().get(size).get(0), board);
                    }
                }
            }
        }
    }

    public String getPieces(String name) {
        StringBuilder primaryPieceString = new StringBuilder("Primary pieces:");
        StringBuilder secondaryPieceString = new StringBuilder("Secondary pieces:");
        for (Player p : players) {
            if (p.getName().equals(name)) {
                for (int i = 0; i < 5; i++) {
                    primaryPieceString.append("\n\t").append(i).append(": ").append(p.getPrimaryPieces().get(i).size());
                    if (p.getSecondaryPieces() != null) {
                        secondaryPieceString.append("\n\t").append(i).append(": ").append(p.getSecondaryPieces().get(i).size());
                    }
                }
            }
        }
        return primaryPieceString.toString() + "\n" + secondaryPieceString.toString();
    }

    public String updateField(int i) {
        StringBuilder fieldString = new StringBuilder();
        for (int j = 0; j < 6; j++) {
            String mark = "_";
            // Start Base has no owner and is stored at index 5
            if (board.getField(i).getFieldContent()[j] != null) {
                if (j == 5) {
                    if (board.getField(i).getFieldContent()[j] != null) {
                        mark = "O";
                    }
                } else {
                    for (int o = 0; o < players.length; o++) {
                        if (board.getField(i).getFieldContent()[j].getOwner() == players[o]) {
                            if (board.getField(i).getFieldContent()[j].isPrimary()) {
                                mark = colors[o][0];
                            } else if (!board.getField(i).getFieldContent()[j].isPrimary()) {
                                mark = colors[o][1];
                            }
                        }
                    }
                }
            }
            fieldString.append(mark);
            if (!(j % 6 == 6 - 1)) {
                fieldString.append(".");
            }
        }
        return fieldString.toString();
    }

    public void setObservers(GameGUI gameGUI) {
        for (int i = 0; i < players.length; i++) {
            players[i].addObserver(gameGUI);
        }
    }

    public Pair<Integer, Piece> askBotMove(String name, boolean first) {
        for (Player p : players) {
            if (name.equals(p.getName())) {
                if (first) {
                    return ((ComputerPlayer) p).doStart();
                } else {
                    return ((ComputerPlayer) p).doMove(players.length, board);
                }

            }
        }
        return null;
    }
}

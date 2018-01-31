package Server;

import Game.Model.Board;
import Game.Model.Field;
import Game.Model.Piece;
import Game.Players.HumanPlayer;
import Game.Players.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerGame extends Thread {

    private Map<Player, ClientHandler> playerMap;
    private List<Player> notGameOver;
    private Board board;
    private Player currentPlayer;
    private boolean start;
    private Server server;


    public ServerGame(List<ClientHandler> chList, Server server) {

        this.server = server;

        playerMap = new HashMap<>();
        notGameOver = new ArrayList<>();

        // Error wrong player size
        if (chList.size() < 2 || chList.size() > 4) {
            System.err.println("Give player names as arguments\n" +
                    "A game can only feature 2, 3 or 4 players");
            System.exit(0);
        }

        // Create HumanPlayers for each ClientHandler and map them
        // And fill not game over -list
        for (ClientHandler ch : chList) {
            Player p = new HumanPlayer(ch.getClientName(), chList.size());
            playerMap.put(p, ch);
            notGameOver.add(p);
        }


        // Create Player[] from playerMap for Board argument
        int i = 0;
        Player[] boardArg = new Player[playerMap.keySet().size()];
        for (Player player : playerMap.keySet()) {
            boardArg[i] = player;
            i++;
        }
        // Create Board
        board = new Board(boardArg);
    }

    public void doneMove(String name, int x, int y, int size, int color) {
        // Check if command came from player whose turn it is

        if (start) {
            if (!(currentPlayer.getName().equals(name))) {
                for (Player p : playerMap.keySet()) {
                    if (p.getName().equals(name)) {
                        playerMap.get(p).sendError(ClientHandler.NOT_YOUR_TURN, "");
                        return;
                    }
                }
            }
            if (size != Piece.START) {
                for (Player p : playerMap.keySet()) {
                    if (p.getName().equals(name)) {
                        playerMap.get(p).sendError(ClientHandler.INVALID_MOVE,
                                "Expected: Start Base");
                        return;
                    }
                }
            } else {
                for (Player p : playerMap.keySet()) {
                    if (p.getName().equals(name)) {
                        if (!p.validStart(board.index(x, y))) {
                            playerMap.get(p).sendError(ClientHandler.INVALID_MOVE,
                                    "Start Base can only get set in 9 middle fields");
                            return;
                        }
                        p.setStart(board.index(x, y), board);
                        for (Player player : playerMap.keySet()) {
                            playerMap.get(player).sendDoneMove(currentPlayer.getName(), x, y, size, color);
                        }
                        start = false;
                        nextPlayer();
                        for (Player player : playerMap.keySet()) {
                            playerMap.get(player).sendDoMove(currentPlayer.getName());
                        }
                        return;
                    }
                }
            }
        }
        if (!board.gameOver(currentPlayer)) {
            for (Player p : playerMap.keySet()) {
                if (p.getName().equals(name)) {
                    if (color == 0) {
                        List<Field> validFields = board.getValidFields(p, true);
                        if (validFields.contains(board.getField(x, y))) {
                            p.makeMove(board.index(x, y),
                                    p.getPrimaryPieces().get(size).get(0), board);
                            for (Player player : playerMap.keySet()) {
                                playerMap.get(player).sendDoneMove(currentPlayer.getName(),
                                        x, y, size, color);
                            }
                        } else {
                            playerMap.get(p).sendError(ClientHandler.INVALID_MOVE, "");
                            return;
                        }
                    } else if (color == 1) {
                        List<Field> validFields = board.getValidFields(p, false);
                        if (validFields.contains(board.getField(x, y))) {
                            p.makeMove(board.index(x, y),
                                    p.getSecondaryPieces().get(size).get(0), board);
                            for (Player player : playerMap.keySet()) {
                                playerMap.get(player).sendDoneMove(currentPlayer.getName(),
                                        x, y, size, color);
                            }
                        } else {
                            playerMap.get(p).sendError(ClientHandler.INVALID_MOVE, "");
                            return;
                        }

                    }
                }
            }
        } else {
            notGameOver.remove(currentPlayer);
        }
        nextPlayer();
        for (Player player : playerMap.keySet()) {
            playerMap.get(player).sendDoMove(currentPlayer.getName());
        }

    }

    public void run() {
        start = true;
        // Ask first Player for move
        currentPlayer = notGameOver.get(0);
        for (Player player : playerMap.keySet()) {
            playerMap.get(player).sendDoMove(currentPlayer.getName());
        }

    }

    private void nextPlayer() {
        for (int i = 0; i < notGameOver.size(); i++) {
            if (currentPlayer.getName().equals(notGameOver.get(i).getName())) {
                if (board.gameOver(notGameOver.get(i))) {
                    notGameOver.remove(i);
                    if (notGameOver.isEmpty()) {
                        List<ClientHandler> chList = new ArrayList<>(playerMap.values());
                        gameFinished(chList);
                        return;
                    }
                }
                currentPlayer = notGameOver.get((i + 1) % notGameOver.size());
                return;
            }
        }
    }

    private void gameFinished(List<ClientHandler> chList) {
        server.print("A game ended");
        StringBuilder result = new StringBuilder();
        for (Player player : playerMap.keySet()) {
            result.append("[").append(player.getName()).append(",").
                    append(board.getScore(player)).append(",").
                    append(player.ringCount()).append("] ");
        }
        for (Player player : playerMap.keySet()) {
            playerMap.get(player).sendResults(result.toString());
        }
        server.gameFinished(chList);
    }
}

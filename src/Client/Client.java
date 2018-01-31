package Client;

import Game.Model.Board;
import Game.Model.Piece;
import Interface.MessageUI;
import javafx.util.Pair;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Arrays.copyOfRange;

public class Client extends Thread {

    private String clientName;
    private MessageUI mui;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private int noOfPlayers;
    private ClientGame clientGame;
    private GameGUI gameGUI;
    private boolean first = true;

    public Client(String clientName, InetAddress host, int port, MessageUI mui, int noOfPlayers)
            throws IOException {
        this.clientName = clientName;
        this.mui = mui;
        this.noOfPlayers = noOfPlayers;
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {

        // Client main loop for receiving messages
        String textIn;
        try {
            while ((textIn = in.readLine()) != null) {
                // Check the first word of every incoming message to see if it is a command
                switch (textIn.split(" ")[0]) {
                    case HELLO:
                        receiveHello(textIn);
                        break;
                    case START:
                        receiveStart(textIn);
                        break;
                    case ERROR:
                        receiveError(textIn);
                        break;
                    case DO_MOVE:
                        receiveDoMove(textIn);
                        break;
                    case DONE_MOVE:
                        receiveDoneMove(textIn);
                        break;
                    case PLAYER_LEFT:
                        receivePlayerLeft(textIn);
                        break;
                    case RESULTS:
                        receiveResults(textIn);
                        break;
                    default:
                        mui.addMessage(textIn);
                        break;
                }
            }
        } catch (IOException e) {
            mui.addMessage("Client terminated");
        }
    }

    public void sendMessage(String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            System.err.println("Error: client " + clientName + " failed to send message");
            shutdown();
        }
    }

    public void shutdown() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error: failed to close client socket");
        }
        mui.addMessage("Connection lost...");
    }

    public String getClientName() {
        return clientName;
    }


    /*-PROTOCOL----------------------------------------------*/
    //                 Error codes
    public static final int GENERAL = 0;
    public static final int INVALID_MOVE = 1;
    public static final int NOT_YOUR_TURN = 2;
    public static final int NAME_IN_USE = 3;
    public static final int INVALID_COMMAND = 4;

    //              Extension names
    // Every extension has a seperate NAME constant that corresponds to their
    // extension name.
    public static final String EXTENSIONS = "";
    // chat challenge leaderboard security

    //              Command keywords
    // shared keywords (both client and server)
    public static final String HELLO = "hello";
    public static final String START = "start";
    // sent by server only
    public static final String ERROR = "error";
    public static final String DO_MOVE = "do_move";
    public static final String DONE_MOVE = "done_move";
    public static final String PLAYER_LEFT = "player_left";
    public static final String RESULTS = "results";
    // sent by client only
    public static final String MOVE = "move";


    //               Commands
    // sending Commands (outgoing)
    public void sendHello(String name, String extensions) {
        sendMessage(HELLO + " " + name + " " + extensions);
    }

    public void sendStart() {
        mui.addMessage("Looking for players to start a " + noOfPlayers + " player game");
        mui.addMessage("Waiting for players...");
        sendMessage(START + " " + noOfPlayers);
    }

    public void sendMove(int x, int y, int size, int colour) {
        sendMessage(MOVE + " " + x + " " + y + " " + size + " " + colour);
    }

    // receiving Commands (incoming)
    private void receiveHello(String helloCommand) {
        String[] hello = helloCommand.split(" ");
        if (hello.length >= 2) {
            mui.addMessage("Server has " + hello[1] + " players waiting for games");
            sendHello(clientName, EXTENSIONS);
        } else {
            mui.addMessage("server did not send hello correctly: " + helloCommand);
        }
    }

    private void receiveStart(String startCommand) {
        String[] start = startCommand.split(" ");
        if (start.length == 3) {
            mui.addMessage("Game started: " + start[1] + " " + start[2]);
        } else if (start.length == 4) {
            mui.addMessage("Game started: " + start[1] + " " + start[2] + " " + start[3]);
        } else if (start.length == 5) {
            mui.addMessage("Game started: " + start[1] + " "
                    + start[2] + " " + start[3] + " " + start[4]);
        } else {
            mui.addMessage("ERROR: invalid amount of players: " + startCommand);
        }
        String[] names = Arrays.copyOfRange(start, 1, start.length);
        clientGame = new ClientGame(names);
        gameGUI = new GameGUI(this, clientGame);
        clientGame.setObservers(gameGUI);
        new Thread(gameGUI).start();


    }

    private void receiveError(String errorCommand) {
        String[] error = errorCommand.split(" ", 3);
        switch (Integer.parseInt(error[1])) {
            case GENERAL:
                mui.addMessage("GENERAL ERROR: " + errorCommand);
                break;
            case INVALID_MOVE:
                System.out.println(errorCommand);
                break;
            case NOT_YOUR_TURN:
                System.out.println(errorCommand);
                break;
            case NAME_IN_USE:
                mui.addMessage("ERROR NAME_IN_USE: " + errorCommand);
                shutdown();
                break;
            case INVALID_COMMAND:
                System.out.println(errorCommand);
                break;
            default:
                mui.addMessage("UNKNOWN ERROR: " + errorCommand);
                break;
        }
    }

    private void receiveDoMove(String doMoveCommand) {
        String[] doMove = doMoveCommand.split(" ");

        if (doMove[1].equals(clientName)) {
            gameGUI.updateTurn(doMove[1], true);
            if (clientName.contains("BOT")) {
                Pair<Integer, Piece> botMove = clientGame.askBotMove(clientName, first);
                int x = botMove.getKey() % Board.DIM;
                int y = (botMove.getKey() - x) / Board.DIM;
                int size = botMove.getValue().getSize();
                int color = botMove.getValue().isPrimary() ? 0 : 1;
                sendMove(x, y, size, color);
            }
        } else {
            gameGUI.updateTurn(doMove[1], false);
        }
        first = false;
    }

    private void receiveDoneMove(String doneMoveCommand) {
        String[] doneMove = doneMoveCommand.split(" ");
        clientGame.doneMove(doneMove[1], Integer.parseInt(doneMove[2]),
                Integer.parseInt(doneMove[3]), Integer.parseInt(doneMove[4]),
                Integer.parseInt(doneMove[5]));
        gameGUI.updatePieces(clientName);
    }

    private void receivePlayerLeft(String playerLeftCommand) {
        String[] playerLeft = playerLeftCommand.split(" ");
        mui.addMessage("Player '" + playerLeft[1] + "' left");
    }

    private void receiveResults(String resultsCommand) {
        gameGUI.printResults(resultsCommand);
        mui.addMessage("Game ended.\nResults:\n" + resultsCommand);
    }
    /*-----------------------------------------------------*/
}
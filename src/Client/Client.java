package Client;

import Interface.MessageUI;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {

    private String clientName;
    private MessageUI mui;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private int noOfPlayers;

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
            //shutdown();
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

    public void sendStart(int numberOfPlayers) {
        mui.addMessage("Waiting for players...");
        sendMessage(START + " " + numberOfPlayers);
    }

    public void sendMove(int x, int y, int size, int colour) {
        sendMessage(MOVE + " " + x + " " + y + " " + size + " " + colour);
    }

    // receiving Commands (incoming)
    public void receiveHello(String helloCommand) {
        String[] hello = helloCommand.split(" ");
        if (hello.length >= 2) {
            if (hello.length > 2) {
                // handle extensions
            }
            mui.addMessage("Server has " + hello[1] + " players waiting for games");
            sendHello(clientName, EXTENSIONS);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendStart(noOfPlayers);
        } else {
            mui.addMessage("server did not send hello correctly: " + helloCommand);
        }
    }

    public void receiveStart(String startCommand) {
        String[] start = startCommand.split(" ");
        if (start.length == 3) {
            mui.addMessage("Game started: " + start[1] + " " + start[2]);
        } else if (start.length == 4) {
            mui.addMessage("Game started: " + start[1] + " " + start[2] + " " + start[3]);
        } else if (start.length == 5) {
            mui.addMessage("Game started: " + start[1] + " " + start[2] + " " + start[3] + " " + start[4]);
        } else {
            mui.addMessage("ERROR: invalid amount of players: " + startCommand);
        }
        // TODO: Start game loop
    }

    public void receiveError(String errorCommand) {
        String[] error = errorCommand.split(" ", 3);
        switch (Integer.parseInt(error[1])) {
            case GENERAL:
                mui.addMessage("GENERAL ERROR: " + errorCommand);
                break;
            case INVALID_MOVE:
                //
                break;
            case NOT_YOUR_TURN:
                //
                break;
            case NAME_IN_USE:
                mui.addMessage("ERROR NAME_IN_USE: " + errorCommand);
                shutdown();
                break;
            case INVALID_COMMAND:
                //
                break;
            default:
                mui.addMessage("UNKNOWN ERROR: " + errorCommand);
                break;
        }
    }

    public void receiveDoMove(String doMoveCommand) {
        // TODO
    }

    public void receiveDoneMove(String doneMoveCommand) {
        // TODO
    }

    public void receivePlayerLeft(String playerLeftCommand) {
        // TODO
    }

    public void receiveResults(String resultsCommand) {
        // TODO
    }
    /*-----------------------------------------------------*/

}

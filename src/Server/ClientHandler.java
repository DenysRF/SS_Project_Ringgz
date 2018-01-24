package Server;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ClientHandler extends Thread {

    private Server server;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientName = null;
    private boolean inGame;
    private Socket socket;

    public ClientHandler(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }


    @Override
    public void run() {

        sendHello(server.getNotInGamePlayerCount(), EXTENSIONS);

        String message;
        try {
            while ((message = in.readLine()) != null) {
                switch (message.split(" ")[0]) {
                    case HELLO:
                        receiveHello(message);
                        break;
                    case START:
                        receiveStart(message);
                        break;
                    case MOVE:
                        receiveMove(message);
                        break;
                    default:
                        server.print(clientName + ": " + message);
                        break;
                }

            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }

    public void sendMessage(String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            shutdown();
            System.err.println("ClientHandler for " + clientName + " failed send message");
        }
    }

    private void shutdown() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeHandler(this);
        server.print("[" + clientName + " has left]");
    }

    public String getClientName() {
        return clientName;
    }

    public boolean inGame() {
        return inGame;
    }

    /*-PROTOCOL----------------------------------------------*/

    public static final String EXTENSIONS = "";

    //                 Error codes
    public static final int GENERAL = 0;
    public static final int INVALID_MOVE = 1;
    public static final int NOT_YOUR_TURN = 2;
    public static final int NAME_IN_USE = 3;
    public static final int INVALID_COMMAND = 4;

    //              Extension names
    // Every extension has a seperate NAME constant that corresponds to their
    // extension name.

    //              Command keywords
    //				Server keywords
    public static final String ERROR = "error";
    public static final String DO_MOVE = "do_move";
    public static final String VALID = "valid";
    public static final String DONE_MOVE = "done_move";
    public static final String PLAYER_LEFT = "player_left";
    public static final String RESULTS = "results";
    //				Client keywords
    public static final String MOVE = "move";
    //				shared keywords (both client and server)
    public static final String HELLO = "hello";
    public static final String START = "start";

    //              Commands
    // sending Commands (outgoing)
    public void sendHello(int numberOfPlayers, String extensions) {
        sendMessage(HELLO + " " + numberOfPlayers + " " + extensions);
    }

    // Server puts a space in front of names
    public void sendStart(String names) {
        sendMessage(START + names);
    }
    public void sendError(int errorcode, String optionalMessage) {
        sendMessage(ERROR + " " + errorcode + " " + optionalMessage);
    }
    public void sendDoMove(String name) {
        sendMessage(DO_MOVE + " " + name);
    }
    public void sendDoneMove(String name, int x, int y, int size, int colour) {
        sendMessage(DONE_MOVE + " " + name + " " + x + " " + y + " " + size + " " + colour);
    }
    public void sendPlayerLeft(String name) {
        sendMessage(PLAYER_LEFT + " " + name);
    }
    public void sendResults(Map<String, Integer> playerPointsMap,
                            Map<String, Integer> playerRingsMap,
                            Map<String, Boolean> playerIsWinnerMap) {

    }
    // receiving Commands (incoming)
    public void receiveHello(String helloCommand) {
        String[] hello = helloCommand.split(" ");
        if (hello.length >= 2) {
            if (server.nameInUse(hello[1])) {
                System.out.println("name in use");
                sendError(NAME_IN_USE, "");
                shutdown();
            } else {
                clientName = hello[1];
                server.print("[" + clientName + " has entered]");
                if (hello.length > 2) {
                    // handle extensions
                }
            }
        } else {
            server.print(this + " did not receive hello from client well");
        }
    }
    public void receiveStart(String startCommand) {
        String[] start = startCommand.split(" ");
        if (start.length == 2) {
            inGame = true;
            server.print(start[0]);
            server.print(clientName + " wants to start a game with " + start[1] + " players");
            server.addPlayerToGame(this, Integer.parseInt(start[1]));
        } else {
            server.print(clientName + "did not receive start well");
        }

    }
    public void receiveMove(String moveCommand) {

    }

    /*-----------------------------------------------------*/
}
package Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Server server;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientName = null;
    private boolean inGame;
    private ServerGame serverGame;

    public ClientHandler(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }


    // Send server hello to client and receive incoming commands
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
                        sendError(INVALID_COMMAND, "");
                        server.print("INVALID COMMAND from " + clientName + ": " + message);
                        break;
                }
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }

    // Send commands to client according to Protocol
    public void sendMessage(String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            shutdown();
            System.err.println("ClientHandler for " + clientName + " failed send message");
        }
    }

    // If Client disconnects this gets triggered and cleans client from server
    private void shutdown() {
        server.removeHandler(this);
        if (clientName != null) {
            server.print("[" + clientName + " has left]");
        }
    }

    public String getClientName() {
        return clientName;
    }

    public boolean inGame() {
        return inGame;
    }

    // Set the in-game status of this clientHandler
    public void setInGame(boolean b) {
        inGame = b;
    }

    // Check if client name if valid
    private boolean isValidName(String helloCommand) {
        String[] hello = helloCommand.split(" ");
        // if the command is longer than 2, check if extra parameters correspond to extensions
        // if they don't this means there are spaces in the name, or elsewhere
        if (hello.length > 2) {
            for (int i = 2; i < hello.length; i++) {
                if (!(hello[i].equals("chat") || hello[i].equals("challenge") ||
                        hello[i].equals("leaderboard") || hello[i].equals("security"))) {
                    return false;
                }
            }
        }
        // checks the name for comma's
        if (hello[1].contains(",")) {
            return false;
        }
        return true;
    }

    public void setServerGame(ServerGame serverGame) {
        this.serverGame = serverGame;
    }

    /*-PROTOCOL----------------------------------------------*/

    public static final String EXTENSIONS = "";
    // chat challenge leaderboard security

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
        sendMessage(START + " " + names);
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
    public void sendResults(String results) {
        sendMessage(RESULTS + " " + results);

    }
    // receiving Commands (incoming)
    public void receiveHello(String helloCommand) {
        String[] hello = helloCommand.split(" ");
        if (hello.length >= 2) {
            // Check if name is valid or already in use
            if (!isValidName(helloCommand)) {
                server.print("Refused new client with invalid name.");
                sendError(GENERAL, "Your name in message: '" + helloCommand + "' is invalid.");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (server.nameInUse(hello[1])) {
                server.print("Refused new client.\n\tName: '" + hello[1] + "' is already in use.");
                sendError(NAME_IN_USE, "");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                clientName = hello[1];
                server.print("[" + clientName + " has entered]");
                if (hello.length > 2) {
                    // no extensions
                }
            }
        } else {
            sendError(GENERAL, "Your sendHello() message has < 2 arguments");
        }
    }
    public void receiveStart(String startCommand) {
        String[] start = startCommand.split(" ");
        if (start.length == 2) {
            server.print(clientName + " wants to start a game with " + start[1] + " players");
            server.addPlayerToGame(this, Integer.parseInt(start[1]));
        } else {
            server.print(clientName + "did not receive start well");
        }
    }

    public void receiveMove(String moveCommand) {
        String[] move = moveCommand.split(" ");
        if (move.length == 5) {
            if (inGame) {
                serverGame.doneMove(clientName, Integer.parseInt(move[1]), Integer.parseInt(move[2]), Integer.parseInt(move[3]), Integer.parseInt(move[4]));

            } else {
                sendError(GENERAL, "You are not in game");
            }
        } else {
            sendError(INVALID_COMMAND, "Number of arguments did not match expected number of arguments");
        }
    }

    /*-----------------------------------------------------*/
}
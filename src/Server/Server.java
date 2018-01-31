package Server;

import Interface.MessageUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends Thread {

    private int port;
    private MessageUI mui;
    private List<ClientHandler> threads;
    private ServerSocket ss;

    private final Map<Integer, List<List<ClientHandler>>> games;

    public Server(int port, MessageUI mui) {
        this.port = port;
        this.mui = mui;
        threads = new ArrayList<>();
        games = new HashMap<>();
        // Mapping for 2-, 3- and 4-player games
        for (int i = 2; i <= 4; i++) {
            games.put(i, new ArrayList<>());
        }
    }

    // Loop for accepting Clients
    @Override
    public void run() {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Error: could not create ServerSocket on port " + port);
        }
        /*
         * Running server loop that accept incoming clients and creates
         * a ClientHandler thread for them.
         */
        while (true) {
            try {
                Socket socket = ss.accept();
                ClientHandler ch = new ClientHandler(this, socket);
                addHandler(ch);
                ch.start();
            } catch (IOException e) {
                System.err.println("Error: failed to accept new client");
            }
        }
    }

    // Method so ClientHandler can print to server
    public void print(String msg) {
        mui.addMessage(msg);
    }

    // Add ClientHandler to server
    private void addHandler(ClientHandler handler) {
        threads.add(handler);
    }

    // Remove ClientHandler from server
    public void removeHandler(ClientHandler handler) {
        mui.addMessage("[" + handler.getClientName() + " disconnected]");
        threads.remove(handler);
        synchronized (games) {
            for (Integer n : games.keySet()) {
                for (List<ClientHandler> chList : games.get(n)) {
                    if (chList.contains(handler)) {
                        chList.remove(handler);
                    }
                }
            }
        }
    }

    // Send message to all Clients
//    public void broadcast(String msg) {
//        mui.addMessage("BROADCAST: " + msg);
//        for (ClientHandler ch : threads) {
//            ch.sendMessage(msg);
//        }
//    }

    // Count all players that are connected to server but not in game
    public int getNotInGamePlayerCount() {
        // Do count yourself
        int i = 0;
        for (ClientHandler ch : threads) {
            if (!ch.inGame()) {
                i++;
            }
        }
        return i;
    }

    // Send message to specific client via ClientHandler
    public void messageClient(String msg, String name) {
        for (ClientHandler ch : threads) {
            if (ch.getClientName().equals(name)) {
                ch.sendMessage(msg);
            }
        }
    }

    // Start a game when enough players are ready
    private void startGame(List<ClientHandler> chList) {
        mui.addMessage("Game started with: ");
        String names = "";
        ServerGame serverGame = new ServerGame(chList, this);
        for (ClientHandler ch : chList) {
            names = names + " " + ch.getClientName();
            ch.setInGame(true);
            ch.setServerGame(serverGame);
        }
        for (ClientHandler ch : chList) {
            mui.addMessage("\t" + ch.getClientName());
            ch.sendStart(names);
        }
        serverGame.playGame();


    }

    public void gameFinished(List<ClientHandler> chList) {
        mui.addMessage("A game just finished");
        for (ClientHandler ch : chList) {
            ch.setInGame(false);
            synchronized (games) {
                for (Integer n : games.keySet()) {
                    for (List<ClientHandler> l : games.get(n)) {
                        if (l.contains(ch)) {
                            l.remove(ch);
                        }
                    }
                }
            }
        }
    }

    // Add player to games and wait for enough players to start
    public void addPlayerToGame(ClientHandler ch, int noOfPlayers) {
        if (games.get(noOfPlayers).size() == 0) {
            games.get(noOfPlayers).add(new ArrayList<>());
        }
        for (List<ClientHandler> l : games.get(noOfPlayers)) {
            if (l.size() < noOfPlayers) {
                l.add(ch);
                if (l.size() == noOfPlayers) {
                    startGame(l);
                }
                break;
            }
        }
    }

    // Check if another Client already uses this name
    public boolean nameInUse(String name) {
        for (ClientHandler ch : threads) {
            if (ch.getClientName() != null) {
                if (ch.getClientName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
}

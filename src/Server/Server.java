package Server;

import Client.Client;
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

    private Map<Integer, List<List<ClientHandler>>> games;

    public Server(int port, MessageUI mui) {
        this.port = port;
        this.mui = mui;
        threads = new ArrayList<>();
        games = new HashMap<>();
        for (int i = 2; i <= 4; i++) {
            games.put(i, new ArrayList<>());
        }
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Error: could not create ServerSocket on port " + port);
        }
        int i = 1;
        /*
        * Running server loop that accept incoming clients and creates
        * a ClientHandler thread for them.
        */
        while (true) {
            try {
                Socket socket = ss.accept();
                mui.addMessage("[client no. " + i + " connected]");
                i++;
                ClientHandler ch = new ClientHandler(this, socket);
                threads.add(ch);
                ch.start();
            } catch(IOException e) {
                System.err.println("Error: failed to accept new client");
            }
        }
    }

    public void print(String message) {
        mui.addMessage(message);
    }

    public void addHandler(ClientHandler handler) {
        threads.add(handler);
    }

    public void removeHandler(ClientHandler handler) {
        threads.remove(handler);
        for (Integer n : games.keySet()) {
            for (List<ClientHandler> chList : games.get(n)) {
                for (int i = 0; i < chList.size(); i++) {
                    if (chList.get(i) == handler) {
                        System.out.println("removed");
                        chList.remove(i);
                    }
                }
            }
        }
    }

    public void broadcast(String msg) {
        mui.addMessage(msg);
        for (ClientHandler ch : threads) {
            ch.sendMessage(msg);
        }
    }

    public int getNotInGamePlayerCount() {
        int i = 0;
        for (ClientHandler ch : threads) {
            if (!ch.inGame()) {
                i++;
            }
        }
        return i;
    }

    public void messageClient(String msg, String name) {
        for (ClientHandler ch : threads) {
            if (ch.getClientName() == name) {
                ch.sendMessage(msg);
            }
        }
    }

    private void startGame(List<ClientHandler> chList) {
        print("Game started with: ");
        String names = "";
        for (ClientHandler ch : chList) {
            names = names + " " + ch.getClientName();
        }
        for (ClientHandler ch : chList) {
            print("\t" + ch.getClientName());
            ch.sendStart(names);
        }
    }

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
            }
        }
    }

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

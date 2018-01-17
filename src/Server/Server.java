package Server;

import Interface.MessageUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

    private int port;
    private MessageUI mui;
    private List<ClientHandler> threads;
    private ServerSocket ss;

    public Server(int port, MessageUI mui) {
        this.port = port;
        this.mui = mui;
        threads = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Error: could not create ServerSOcket on port " + port);
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
    }

}

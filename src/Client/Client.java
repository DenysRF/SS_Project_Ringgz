package Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {

    private String clientName;
    private ClientMessageUI mui;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public Client(String clientName, InetAddress host, int port, ClientMessageUI mui)
            throws IOException {
        this.clientName = clientName;
        this.mui = mui;
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        String textIn;
        try {
            while ((textIn = in.readLine()) != null) {
                mui.addMessage(textIn);
            }
            System.out.println("readLine() loop in client.run ended: shutdown");
            shutdown();
        } catch (IOException e) {
            System.err.println("Error: IOException in client run()-loop");
            shutdown();
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
        mui.addMessage("Closing socket connection...");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error: failed to close client socket");
        }
    }

    public String getClientName() {
        return clientName;
    }


}

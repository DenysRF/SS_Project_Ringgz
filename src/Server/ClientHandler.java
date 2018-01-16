package Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Server server;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientName;

    public ClientHandler(Server server, Socket socket) throws IOException {
        this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    // Protocol method
    public void announce() throws IOException {
        clientName = in.readLine();
        server.print("[" + clientName + " has entered]");
    }

    @Override
    public void run() {
        String message;
        try {
            // protocol Client sends his name first
            announce();
            while ((message = in.readLine()) != null) {
                server.print(clientName + ": " + message);
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
        server.removeHandler(this);
        server.print("[" + clientName + " has left]");
    }

}

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




    public Client(String clientName, InetAddress host, int port, MessageUI mui)
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

	//              Command keywords
	// shared keywords (both client and server)
	public static final String HELLO = "hello";
	public static final String START = "start";
	// sent by server only
	public static final String ERROR = "error";
	public static final String DO_MOVE = "do_move";
	public static final String VALID = "valid";
	public static final String DONE_MOVE = "done_move";
	public static final String PLAYER_LEFT = "player_left";
	public static final String RESULTS = "results";
	// sent by client only
	public static final String MOVE = "move";


	//               Commands
	// sending Commands (outgoing)
	public void sendHello(String name, String extensions) {
       // sendMessage();
    }
	public void sendStart(int numberOfPlayers) {

    }
	public void sendMove(int x, int y, int size, int colour) {

    }

	// receiving Commands (incoming)
	public void receiveHello(String helloCommand) {

    }
	public void receiveStart(String startCommand) {

    }
	public void receiveError(String errorCommand) {

    }
	public void receiveDoMove(String doMoveCommand) {

    }
	public void receiveDoneMove(String doneMoveCommand) {

    }
	public void receivePlayerLeft(String playerLeftCommand) {

    }
	public void receiveResults(String resultsCommand) {

    }
    /*-----------------------------------------------------*/

}

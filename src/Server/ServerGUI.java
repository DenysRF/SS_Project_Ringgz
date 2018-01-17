package Server;

import Interface.MessageUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerGUI extends JFrame implements ActionListener, MessageUI {

    private JButton bListen;
    private JTextField tfPort;
    private JTextArea taMessages;
    private Server server;

    public ServerGUI() {
        super("Ringgz Server");

        buildGUI();
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
            }
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void buildGUI() {
        setSize(600, 400);

        // Panel p1 - Listen
        JPanel p1 = new JPanel(new FlowLayout());
        JPanel pFields = new JPanel(new GridLayout(2, 2));

        JLabel lbAddress = new JLabel("Address:");
        JTextField tfAddress = new JTextField(getHostAddress());
        tfAddress.setEditable(false);

        JLabel lbPort = new JLabel("Port:");
        tfPort = new JTextField("2000");

        pFields.add(lbAddress);
        pFields.add(tfAddress);
        pFields.add(lbPort);
        pFields.add(tfPort);

        bListen = new JButton("Start listening");
        bListen.addActionListener(this);

        p1.add(pFields, BorderLayout.WEST);
        p1.add(bListen, BorderLayout.EAST);

        // Panel p2 - Messages
        JPanel p2 = new JPanel(new BorderLayout());

        JLabel lbMessages = new JLabel("Messages:");
        taMessages = new JTextArea("", 15, 50);
        taMessages.setEditable(false);

        p2.add(lbMessages);
        p2.add(taMessages, BorderLayout.SOUTH);

        Container cc = getContentPane();
        cc.setLayout(new FlowLayout());
        cc.add(p1);
        cc.add(p2);

    }

    private String getHostAddress() {
        try {
            InetAddress iaddr = InetAddress.getLocalHost();
            return iaddr.getHostAddress();
        } catch (UnknownHostException e) {
            return "?unknown?";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == bListen) {
            startListening();
        }
    }

    private void startListening() {
        int port;
        try {
            port = Integer.parseInt(tfPort.getText());
        } catch (NumberFormatException e) {
            addMessage("Error: not a valid port number");
            return;
        }

        tfPort.setEditable(false);
        bListen.setEnabled(false);

        server = new Server(port, this);
        server.start();

        addMessage("Started listening on port " + port + "...");
    }

    @Override
    public void addMessage(String msg) {
        taMessages.append(msg + "\n");
    }

    public static void main(String[] args) {
        new ServerGUI();
    }


}

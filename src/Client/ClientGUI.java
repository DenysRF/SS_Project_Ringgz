package Client;

import Interface.MessageUI;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientGUI extends JFrame implements ActionListener, MessageUI {

    private JButton bConnect, bDisconnect;
    private JTextField tfPort, tfName, tfHost;
    private JTextArea taMessages;
    private JRadioButton rbTwoPlayers, rbThreePlayers, rbFourPlayers;
    private JComboBox<String[]> pColorList, sColorList;

    private String[] colors = {"BLUE", "RED", "GREEN", "PURPLE"};

    private Client client;

    public ClientGUI() {
        super("Ringgz Client");

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

    private void buildGUI() {
        setSize(460, 520);

        // Panel p1 - Connect
        JPanel p1 = new JPanel(new FlowLayout());
        JPanel pFields = new JPanel(new GridLayout(3, 2));

        JLabel lbHost = new JLabel("Hostname:");
        tfHost = new JTextField("localhost", 12);

        JLabel lbPort = new JLabel("Port:");
        tfPort = new JTextField("2000", 12);

        JLabel lbName = new JLabel("Name:");
        tfName = new JTextField("Anonymous", 12);

        pFields.add(lbHost);
        pFields.add(tfHost);
        pFields.add(lbPort);
        pFields.add(tfPort);
        pFields.add(lbName);
        pFields.add(tfName);

        bConnect = new JButton("Connect");
        bConnect.addActionListener(this);

        p1.add(pFields);
        p1.add(bConnect);

        // Panel p2 - Options
        JPanel p2 = new JPanel(new FlowLayout());

        JLabel lbCount = new JLabel("Number of players:");
        JPanel pCount = new JPanel(new FlowLayout());

        rbTwoPlayers = new JRadioButton("2");
        rbTwoPlayers.addActionListener(this);
        rbTwoPlayers.setActionCommand("2");


        rbThreePlayers = new JRadioButton("3");
        rbThreePlayers.addActionListener(this);
        rbThreePlayers.setActionCommand("3");

        rbFourPlayers = new JRadioButton("4");
        rbFourPlayers.addActionListener(this);
        rbFourPlayers.setActionCommand("4");

        ButtonGroup group = new ButtonGroup();
        group.add(rbTwoPlayers);
        group.add(rbThreePlayers);
        group.add(rbFourPlayers);
        group.setSelected(rbTwoPlayers.getModel(), true);

        pCount.add(lbCount);
        pCount.add(rbTwoPlayers);
        pCount.add(rbThreePlayers);
        pCount.add(rbFourPlayers);


        JLabel lbPColors = new JLabel("Choose your primary color");
        JLabel lbSColors = new JLabel("Choose your secondary color");
        JPanel pColors = new JPanel(new BorderLayout());
        JPanel pPColor = new JPanel(new BorderLayout());
        JPanel pSColor = new JPanel(new BorderLayout());

        pColorList = new JComboBox(colors);
        pColorList.setSelectedIndex(0);
        pColorList.addActionListener(this);
        pPColor.add(lbPColors, BorderLayout.NORTH);
        pPColor.add(pColorList);

        sColorList = new JComboBox(colors);
        sColorList.setSelectedIndex(1);
        sColorList.addActionListener(this);
        pSColor.add(lbSColors, BorderLayout.NORTH);
        pSColor.add(sColorList);

        pColors.add(pPColor, BorderLayout.NORTH);
        pColors.add(pSColor, BorderLayout.SOUTH);

        p2.add(pCount);
        p2.add(pColors);

        // Panel p3 - Messages
        JPanel p3 = new JPanel(new BorderLayout());

        JLabel lbMessages = new JLabel("Messages:");
        taMessages = new JTextArea("", 15, 30);
        taMessages.setEditable(false);
        DefaultCaret caret = (DefaultCaret)taMessages.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane spScroll = new JScrollPane(taMessages);

        p3.add(lbMessages);
        p3.add(spScroll, BorderLayout.SOUTH);


        // Panel p4 - Disconnect
        JPanel p4 = new JPanel(new FlowLayout());

        bDisconnect = new JButton("Disconnect");
        bDisconnect.setEnabled(false);
        bDisconnect.addActionListener(this);

        p4.add(bDisconnect);

        // Add to container
        Container cc = getContentPane();
        cc.setLayout(new FlowLayout());
        cc.add(p1);
        cc.add(p2);
        cc.add(p3);
        cc.add(p4);

    }

    @Override
    public void addMessage(String msg) {
        taMessages.append(msg + "\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == bConnect) {
            connect();
        } else if (src == bDisconnect) {
            client.shutdown();
            tfHost.setEditable(true);
            tfName.setEditable(true);
            tfPort.setEditable(true);
            bConnect.setEnabled(true);
            rbTwoPlayers.setEnabled(true);
            rbThreePlayers.setEnabled(true);
            rbFourPlayers.setEnabled(true);
            pColorList.setEnabled(true);
            sColorList.setEnabled(true);
            bDisconnect.setEnabled(false);
        } else if (src == rbTwoPlayers || src == rbThreePlayers) {
             sColorList.setEnabled(true);
        } else if (src == rbFourPlayers) {
            sColorList.setEnabled(false);
        } else if (src == pColorList) {
            if (pColorList.getSelectedIndex() == sColorList.getSelectedIndex()) {
                sColorList.setSelectedIndex((sColorList.getSelectedIndex() + 1) % (colors.length));
            }
        } else if (src == sColorList) {
            if (sColorList.getSelectedIndex() == pColorList.getSelectedIndex()) {
                pColorList.setSelectedIndex((pColorList.getSelectedIndex() + 1) % (colors.length));
            }
        }
    }

    private void connect() {
        int noOfPlayers;
        String pColor = (String) pColorList.getSelectedItem();
        String sColor = "_";

        if (rbTwoPlayers.isSelected()) {
            noOfPlayers = 2;
            sColor = (String) sColorList.getSelectedItem();
        } else if (rbThreePlayers.isSelected()) {
            noOfPlayers = 3;
            sColor = (String) sColorList.getSelectedItem();
        } else if (rbFourPlayers.isSelected()) {
            noOfPlayers = 4;
        } else {
            return;
        }

        int port;
        String name = tfName.getText();
        InetAddress host;

        try {
            port = Integer.parseInt(tfPort.getText());
        } catch (NumberFormatException e) {
            addMessage("ERROR: not a valid portnumber!");
            return;
        }

        try {
            host = InetAddress.getByName(tfHost.getText());
        } catch (UnknownHostException e) {
            addMessage("ERROR: not a valid host!");
            return;
        }

        try {
            client = new Client(name, host, port, this, noOfPlayers);
        } catch (IOException e) {
            addMessage("ERROR: could not connect");
            return;
        }

        tfHost.setEditable(false);
        tfName.setEditable(false);
        tfPort.setEditable(false);
        bConnect.setEnabled(false);
        rbTwoPlayers.setEnabled(false);
        rbThreePlayers.setEnabled(false);
        rbFourPlayers.setEnabled(false);
        pColorList.setEnabled(false);
        sColorList.setEnabled(false);
        bDisconnect.setEnabled(true);

        addMessage("Connected to server...");
        if (noOfPlayers == 4) {
            //addMessage("Your color: " + pColor);
        } else {
            //addMessage("Your colors: " + pColor + ", " + sColor);
        }
        addMessage("Looking for players to start a " + noOfPlayers + " player game");

        client.start();
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
}

package Client;

import Interface.MessageUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientGUI extends JFrame implements ActionListener, MessageUI {

    private JButton bConnect;
    private JTextField tfPort;
    private JTextField tfName;
    private JTextField tfHost;
    private JTextArea taMessages;
    private JRadioButton rbTwoPlayers;
    private JRadioButton rbThreePlayers;
    private JRadioButton rbFourPlayers;

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

    public void buildGUI() {
        setSize(600, 600);

        // Panel p1 - Connect
        JPanel p1 = new JPanel(new FlowLayout());
        JPanel pFields = new JPanel(new GridLayout(3, 2));

        JLabel lbHost = new JLabel("Hostname:");
        tfHost = new JTextField("", 12);

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

        pCount.add(lbCount);
        pCount.add(rbTwoPlayers);
        pCount.add(rbThreePlayers);
        pCount.add(rbFourPlayers);



//        JLabel lbColors = new JLabel("Choose your colors");
//        JPanel pColors = new JPanel(new FlowLayout());
//        pColors.add(lbColors, BorderLayout.NORTH);

        p2.add(pCount);
       // p2.add(pColors);

        // Add to container
        Container cc = getContentPane();
        cc.setLayout(new GridLayout(3, 1));
        cc.add(p1);
        cc.add(p2);

    }

    @Override
    public void addMessage(String msg) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        new Client.ClientGUI();
    }
}

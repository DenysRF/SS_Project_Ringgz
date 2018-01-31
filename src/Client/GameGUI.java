package Client;

import Game.Model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class GameGUI extends JFrame implements ActionListener, Observer, Runnable {

    private JButton[] fields = new JButton[Board.DIM * Board.DIM];

    private Client client;
    private JTextArea taPieces;
    private JPanel p3;
    private JRadioButton rbPrimary, rbSecondary, rbBase, rbSmall, rbMedium, rbBig, rbHuge, rbStart;
    private ClientGame clientGame;
    private JLabel lbMessage;


    public GameGUI(Client client, ClientGame clientGame)  {
        super(client.getClientName());
        this.client = client;
        this.clientGame = clientGame;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
            }
        });
    }

    private void buildGUI() {
        //setSize(600, 700);

        // Panel p1 - Fields
        JPanel p1 = new JPanel(new GridBagLayout());

        //ImageIcon icon = new ImageIcon("src\\Client\\images\\empty.png");
        GridBagConstraints c = new GridBagConstraints();


        for (int i = 0; i < Board.DIM * Board.DIM; i++) {
            int x = i % Board.DIM;
            int y = 0;
            for (int setY = 0; setY < Board.DIM; setY++) {
                if (i < ((setY + 1) * Board.DIM)) {
                    y = setY;
                    break;
                }
            }

            fields[i] = new JButton("(_._._._._)");

            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0 / Board.DIM;
            c.weighty = 1.0 / Board.DIM;
            c.gridx = x;
            c.gridy = y;

            fields[i].setMargin(new Insets(0,0,0,0));
            fields[i].setContentAreaFilled(false);
            fields[i].setFocusable(false);
            fields[i].setOpaque(true);
            fields[i].setBackground(Color.WHITE);

            fields[i].addActionListener(new MyButtonListener(x, y));

            p1.add(fields[i], c);
        }

        // p2 - Display Piece collection

        JPanel p2 = new JPanel(new GridLayout(1, 1));

        taPieces = new JTextArea("Display pieces after Start Base has been set",15, 10);
        taPieces.setEditable(false);

        p2.add(taPieces);


        // p3 - Message

        p3 = new JPanel(new GridLayout(1, 1));
        p3.setBackground(Color.GRAY);

        lbMessage = new JLabel("Game not yet started", JLabel.CENTER);

        p3.add(lbMessage);


        // p4 - Color and Piece radio buttons

        JPanel p4 = new JPanel(new BorderLayout());

        JLabel lbColors = new JLabel("Select color:");
        JPanel pColors = new JPanel(new FlowLayout());

        rbPrimary = new JRadioButton("Primary");
        rbPrimary.addActionListener(this);
        rbPrimary.setActionCommand("0");

        rbSecondary = new JRadioButton("Secondary");
        rbPrimary.addActionListener(this);
        rbPrimary.setActionCommand("1");

        ButtonGroup bgColorGroup = new ButtonGroup();
        bgColorGroup.add(rbPrimary);
        bgColorGroup.add(rbSecondary);
        bgColorGroup.setSelected(rbPrimary.getModel(), true);

        pColors.add(lbColors);
        pColors.add(rbPrimary);
        pColors.add(rbSecondary);

        JLabel lbPieces = new JLabel("Select Piece:");
        JPanel pPieces = new JPanel(new FlowLayout());

        rbBase = new JRadioButton("Base");
        rbSmall = new JRadioButton("Small");
        rbMedium = new JRadioButton("Medium");
        rbBig = new JRadioButton("Big");
        rbHuge = new JRadioButton("Huge");
        rbStart = new JRadioButton("Start");

        rbBase.addActionListener(this);
        rbSmall.addActionListener(this);
        rbMedium.addActionListener(this);
        rbBig.addActionListener(this);
        rbHuge.addActionListener(this);
        rbStart.addActionListener(this);

        rbBase.setActionCommand("0");
        rbSmall.setActionCommand("1");
        rbMedium.setActionCommand("2");
        rbBig.setActionCommand("3");
        rbHuge.setActionCommand("4");
        rbStart.setActionCommand("5");

        ButtonGroup bgPieceGroup = new ButtonGroup();
        bgPieceGroup.add(rbBase);
        bgPieceGroup.add(rbSmall);
        bgPieceGroup.add(rbMedium);
        bgPieceGroup.add(rbBig);
        bgPieceGroup.add(rbHuge);
        bgPieceGroup.add(rbStart);
        bgPieceGroup.setSelected(rbBase.getModel(), true);

        pPieces.add(lbPieces);
        pPieces.add(rbBase);
        pPieces.add(rbSmall);
        pPieces.add(rbMedium);
        pPieces.add(rbBig);
        pPieces.add(rbHuge);
        pPieces.add(rbStart);

        p4.add(pColors, BorderLayout.NORTH);
        p4.add(pPieces);

        // Add to container
        JPanel cp = new JPanel(new BorderLayout());
        cp.add(p2, BorderLayout.NORTH);
        cp.add(p3, BorderLayout.CENTER);
        cp.add(p4, BorderLayout.SOUTH);

        Container cc = getContentPane();
        cc.setLayout(new BorderLayout());
        cc.add(p1, BorderLayout.WEST);
        cc.add(cp);
    }

    @Override
    public void run() {
        buildGUI();
        pack();
        setVisible(true);
    }

    private class MyButtonListener implements ActionListener {
        private int x, y;

        private MyButtonListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int size;
            int color;
            if (rbBase.isSelected()) {
                size = 0;
            } else if (rbSmall.isSelected()) {
                size = 1;
            } else if (rbMedium.isSelected()) {
                size = 2;
            } else if (rbBig.isSelected()) {
                size = 3;
            } else if (rbHuge.isSelected()){
                size = 4;
            } else {
                size = 5;
            }
            if (rbPrimary.isSelected()) {
                color = 0;
            } else {
                color = 1;
            }
            client.sendMove(x, y, size, color);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Object src = e.getSource();
    }

    @Override
    public void update(Observable o, Object arg) {
        for (int i = 0; i < fields.length; i++) {
            fields[i].setText(clientGame.updateField(i));
        }
    }

    public void updateTurn(String name, boolean b) {
        if (p3 == null) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (b) {
            p3.setBackground(Color.GREEN);
        } else {
            p3.setBackground(Color.RED);
        }
        lbMessage.setText("It's "+ name + "'s turn");
    }

    public void updatePieces(String name) {
        taPieces.setText(clientGame.getPieces(name));
    }

    public void printResults(String resultsCommand) {
        String[] results = resultsCommand.split(" ");

        StringBuilder s = new StringBuilder("Results:");
        for (int i = 1; i < results.length; i++) {
            s.append("\n\t").append(results[i]);
        }
        taPieces.setText(clientGame.getPieces(client.getClientName()) + "\n" + s.toString());
        lbMessage.setText("Game over");
    }
}

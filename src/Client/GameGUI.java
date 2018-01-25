package Client;

import Game.Model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

public class GameGUI extends JFrame implements ActionListener, Observer {

    private JButton[] fields = new JButton[Board.DIM * Board.DIM];

    private Client client;


    public GameGUI(Client client)  {
        super("Ringgz");
        this.client = client;

        buildGUI();
        pack();
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
        //setSize(600, 700);

        // Panel p1 - Fields
        JPanel p1 = new JPanel(new GridBagLayout());

        ImageIcon icon = new ImageIcon("src\\Client\\images\\empty.png");
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

            fields[i] = new JButton(icon);

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

        // p3 - Color and Piece radio buttons

        JPanel p3 = new JPanel(new BorderLayout());

        JLabel lbColors = new JLabel("Select color:");
        JPanel pColors = new JPanel(new FlowLayout());

        JRadioButton rbPrimary = new JRadioButton("Primary");
        rbPrimary.addActionListener(this);
        rbPrimary.setActionCommand("0");

        JRadioButton rbSecondary = new JRadioButton("Secondary");
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

        JRadioButton rbBase = new JRadioButton("Base");
        JRadioButton rbSmall = new JRadioButton("Small");
        JRadioButton rbMedium = new JRadioButton("Mediun");
        JRadioButton rbBig = new JRadioButton("Big");
        JRadioButton rbHuge = new JRadioButton("Huge");

        rbBase.addActionListener(this);
        rbSmall.addActionListener(this);
        rbMedium.addActionListener(this);
        rbBig.addActionListener(this);
        rbHuge.addActionListener(this);

        rbBase.setActionCommand("0");
        rbSmall.setActionCommand("1");
        rbMedium.setActionCommand("2");
        rbBig.setActionCommand("3");
        rbHuge.setActionCommand("4");

        ButtonGroup bgPieceGroup = new ButtonGroup();
        bgPieceGroup.add(rbBase);
        bgPieceGroup.add(rbSmall);
        bgPieceGroup.add(rbMedium);
        bgPieceGroup.add(rbBig);
        bgPieceGroup.add(rbHuge);
        bgPieceGroup.setSelected(rbBase.getModel(), true);

        pPieces.add(lbPieces);
        pPieces.add(rbBase);
        pPieces.add(rbSmall);
        pPieces.add(rbMedium);
        pPieces.add(rbBig);
        pPieces.add(rbHuge);

        p3.add(pColors, BorderLayout.NORTH);
        p3.add(pPieces);

        // Add to container
        Container cc = getContentPane();
        cc.setLayout(new FlowLayout());
        cc.add(p1);
        //cc.add(p2);
        cc.add(p3);
    }

    private class MyButtonListener implements ActionListener {
        private int x, y;

        private MyButtonListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("(" + x + "." + y + ")");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public static void main(String[] args) {
        new GameGUI(null);
    }
}

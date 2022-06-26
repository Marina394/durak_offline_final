package ru.ccfit.idrisova.task3;

import javax.swing.*;
import java.awt.*;

class GameFrame extends JFrame {
    final private GameModel gameModel;
    final private GameView gameView;

    public GameFrame() {
        final int WIDTH = 800;
        final int HEIGHT = 600;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Durak Game");
        this.setSize(WIDTH, HEIGHT);

        gameModel = new GameModel();
        gameView = new GameView(gameModel, gameModel);
        this.getContentPane().add(gameView);

        createMenu();
    }
    private void createMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem m11 = new JMenuItem("New Game");
        JMenuItem m12 = new JMenuItem("Exit");
        m11.setToolTipText("Start a New Game");
        m11.addActionListener((e) -> this.startGame());
        m12.setToolTipText("Exit application");
        m12.addActionListener((event) -> System.exit(0));
        m1.add(m11);
        m1.addSeparator();
        m1.add(m12);
        JMenuItem m21 = new JMenuItem("About");
        m21.addActionListener((e) -> this.showHelp());
        m2.add(m21);
        this.getContentPane().add(BorderLayout.NORTH, mb);
    }
    private void startGame() {
        if (null != gameModel)
            gameModel.startGame();

        gameView.repaint();
    }
    private void showHelp(){
        JOptionPane.showMessageDialog(null,
                "Some words about this program", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}

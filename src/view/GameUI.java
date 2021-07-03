package view;

/*
 * The View class is responsible for setting up the gui and
 * displaying the state of the game on the gui as informed by
 * the model.
 */

import adapter.GameAdapter;
import components.Mark;
import controller.Controller;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class GameUI {
    private JFrame gui;
    private GameAdapter adapter;
    private JButton[][] blocks;
    private JButton reset;
    private JButton exit;
    private JTextArea playerTurn;
    private JLabel score;
    private JPanel gamePanel;
    private JPanel game;
    private JPanel scorePanel;
    private JPanel options;
    private JPanel messages;
    private int boardSize;

    // default constructor to initialize the gui as JFrame
    public GameUI(JFrame gui) {
        this.gui = gui;
        this.boardSize = Constants.BOARD_SIZE_DEFAULT;
        this.blocks = new JButton[boardSize][boardSize];
        this.reset = new JButton("Reset");
        this.exit = new JButton("Exit");
        this.playerTurn = new JTextArea();
        initialize();
    }

    // function to add action listeners to buttons
    public void setActionListener(Controller c) {
        // adapter needs reference of controller and view class
        this.adapter = new GameAdapter(c, this);
        this.setBlocksListener();

        reset.addActionListener(adapter);
        exit.addActionListener(e -> {
            if (isExit(e)) {
                gui.getContentPane().removeAll();
                gui.repaint();
                c.setGameSaveRequest();
                c.setGameSaveHistoryRequest();
                c.setUIRequest(Constants.UIConstants.UI_MENU);
            }
        });
    }

    public void setBlocksListener() {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (blocks[row][column].getActionListeners().length == 0)
                    blocks[row][column].addActionListener(adapter);
            }
        }
    }

    // function to initialize layout and buttons
    public void initialize() {
        this.gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gui.setSize(new Dimension(500, 600));
        this.gui.setResizable(false);

        Box gameBox = Box.createVerticalBox();

        gamePanel = new JPanel(new FlowLayout());

        scorePanel = new JPanel(new BorderLayout());
        score = new JLabel("0-0");
        score.setFont(new Font("Arial", Font.PLAIN, 45));
        score.setHorizontalAlignment(SwingConstants.CENTER);
        score.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.add(score, BorderLayout.CENTER);

        game = new JPanel(new GridLayout(boardSize, boardSize));
        gameBox.add(score);
        gameBox.add(game);
        gamePanel.add(gameBox, BorderLayout.CENTER);

        options = new JPanel(new FlowLayout());
        options.add(reset);
        options.add(exit);

        messages = new JPanel(new FlowLayout());
        messages.setBackground(Color.white);
        messages.add(playerTurn);
        playerTurn.setText("Player 1 to play 'X'");

        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                blocks[row][column] = new JButton();
                blocks[row][column].setPreferredSize(new Dimension(81 - (boardSize * 2), 81 - (boardSize * 2)));
                blocks[row][column].setFont(new Font("Arial", Font.BOLD, 51 - (boardSize * 21 / 10)));
                blocks[row][column].setText("");
                game.add(blocks[row][column]);
            }
        }
    }

    public boolean isExit(ActionEvent e) {
        if (e.getSource() != exit)
            return false;

        int confirm = JOptionPane.showConfirmDialog(this.gui.getParent(),
                                                "Do you really want to exit from current game?", "Exit", JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }

    // function to check if the action event was generated because of reset key
    public boolean isReset(ActionEvent e) {
        return e.getSource() == reset;
    }

    // function to find the x,y-coordinates of the pressed button
    public ArrayList<Integer> getPosition(ActionEvent e) {
        ArrayList<Integer> position = new ArrayList<Integer>();
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (e.getSource() == blocks[row][column]) {
                    position.add(row);
                    position.add(column);
                }
            }
        }
        return position;
    }

    // function to update the view with the correct mark and message
    public void update(int row, int column, Mark symbol, String message) {
        blocks[row][column].setText(Character.toString(symbol.getMark()));
        blocks[row][column].setEnabled(false);
        playerTurn.setText(message);
    }

    // function to freeze the view if there is a winner or game is tied
    public void isWinner(int row, int column, Mark symbol, String message) {
        blocks[row][column].setText(Character.toString(symbol.getMark()));
        blocks[row][column].setEnabled(false);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                blocks[i][j].setEnabled(false);
            }
        }
        playerTurn.setText(message);
    }

    // function to clear the view and reset it for a new game
    public void resetGame() {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                blocks[row][column].setText("");
                blocks[row][column].setEnabled(true);
            }
        }
    }

    public void requestView() {
        this.gui.getContentPane().removeAll();
        this.gui.repaint();
        this.gui.add(gamePanel, BorderLayout.NORTH);
        this.gui.add(options, BorderLayout.CENTER);
        this.gui.add(messages, BorderLayout.SOUTH);
        this.gui.setVisible(true);
        this.gui.pack();
    }

    // mock getter function for checking the value of a button on the grid
    public String getButtonText(int i, int j) {
        return blocks[i][j].getText();
    }

    public void setPlayerTurnText(String text) {
        playerTurn.setText(text);
    }

    public void setScore(String score) {
        this.score.setText(score);
    }

    public void setGameConfiguration(int boardSize) {
        this.boardSize = boardSize;
        this.blocks = new JButton[boardSize][boardSize];
        this.initialize();

        if (adapter != null)
            adapter.setGameUI(this);

        this.setBlocksListener();
    }

    public boolean checkSave() {
        int confirm = JOptionPane.showConfirmDialog(this.gui.getParent(),
                "Do you want to save current game progress?", "Tic Tac Toe", JOptionPane.YES_NO_OPTION);

        return confirm == JOptionPane.YES_OPTION;
    }

    public boolean checkResume() {
        int confirm = JOptionPane.showConfirmDialog(this.gui.getParent(),
                "Do you want to resume previous game?", "Tic Tac Toe", JOptionPane.YES_NO_OPTION);

        return confirm == JOptionPane.YES_OPTION;
    }

    public void showResumeNotFound() {
        JOptionPane.showMessageDialog(this.gui.getParent(),
                "Previous game data not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showFailedDbConnection() {
        JOptionPane.showMessageDialog(this.gui.getParent(),
                "Failed to connect to database server!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void requestResume() {
        this.setBlocksListener();
        this.requestView();
    }
}

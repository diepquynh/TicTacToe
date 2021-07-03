
/*
 * Game configuration UI
 */

package view;

import components.ComboBoxRenderer;
import controller.Controller;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class SelectionUI {
    // Basic GUI
    private JFrame gui;
    private JPanel modePanel;
    private JPanel botPanel;
    private JPanel sizePanel;
    private JPanel bottomPanel;
    private JPanel container;
    private JLabel modeLabel;
    private JLabel difficultyLabel;
    private JLabel sizeLabel;
    private JComboBox difficulties;
    private JButton backButton;
    private JButton startGameButton;

    // Properties
    private JRadioButton modeComputer;
    private JRadioButton modeHuman;
    private JSpinner sizeSpinner;

    public SelectionUI(JFrame gui) {
        this.gui = gui;
        this.gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font textFont = new Font("Arial", Font.PLAIN, 15);
        ButtonGroup modeGroup = new ButtonGroup();
        Box modeBox = Box.createVerticalBox();

        modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        modeLabel = new JLabel("Select game mode:");
        modeLabel.setFont(textFont);
        modePanel.setBorder(BorderFactory.createTitledBorder("Game mode"));
        modeComputer = new JRadioButton("Play with computer (offline-only)");
        modeComputer.setFont(textFont);
        modeComputer.addItemListener(e -> difficulties.setEnabled(e.getStateChange() == ItemEvent.SELECTED));

        modeHuman = new JRadioButton("Play with person");
        modeHuman.setFont(textFont);
        modeHuman.setSelected(true);
        modeGroup.add(modeComputer);
        modeGroup.add(modeHuman);
        modeBox.add(modeLabel);
        modeBox.add(modeComputer);
        modeBox.add(modeHuman);
        modePanel.add(modeBox);

        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        difficulties = new JComboBox(difficultyLevels);
        difficulties.setFont(textFont);
        difficulties.setRenderer(new ComboBoxRenderer("Select"));
        difficulties.setSelectedIndex(-1);
        difficulties.setEnabled(false);
        botPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyLabel = new JLabel("Select difficulty");
        difficultyLabel.setFont(textFont);
        botPanel.setBorder(BorderFactory.createTitledBorder("Bot difficulty"));
        botPanel.add(difficultyLabel);
        botPanel.add(difficulties);

        SpinnerModel sizeModel = new SpinnerNumberModel(13, 3, 100, 1);
        sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sizeLabel = new JLabel("Board size:");
        sizeLabel.setFont(textFont);
        sizeSpinner = new JSpinner(sizeModel);
        sizeSpinner.setFont(textFont);
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeSpinner);

        GridBagConstraints gbc = new GridBagConstraints();
        bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Board size adjustments"));
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 1;
        bottomPanel.add(sizePanel, gbc);

        backButton = new JButton("Back");
        backButton.setFont(textFont);
        backButton.setPreferredSize(new Dimension(90, 30));
        startGameButton = new JButton("Start game");
        startGameButton.setFont(textFont);
        startGameButton.setPreferredSize(new Dimension(130, 30));
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        bottomPanel.add(backButton, gbc);
        bottomPanel.add(startGameButton, gbc);

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(modePanel);
        container.add(botPanel);
        container.add(bottomPanel);
    }

    public int getBotDifficulty() {
        int difficulty = 0;

        switch (difficulties.getSelectedItem().toString()) {
            case "Easy":
                difficulty = Constants.GameModes.MODE_COMPUTER_EASY;
                break;
            case "Medium":
                difficulty = Constants.GameModes.MODE_COMPUTER_MEDIUM;
                break;
            case "Hard":
                difficulty = Constants.GameModes.MODE_COMPUTER_HARD;
                break;
        }

        return difficulty;
    }

    public void setActionListener(Controller c) {
        backButton.addActionListener(e -> c.setUIRequest(Constants.UIConstants.UI_MENU));

        startGameButton.addActionListener(e -> {
            boolean isBotMode = modeComputer.isSelected();
            if (isBotMode && difficulties.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(null,
                        "Please select computer difficulty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextField firstPlayerName = new JTextField();
            JTextField secondPlayerName = new JTextField();

            if (isBotMode) {
                secondPlayerName.setText("Computer");
                secondPlayerName.setEnabled(false);
            }

            Object[] message = {
                    "First player name:", firstPlayerName,
                    "Second player name:", secondPlayerName
            };

            do {
                int confirm = JOptionPane.showConfirmDialog(null, message,
                        "Enter your name", JOptionPane.OK_CANCEL_OPTION);
                if (confirm == JOptionPane.CANCEL_OPTION || confirm == JOptionPane.CLOSED_OPTION)
                    return;

                if (firstPlayerName.getText().length() == 0 || secondPlayerName.getText().length() == 0)
                    JOptionPane.showMessageDialog(null,
                            "Player names cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } while (firstPlayerName.getText().length() == 0 || secondPlayerName.getText().length() == 0);

            c.setGamePlayerNames(firstPlayerName.getText(), secondPlayerName.getText());
            c.setConfigModel(isBotMode ? getBotDifficulty() : Constants.GameModes.MODE_HUMAN,
                    Integer.parseInt(sizeSpinner.getValue().toString()));
            c.setUIRequest(Constants.UIConstants.UI_GAME);
        });
    }

    public void requestView() {
        this.gui.getContentPane().removeAll();
        this.gui.revalidate();
        this.gui.repaint();
        this.gui.add(container, BorderLayout.CENTER);
        this.gui.setVisible(true);
        this.gui.pack();
        this.gui.setSize(500, 300);
    }
}

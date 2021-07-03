package view;

/*
 * This class is responsible for showing game menu graphical
 * user interface
 */

import controller.Controller;
import utils.Constants;

import javax.swing.*;
import java.awt.*;

import static utils.ImageUtils.resizeIcon;

public class MenuUI {
    private JFrame gui;
    private JButton playButton;
    private JButton resumeButton;
    private JButton historyButton;
    private JButton quitButton;
    private JPanel container;
    private JPanel btnPanel;
    private JPanel logoPanel;
    private JLabel logo;

    public MenuUI(JFrame gui) {
        this.gui = gui;
        this.playButton = new JButton("Play new game");
        this.resumeButton = new JButton("Resume game");
        this.historyButton = new JButton("View game history");
        this.quitButton = new JButton("Quit the game");

        this.playButton.setFont(new Font("Arial", Font.PLAIN, 20));
        this.resumeButton.setFont(new Font("Arial", Font.PLAIN, 20));
        this.historyButton.setFont(new Font("Arial", Font.PLAIN, 20));
        this.quitButton.setFont(new Font("Arial", Font.PLAIN, 20));

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        logo = new JLabel();
        logo.setIcon(new ImageIcon(getClass().getResource("/resources/images/logo.png")));
        logoPanel = new JPanel();
        logoPanel.add(this.logo, BorderLayout.CENTER);

        btnPanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.ipady = 30;

        playButton.setIcon(resizeIcon(new ImageIcon(getClass().getResource("/resources/icons/play.png")), 30, 30));
        resumeButton.setIcon(resizeIcon(new ImageIcon(getClass().getResource("/resources/icons/resume.png")), 30, 30));
        historyButton.setIcon(resizeIcon(new ImageIcon(getClass().getResource("/resources/icons/history.png")), 30, 30));
        quitButton.setIcon(resizeIcon(new ImageIcon(getClass().getResource("/resources/icons/stop.png")), 30, 30));

        playButton.setHorizontalAlignment(SwingConstants.LEFT);
        resumeButton.setHorizontalAlignment(SwingConstants.LEFT);
        historyButton.setHorizontalAlignment(SwingConstants.LEFT);
        quitButton.setHorizontalAlignment(SwingConstants.LEFT);

        btnPanel.setLayout(new GridBagLayout());
        btnPanel.add(this.playButton, gbc);
        btnPanel.add(this.resumeButton, gbc);
        btnPanel.add(this.historyButton, gbc);
        btnPanel.add(this.quitButton, gbc);

        container.add(logoPanel);
        container.add(btnPanel);

        requestView();
    }

    public void addButtonListener(Controller c) {
        playButton.addActionListener(e -> c.setUIRequest(Constants.UIConstants.UI_SELECTION));

        resumeButton.addActionListener(e -> c.setGameResumeRequest());

        historyButton.addActionListener(e -> c.setUIRequest(Constants.UIConstants.UI_HISTORY));

        quitButton.addActionListener(e -> gui.dispose());
    }

    public void requestView() {
        this.gui.getContentPane().removeAll();
        this.gui.repaint();
        this.gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gui.setSize(500, 700);
        this.gui.setResizable(false);
        this.gui.add(container);
        this.gui.setLocationRelativeTo(null);
        this.gui.setVisible(true);
//        gui.pack();
    }
}

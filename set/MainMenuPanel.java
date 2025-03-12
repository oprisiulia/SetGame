package set;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private static final Color PURPLE = new Color(128, 0, 128);
    private static final Color DARK_GRAY_BG = new Color(40, 40, 40);

    // parent ablak referencia, tartalmazza ezt a panelt
    private SetGame parent;

    public MainMenuPanel(SetGame parent) {
        this.parent = parent;
        setBackground(Color.BLACK);

        setLayout(new GridBagLayout());
        //poz
        GridBagConstraints gbc = new GridBagConstraints();
        // 20 pixel margok
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // hozzaadjuk a cimet a panel elso soraba
        JLabel title = new JLabel("Set Game");
        title.setFont(new Font("Courier New", Font.BOLD, 40));
        title.setForeground(Color.GREEN);
        add(title, gbc);

        // Start Game gomb
        gbc.gridy++; // kov sor
        JButton startButton = createButton("Start Game", Color.GREEN);
        startButton.addActionListener(e -> {
            SoundManager.playSound("button.wav");
            parent.showPanel(SetGame.GAME_PANEL);
        });
        add(startButton, gbc);

        // Start Game gomb
        gbc.gridy++;
        JButton highScoresButton = createButton("High Scores", PURPLE);
        highScoresButton.addActionListener(e -> {
            SoundManager.playSound("button.wav");
            parent.showPanel(SetGame.HIGH_SCORES_PANEL);
        });
        add(highScoresButton, gbc);

        // Settings gomb
        gbc.gridy++;
        JButton settingsButton = createButton("Settings", Color.RED);
        settingsButton.addActionListener(e -> {
            SoundManager.playSound("button.wav");
            parent.showPanel(SetGame.SETTINGS_PANEL);
        });
        add(settingsButton, gbc);

        // Help gomb
        gbc.gridy++;
        JButton helpButton = createButton("Help", Color.GREEN);
        helpButton.addActionListener(e -> {
            SoundManager.playSound("button.wav");
            showHelpDialog();
        });
        add(helpButton, gbc);

        // Exit gomb
        gbc.gridy++;
        JButton exitButton = createButton("Exit", PURPLE);
        exitButton.addActionListener(e -> {
            SoundManager.playSound("button.wav");
            System.exit(0);
        });
        add(exitButton, gbc);
    }

    private JButton createButton(String text, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Courier New", Font.PLAIN, 24));
        button.setForeground(textColor);
        button.setBackground(DARK_GRAY_BG);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(textColor, 2));
        return button;
    }

    private void showHelpDialog() {
        String helpText = "How to play Set:\n\n" +
                "1. Click 'Start Game' to begin.\n" +
                "2. Select 3 cards forming a valid set.\n" +
                "3. If stuck, press 'Show Hint'.\n" +
                "4. Enjoy!\n";

        JOptionPane.showMessageDialog(
                this,
                helpText,
                "Help",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

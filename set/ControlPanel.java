package set;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private GamePanel gamePanel;

    private JButton newGameButton;
    private JButton hintButton;
    private JButton mainMenuButton;

    public ControlPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setLayout(new FlowLayout());

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            SoundManager.playSound("button.wav");
            gamePanel.startNewGame();
        });
        add(newGameButton);

        hintButton = new JButton("Show Hint");
        hintButton.addActionListener(e -> {
            SoundManager.playSound("hint.wav");
            gamePanel.showHint();
        });
        add(hintButton);

        mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(e -> {
            SoundManager.playSound("button.wav");
            gamePanel.goToMainMenu();
        });
        add(mainMenuButton);
    }
}

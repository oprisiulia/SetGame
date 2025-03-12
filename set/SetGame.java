package set;

import javax.swing.*;
import java.awt.*;

public class SetGame extends JFrame {
    public static final String MAIN_MENU = "MainMenu";
    public static final String GAME_PANEL = "GamePanel";
    public static final String HIGH_SCORES_PANEL = "HighScores";
    public static final String SETTINGS_PANEL = "Settings";

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HighScoreManager highScoreManager;

    // referenciak a panelekre osztalyszinten
    private MainMenuPanel mainMenu;
    private GamePanel gamePanel;
    private HighScoresPanel highScoresPanel;
    private SettingsPanel settingsPanel;

    public SetGame() {
        setTitle("Set Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // kozep

        // a pontszamokat betoltjuk filebol
        highScoreManager = new HighScoreManager();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // atadjuk a paneleknek az aktualis osztalyt, hogy ferjenek hozza
        mainMenu = new MainMenuPanel(this);
        gamePanel = new GamePanel(this, highScoreManager);
        highScoresPanel = new HighScoresPanel(this, highScoreManager);
        settingsPanel = new SettingsPanel(this);

        mainPanel.add(mainMenu, MAIN_MENU);
        mainPanel.add(gamePanel, GAME_PANEL);
        mainPanel.add(highScoresPanel, HIGH_SCORES_PANEL);
        mainPanel.add(settingsPanel, SETTINGS_PANEL);

        add(mainPanel);
        showPanel(MAIN_MENU);
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void updateGamePanelColors() {
        Color newHighlightColor = settingsPanel.getHighlightColor();
        Color newHintColor = settingsPanel.getHintColor();

        gamePanel.setHighlightColor(newHighlightColor);
        gamePanel.setHintColor(newHintColor);
    }

    public void updateHighScoresPanel() {
        highScoresPanel.refreshHighScores();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SetGame game = new SetGame();
            game.setVisible(true);
        });
    }
}

package set;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

// fo jatekpanel, ez kezeli a jatekot
public class GamePanel extends JPanel {
    private SetGame parent;
    private HighScoreManager highScoreManager;
    private String playerName;

    private StatusPanel statusPanel;
    private ControlPanel controlPanel;
    private CardPanel cardPanel;

    private Deck deck;
    private List<Card> boardCards;
    private List<Card> selectedCards;

    private Color highlightColor = Color.YELLOW;
    private Color hintColor = Color.CYAN;

    public GamePanel(SetGame parent, HighScoreManager highScoreManager) {
        this.parent = parent;
        this.highScoreManager = highScoreManager;
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        statusPanel = new StatusPanel();
        controlPanel = new ControlPanel(this);
        cardPanel = new CardPanel(this);

        statusPanel.setBackground(Color.BLACK);
        controlPanel.setBackground(Color.BLACK);
        cardPanel.setBackground(Color.BLACK);

        add(statusPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        //elinditunk egy jatekot
        startNewGame();
    }

    public void goToMainMenu() {
        // parbeszed
        UIManager.put("OptionPane.background", Color.BLACK);
        UIManager.put("Panel.background", Color.BLACK);
        UIManager.put("OptionPane.messageForeground", Color.GREEN);
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 14));
        UIManager.put("OptionPane.foreground", Color.GREEN);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "<html><body style='color:green;font-family:Arial;font-size:14px;'>"
                        + "Are you sure you want to return to the main menu?<br>"
                        + "Your current game will be lost!</body></html>",
                "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            statusPanel.stopTimer();
            startNewGame();
            parent.showPanel(SetGame.MAIN_MENU);
        }
    }

    public void startNewGame() {
        statusPanel.reset();

        // letrehozunk es osszekeverunk egy uj paklit
        deck = new Deck();
        deck.shuffle();
        boardCards = new ArrayList<>();
        selectedCards = new ArrayList<>();

        //toroljuk a kartyapanelt, osztunk egy kartyat es hozzaadjuk a tablahoz ill a kartyapanelhez
        cardPanel.clearCards();
        for (int i = 0; i < 12 && !deck.isEmpty(); i++) {
            Card card = deck.dealCard();
            boardCards.add(card);
            cardPanel.addCard(card);
        }

        // idozites
        statusPanel.startTimer();
        revalidate();
        repaint();
    }

    // kartyak kivalasztasa
    public void handleCardSelection(Card card, JButton button) {
        // ha mar be van jelolve kattra nem lesz
        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
            button.setBackground(new Color(60, 60, 60));
        } else {
            selectedCards.add(card);
            button.setBackground(highlightColor);
        }

        // ha van 3 kartyank, vizsgaljuk, hogy set-e vagy sem
        if (selectedCards.size() == 3) {
            if (isSet(selectedCards.get(0), selectedCards.get(1), selectedCards.get(2))) {
                showSetFoundThenReplace(new ArrayList<>(selectedCards));
            } else {
                statusPanel.setMessage("Invalid Set! Try again.");
                SoundManager.playSound("incorrect.wav");
            }
            selectedCards.clear();
            cardPanel.resetCardHighlights();
        }
    }

    // helyes set kezelese
    private void showSetFoundThenReplace(List<Card> setCards) {
        statusPanel.setMessage("SET FOUND!");

        // kesleltetes 0.3 mp
        Timer t = new Timer(300, e -> {
            replaceSet(setCards);
        });
        t.setRepeats(false);
        t.start();
    }

    // set feltetelei: minden tulajdonsagban a kartyak vagy azonosak (pl. minden piros), vagy kulonboznek (pl 1,2,3 db)
    public boolean isSet(Card a, Card b, Card c) {
        return allSameOrAllDifferent(a.getCount(), b.getCount(), c.getCount()) &&
                allSameOrAllDifferent(a.getShape(), b.getShape(), c.getShape()) &&
                allSameOrAllDifferent(a.getColor(), b.getColor(), c.getColor()) &&
                allSameOrAllDifferent(a.getShading(), b.getShading(), c.getShading());
    }

    private <T> boolean allSameOrAllDifferent(T a, T b, T c) {
        boolean allSame = a.equals(b) && b.equals(c);
        boolean allDifferent = !a.equals(b) && !a.equals(c) && !b.equals(c);
        return allSame || allDifferent;
    }

    //
    private void replaceSet(List<Card> setCards) {
        for (Card selectedCard : setCards) {
            int index = boardCards.indexOf(selectedCard);
            if (index != -1) {
                boardCards.remove(index);
                cardPanel.removeCard(index);

                // uj kartyat osztunk
                if (!deck.isEmpty()) {
                    Card newCard = deck.dealCard();
                    boardCards.add(index, newCard);
                    cardPanel.addCard(newCard, index);
                }
            }
        }
        cardPanel.refresh();

        // ha nincs set a tablan, de meg van a pakliban, akkor tovabbi kartyakat osztunk
        if (!isAnySetAvailable()) {
            if (deck.isEmpty()) {
                endGame("No more sets available and the deck is exhausted. Game Over!");
            } else {
                dealAdditionalCards();
            }
        } else {
            statusPanel.setMessage("Find a Set!");
        }
    }

    // vizsgaljuk, ha van-e set a tablan
    public boolean isAnySetAvailable() {
        int size = boardCards.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                for (int k = j + 1; k < size; k++) {
                    Card a = boardCards.get(i);
                    Card b = boardCards.get(j);
                    Card c = boardCards.get(k);
                    if (isSet(a, b, c)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ha nincs set a tablan de nem ures a pakli
    private void dealAdditionalCards() {
        // tovabbi 3 kartyat adunk a tablahoz
        int cardsToDeal = 3;
        for (int i = 0; i < cardsToDeal; i++) {
            if (deck.isEmpty()) break;
            Card newCard = deck.dealCard();
            boardCards.add(newCard);
            cardPanel.addCard(newCard);
        }
        cardPanel.refresh();

        // ha ezutan sincs set akkor vege a jateknak
        if (!isAnySetAvailable()) {
            if (deck.isEmpty()) {
                endGame("No sets available and the deck is exhausted. Game Over!");
            } else {
                statusPanel.setMessage("No sets available. Dealing more cards...");
            }
        } else {
            statusPanel.setMessage("Find a Set!");
        }
    }

    // jatek vege
    private void endGame(String message) {
        statusPanel.setMessage(message);
        cardPanel.disableAllCards();
        statusPanel.stopTimer();

        // bekerjuk a jatekos nevet
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = JOptionPane.showInputDialog(
                    this,
                    "Enter your name:",
                    "Player Name",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (playerName == null || playerName.trim().isEmpty()) { // trim - whitespace karaktereket tav el
                playerName = "Player";
            }
        }

        // elmentjuk a pontszamat
        HighScore newScore = new HighScore(playerName, statusPanel.getElapsedTime());
        highScoreManager.addHighScore(newScore);

        parent.updateHighScoresPanel();

        // parbeszed
        int response = JOptionPane.showConfirmDialog(
                this,
                message + "\nDo you want to start a new game?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
        );

        if (response == JOptionPane.YES_OPTION) {
            startNewGame();
            cardPanel.enableAllCards();
        } else {
            parent.showPanel(SetGame.MAIN_MENU);
        }
    }

    public void showHint() {
        cardPanel.showHint();
    }

    public List<Card> getBoardCards() {
        return boardCards;
    }

    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    public void setHighlightColor(Color color) {
        this.highlightColor = color;
        statusPanel.setHighlightColor(color);
        cardPanel.setHighlightColor(color);
        revalidate();
        repaint();
    }

    public void setHintColor(Color color) {
        this.hintColor = color;
        cardPanel.setHintColor(color);
        revalidate();
        repaint();
    }
}

package set;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CardPanel extends JPanel {
    private GamePanel gamePanel;
    private List<JButton> cardButtons;

    private Color highlightColor = Color.YELLOW;
    private Color hintColor = Color.CYAN;

    public CardPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.cardButtons = new ArrayList<>();
        setLayout(new GridLayout(3, 4, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.BLACK);
    }

    // hozzaad egy kartyat a panelhez - kov elerheto pozicio
    public void addCard(Card card) {
        JButton cardButton = createCardButton(card);
        cardButtons.add(cardButton);
        add(cardButton);
    }

    // hozzaad egy kartyat a panelhez - adott index
    public void addCard(Card card, int index) {
        JButton cardButton = createCardButton(card);
        cardButtons.add(index, cardButton);
        add(cardButton, index);
    }

    // eltavolit egy kartyat a panelrol - adott index
    public void removeCard(int index) {
        remove(index);
        cardButtons.remove(index);
    }

    // torol minden kartyat a panelbol
    public void clearCards() {
        removeAll();
        cardButtons.clear();
    }

    // a panelt ujrarendezi
    public void refresh() {
        revalidate();
        repaint();
    }

    // az osszes kartyagombot tiltja le
    public void disableAllCards() {
        for (JButton button : cardButtons) {
            button.setEnabled(false);
        }
    }

    // engedelyezi a kartyagombokat
    public void enableAllCards() {
        for (JButton button : cardButtons) {
            button.setEnabled(true);
            button.setBackground(new Color(60, 60, 60));
        }
    }

    // kartyagombok hatterszinet alaphelyzetbe allitja be
    public void resetCardHighlights() {
        for (JButton button : cardButtons) {
            button.setBackground(new Color(60, 60, 60));
        }
    }

    // JButton, ami a kartyat reprezentalja
    private JButton createCardButton(Card card) {
        JButton cardButton = new JButton();
        cardButton.setPreferredSize(new Dimension(150, 100));
        cardButton.setBackground(new Color(60, 60, 60));
        cardButton.setFocusPainted(false);

        ImageIcon cardIcon = loadImage(card.getImageFileName());
        if (cardIcon != null) {
            Image scaled = cardIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            cardButton.setIcon(new ImageIcon(scaled));
        } else {
            cardButton.setText("Image not found");
            cardButton.setFont(new Font("Arial", Font.PLAIN, 12));
            cardButton.setForeground(Color.WHITE);
        }

        // kattintast kezelo listener
        cardButton.addActionListener(new CardButtonListener(card, cardButton, gamePanel));

        return cardButton;
    }

    private ImageIcon loadImage(String fileName) {
        String path = "/img/" + fileName;
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Image not found: " + path);
                return null;
            }
            return new ImageIcon(ImageIO.read(is));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // sugo - kiemel egy setet a tablan
    public void showHint() {
        resetCardHighlights();
        // keresunk egy ervenyes setet
        List<Card> set = findAnySet();
        if (set != null) {
            for (Card c : set) {
                // megkeressuk a kartya indexet
                int index = gamePanel.getBoardCards().indexOf(c);
                if (index != -1) {
                    cardButtons.get(index).setBackground(hintColor);
                }
            }
            gamePanel.getStatusPanel().setMessage("Hint: A set is highlighted.");
        } else {
            gamePanel.getStatusPanel().setMessage("No sets available!");
        }
    }

    // keres egy ervenyes setet es visszateriti a set kartyai listajat vagy nullt
    private List<Card> findAnySet() {
        // lekerjuk a jetektabla kartyait
        List<Card> cards = gamePanel.getBoardCards();
        int size = cards.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                for (int k = j + 1; k < size; k++) {
                    Card a = cards.get(i);
                    Card b = cards.get(j);
                    Card c = cards.get(k);
                    // ha kapunk egy ervenyes setet, visszateritjuk a talalt listaban
                    if (gamePanel.isSet(a, b, c)) {
                        List<Card> found = new ArrayList<>();
                        found.add(a);
                        found.add(b);
                        found.add(c);
                        return found;
                    }
                }
            }
        }
        return null;
    }

    public void setHighlightColor(Color color) {
        this.highlightColor = color;
    }

    public void setHintColor(Color color) {
        this.hintColor = color;
    }

    public List<JButton> getCardButtons() {
        return cardButtons;
    }

}

package set;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardButtonListener implements ActionListener {
    private Card card;
    private JButton cardButton;
    private GamePanel gamePanel;

    public CardButtonListener(Card card, JButton cardButton, GamePanel gamePanel) {
        this.card = card;
        this.cardButton = cardButton;
        this.gamePanel = gamePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SoundManager.playSound("select.wav");
        gamePanel.handleCardSelection(card, cardButton);
    }
}

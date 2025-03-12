package set;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusPanel extends JPanel {
    private JLabel messageLabel;
    private JLabel timerLabel;
    private Timer gameTimer;
    private int elapsedTime;

    private Color highlightColor = Color.YELLOW;

    public StatusPanel() {
        setLayout(new BorderLayout());

        messageLabel = new JLabel("Find a Set!");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setForeground(Color.GREEN);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel, BorderLayout.CENTER);

        timerLabel = new JLabel("Time: 0 seconds");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(timerLabel, BorderLayout.EAST);

        // idozito
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                timerLabel.setText("Time: " + elapsedTime + " seconds");
            }
        });
    }

    public void startTimer() {
        elapsedTime = 0;
        timerLabel.setText("Time: 0 seconds");
        gameTimer.start();
    }

    public void stopTimer() {
        gameTimer.stop();
    }

    public void reset() {
        setMessage("Find a Set!");
        startTimer();
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setHighlightColor(Color color) {
        this.highlightColor = color;
    }
}

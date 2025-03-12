package set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.util.List;

public class HighScoresPanel extends JPanel {
    private SetGame parent;
    private HighScoreManager highScoreManager;

    //HTML alapu megjelenito (pontszamok megjelenitese)
    private JEditorPane highScoresEditor;

    public HighScoresPanel(SetGame parent, HighScoreManager highScoreManager) {
        this.parent = parent;
        this.highScoreManager = highScoreManager;

        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Top 10 High Scores");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.RED);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        //HTML szovegmegjelenites
        highScoresEditor = new JEditorPane("text/html", "");
        highScoresEditor.setEditable(false);
        highScoresEditor.setOpaque(true);
        highScoresEditor.setBackground(Color.BLACK);

        //gorgetopanel
        JScrollPane scrollPane = new JScrollPane(highScoresEditor);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.BLACK);
        add(scrollPane, BorderLayout.CENTER);

        // Back to Menu gomb
        JButton backButton = new JButton("Back to Menu");
        styleBottomButton(backButton, Color.MAGENTA);

        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.addActionListener(e -> parent.showPanel(SetGame.MAIN_MENU));
        add(backButton, BorderLayout.SOUTH);

        updateHighScores();
    }

    public void refreshHighScores() {
        updateHighScores();
    }

    //HTML
    private void updateHighScores() {
        List<HighScore> scores = highScoreManager.getHighScores();

        // top 10
        int size = Math.min(10, scores.size());

        // HTML string epites
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='background-color:black;color:green;text-align:center;"
                + "font-family:Courier New;font-size:18px;'>");

        sb.append("<h3 style='color:yellow;'>Here are the fastest times:</h3>");

        if (size == 0) {
            sb.append("<p>No high scores yet!</p>");
        } else {
            for (int i = 0; i < size; i++) {
                HighScore s = scores.get(i);
                sb.append("<p>")
                        .append((i + 1))
                        .append(". ")
                        .append(s.getPlayerName())
                        .append(" - ")
                        .append(s.getScore())
                        .append("</p>");
            }
        }

        sb.append("</body></html>");

        //beallitjuk a html szoveget es a lista tetejere megyunk
        highScoresEditor.setText(sb.toString());
        highScoresEditor.setCaretPosition(0);
    }

    private void styleBottomButton(JButton button, Color textColor) {
        button.setUI(new BasicButtonUI());
        button.setForeground(textColor);
        button.setBackground(new Color(60, 60, 60));
        button.setBorder(new LineBorder(textColor, 2));
        button.setFocusPainted(false);
        button.setOpaque(true);
    }
}

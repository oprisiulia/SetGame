package set;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {
    private static final String HIGH_SCORES_FILE = "highscores.txt";

    private List<HighScore> highScores;

    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadHighScoresFromFile();
    }

    public void addHighScore(HighScore score) {
        highScores.add(score);
        Collections.sort(highScores, (a, b) -> a.getScore() - b.getScore());
        saveHighScoresToFile();
    }

    public List<HighScore> getHighScores() {
        return highScores;
    }

    private void loadHighScoresFromFile() {
        File file = new File(HIGH_SCORES_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // ures sort kihagyjuk

                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String playerName = parts[0];
                    int score;
                    try {
                        score = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        continue; // ha a pontszam nem szam akkor a sort kihagyjuk
                    }
                    highScores.add(new HighScore(playerName, score));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(highScores, (a, b) -> a.getScore() - b.getScore());
    }

    //fileba mentjuk a pontszamokat (az ido)
    private void saveHighScoresToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HIGH_SCORES_FILE))) {
            for (HighScore hs : highScores) {
                writer.println(hs.getPlayerName() + " " + hs.getScore());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

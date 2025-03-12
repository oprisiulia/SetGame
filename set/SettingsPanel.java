package set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class SettingsPanel extends JPanel {
    private SetGame parent;

    // letrehoz egy csp-t amivel biztositjuk az osztaly beallitasainak a tarolasat
    private Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
    private  String PREF_HIGHLIGHT_COLOR = "highlightColor";
    private  String PREF_HINT_COLOR = "hintColor";

    private Color highlightColor = Color.YELLOW;
    private Color hintColor = Color.CYAN;

    public SettingsPanel(SetGame parent) {
        this.parent = parent;
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Settings cim
        JLabel settingsLabel = new JLabel("Settings");
        settingsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        settingsLabel.setForeground(Color.ORANGE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(settingsLabel, gbc);

        // betoltjuk a szineket
        highlightColor = getColorFromPreferences(PREF_HIGHLIGHT_COLOR, Color.YELLOW);
        hintColor = getColorFromPreferences(PREF_HINT_COLOR, Color.CYAN);

        // highlightColor beallitasa
        gbc.gridwidth = 1;
        gbc.gridy++;
        // elso oszlopban
        gbc.gridx = 0;
        JLabel highlightLabel = new JLabel("Highlight Color:");
        highlightLabel.setForeground(Color.WHITE);
        add(highlightLabel, gbc);

        // masodik oszlopban
        gbc.gridx = 1;
        JPanel highlightColorPanel = createColorSelectionPanel("Highlight", highlightColor,
                newColor -> {
                // ha uj szint valasztunk akkor frissitunk, elmentjuk es ertesitjuk a parentet
                    highlightColor = newColor;
                    saveColorToPreferences(PREF_HIGHLIGHT_COLOR, highlightColor);
                    parent.updateGamePanelColors();
                },
                getHighlightColors()
        );
        add(highlightColorPanel, gbc);

        // hintColor
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel hintLabel = new JLabel("Hint Color:");
        hintLabel.setForeground(Color.WHITE);
        add(hintLabel, gbc);

        gbc.gridx = 1;
        JPanel hintColorPanel = createColorSelectionPanel("Hint", hintColor,
                newColor -> {
                    hintColor = newColor;
                    saveColorToPreferences(PREF_HINT_COLOR, hintColor);
                    parent.updateGamePanelColors();
                },
                getHintColors()
        );
        add(hintColorPanel, gbc);

         // Back to menu gomb
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton backButton = new JButton("Back to Menu");
        styleBottomButton(backButton, Color.CYAN);
        add(backButton, gbc);

        backButton.addActionListener(e -> {
            SoundManager.playSound("button.wav");
            parent.showPanel(SetGame.MAIN_MENU);
        });
    }

    // gombok kinezete
    private void styleBottomButton(JButton button, Color textColor) {
        button.setUI(new BasicButtonUI());
        button.setForeground(textColor);
        button.setBackground(new Color(60, 60, 60));
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        button.setOpaque(true);
    }

    // szinvalasztas
    private JPanel createColorSelectionPanel(String type, Color currentColor,
                                             ColorChangeListener listener,
                                             Map<String, Color> colorOptions) {
        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colorPanel.setBackground(Color.BLACK);
        ButtonGroup group = new ButtonGroup();

        for (Map.Entry<String, Color> entry : colorOptions.entrySet()) {
            JRadioButton colorButton = new JRadioButton(entry.getKey());
            colorButton.setActionCommand(entry.getKey());
            colorButton.setForeground(entry.getValue());
            colorButton.setBackground(Color.BLACK);
            group.add(colorButton);
            colorPanel.add(colorButton);

            if (entry.getValue().equals(currentColor)) {
                colorButton.setSelected(true);
            }

            // ha a gombot kivalasztjuk, akkor az uj szint beallitjuk
            colorButton.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    listener.colorChanged(entry.getValue());
                }
            });
        }
        return colorPanel;
    }

    private Map<String, Color> getHighlightColors() {
        Map<String, Color> colors = new HashMap<>();
        colors.put("Yellow", Color.YELLOW);
        colors.put("Green", Color.GREEN);
        colors.put("Blue", Color.BLUE);
        colors.put("Magenta", Color.MAGENTA);
        colors.put("Orange", Color.ORANGE);
        return colors;
    }

    private Map<String, Color> getHintColors() {
        Map<String, Color> colors = new HashMap<>();
        colors.put("Cyan", Color.CYAN);
        colors.put("Pink", Color.PINK);
        colors.put("Light Gray", Color.BLACK);
        colors.put("Gray", Color.GRAY);
        colors.put("Dark Gray", Color.DARK_GRAY);
        return colors;
    }

    private Color getColorFromPreferences(String key, Color defaultColor) {
        String colorStr = prefs.get(key, ""); // hexa forma
        if (!colorStr.isEmpty()) {
            try {
                return new Color(Integer.parseInt(colorStr, 16)); // hexabol Color obj-ba
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return defaultColor;
    }

    // a valasztott szineket lementjuk hexa formaban
    //ARGB-bol RGB-be a substring(2)vel alakitunk, az a(alfa) az atlatszosag, ami nem kell
    private void saveColorToPreferences(String key, Color color) {
        prefs.put(key, Integer.toHexString(color.getRGB()).substring(2));
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public Color getHintColor() {
        return hintColor;
    }

    @FunctionalInterface
    interface ColorChangeListener {
        void colorChanged(Color newColor);
    }
}

package Pages;

import javax.swing.*;
import java.awt.*;

import static Pages.CruiseAppUtilities.*;

public class LandingPage {
    public static JPanel createLandingPagePanel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,10,10,10);
        //modify this to your path_to_logo.png if different
        ImageIcon originalIcon = new ImageIcon("logo.png");
        // Scale the image
        Image scaledImage = originalIcon.getImage().getScaledInstance(150,150, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(scaledIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns
        panel.add(logoLabel, gbc);

        //! PLACHOLDERS
        JButton cruise1Button = createStyledButton("Cruise.Cruise 1", BUTTON_FONT, BUTTON_COLOR);
        cruise1Button.addActionListener(e -> {/* Handle Cruise.Cruise 1 selection */});
        gbc.gridx = 0;
        gbc.gridy = 1;
//        gbc.gridwidth = 1;
        panel.add(cruise1Button, gbc);

        JButton cruise2Button = createStyledButton("Cruise.Cruise 2", BUTTON_FONT, BUTTON_COLOR);
        cruise2Button.addActionListener(e -> {/* Handle Cruise.Cruise 2 selection */});
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(cruise2Button, gbc);

        JButton cruise3Button = createStyledButton("Cruise.Cruise 3", BUTTON_FONT, BUTTON_COLOR);
        cruise3Button.addActionListener(e -> {/* Handle Cruise.Cruise 3 selection */});
        gbc.gridx = 0;
        gbc.gridy = 3;
//        gbc.gridwidth = 2;
        panel.add(cruise3Button, gbc);

        JButton loginButton = createStyledButton("Login", BUTTON_FONT, BUTTON_COLOR);
        loginButton.addActionListener(e -> switchToPanel(frame, "Login"));
        gbc.gridx = 0;
        gbc.gridy = 4;
//        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        return panel;

    }
}

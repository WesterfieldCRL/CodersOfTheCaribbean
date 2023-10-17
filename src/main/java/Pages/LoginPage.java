package Pages;

import javax.swing.*;
import java.awt.*;

import static Pages.CruiseAppUtilities.*;
import static Pages.CruiseAppUtilities.switchToPanel;

public class LoginPage {
    public static JPanel createLoginPanel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Changed to GridBagLayout for better positioning
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints(); // GridBagConstraints for positioning
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,10,10,10); // Padding
        //modify this to your path_to_logo.png if different
        ImageIcon originalIcon = new ImageIcon("logo.png");//assuming you have "Coders of the Caribbean" logo stored locally
        // Scale the image
        Image scaledImage = originalIcon.getImage().getScaledInstance(150,150, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(scaledIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns
        panel.add(logoLabel, gbc);

        gbc.gridwidth = 1;
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(createStyledLabel("Username:", LABEL_FONT), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(createStyledLabel("Password:", LABEL_FONT), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        JButton loginSubmitButton = createStyledButton("Login", BUTTON_FONT, BUTTON_COLOR);
        JButton createAccountButton = createStyledButton("Create Account", BUTTON_FONT, BUTTON_COLOR);

        loginSubmitButton.addActionListener(e -> handleLogin(usernameField, passwordField));
        createAccountButton.addActionListener(e -> switchToPanel(frame, "Create Account"));


        buttonsPanel.add(loginSubmitButton);
        buttonsPanel.add(createAccountButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span two columns
        panel.add(buttonsPanel, gbc);

        return panel;
    }

}

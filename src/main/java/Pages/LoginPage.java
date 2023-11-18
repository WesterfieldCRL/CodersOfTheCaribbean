package Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import static Pages.CruiseAppUtilities.*;
import static Pages.CruiseAppUtilities.switchToPanel;
import static Person.Guest.resetRequest;
import static Person.Guest.usernameExists;

public class LoginPage {
    public static JPanel createLoginPanel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,10,10,10); // Padding
        ImageIcon originalIcon = new ImageIcon("logo.png");
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

        loginSubmitButton.addActionListener(e -> handleLogin(usernameField, passwordField, frame));
        createAccountButton.addActionListener(e -> switchToPanel(frame, "Create Account"));
        JButton resetPasswordButton = createStyledButton("Reset Password", BUTTON_FONT, BUTTON_COLOR);

        resetPasswordButton.addActionListener(e -> {
            JTextField usernameFieldForReset = new JTextField(15);
            int result = JOptionPane.showConfirmDialog(frame, new Object[]{"Username:", usernameFieldForReset}, "Password Reset Request", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String username = usernameFieldForReset.getText().trim();
                if (!username.isEmpty() && usernameExists(username)) {
                    java.util.List<String> requirements = new ArrayList<>(Arrays.asList(
                            "At least 8 characters",
                            "At least one digit",
                            "At least one letter",
                            "At least one special character (!@#$%^&*+=?-)"
                    ));
                    JPasswordField newPasswordField = new JPasswordField(15);
                    JDialog passwordDialog = new JDialog(frame, "Set New Password", true);
                    passwordDialog.setLayout(new GridLayout(0, 1));
                    passwordDialog.add(new JLabel("New Password:"));
                    passwordDialog.add(newPasswordField);

                    JLabel requirementsLabel = new JLabel("<html>" + String.join("<br>", requirements) + "</html>");
                    passwordDialog.add(requirementsLabel);

                    newPasswordField.addKeyListener(new KeyAdapter() {
                        public void keyReleased(KeyEvent e) {
                            String password = new String(newPasswordField.getPassword());

                            requirements.clear();
                            if (password.length() < 8) requirements.add("At least 8 characters");
                            if (!password.matches(".*\\d.*")) requirements.add("At least one digit");
                            if (!password.matches(".*[a-zA-Z].*")) requirements.add("At least one letter");
                            if (!password.matches(".*[!@#$%^&*+=?-].*")) requirements.add("At least one special character (!@#$%^&*+=?-)");

                            requirementsLabel.setText("<html>" + String.join("<br>", requirements) + "</html>");
                            passwordDialog.pack();
                        }
                    });

                    JButton confirmButton = new JButton("Confirm");
                    confirmButton.addActionListener(ev -> {
                        String password = new String(newPasswordField.getPassword()).trim();
                        String passwordValidationMessage = validatePassword(password);
                        if (passwordValidationMessage.isEmpty()) {
                            resetRequest(username, password);
                            JOptionPane.showMessageDialog(frame, "A password reset request has been submitted.", "Request Submitted", JOptionPane.INFORMATION_MESSAGE, scaledSuccessIcon);
                            passwordDialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(frame, passwordValidationMessage, "Invalid Password", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                        }
                    });

                    passwordDialog.add(confirmButton);
                    passwordDialog.pack();
                    passwordDialog.setLocationRelativeTo(frame);
                    passwordDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Username cannot be empty or does not exist.", "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                }
            }
        });

        buttonsPanel.add(resetPasswordButton);
        buttonsPanel.add(loginSubmitButton);
        buttonsPanel.add(createAccountButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span two columns
        panel.add(buttonsPanel, gbc);

        return panel;
    }

}

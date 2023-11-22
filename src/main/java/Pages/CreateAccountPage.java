package Pages;

import Person.Guest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static Pages.CruiseAppUtilities.*;

public class CreateAccountPage {
    public static JPanel createAccountPanel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = createStyledLabel("Create New Guest Account", LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField nameField = new JTextField(15);
        JTextField addressField = new JTextField(15);
        JTextField emailField = new JTextField(15);

        String[] labels = {"Username:", "Password:", "Name:", "Address:", "Email:"};
        JTextField[] fields = {usernameField, passwordField, nameField, addressField, emailField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            panel.add(createStyledLabel(labels[i], LABEL_FONT), gbc);

            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }


        JLabel passwordRequirementsLabel = new JLabel();
        updateRequirementsLabel(requirements, passwordRequirementsLabel);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(passwordRequirementsLabel, gbc);

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String password = new String(passwordField.getPassword());
                updateRequirementsLabel(password, requirements, passwordRequirementsLabel);
                panel.revalidate();
                panel.repaint();
            }
        });

        JButton submitButton = createStyledButton("Submit", BUTTON_FONT, BUTTON_COLOR);
        submitButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            String passwordValidationMessage = validatePassword(password);
            if (!passwordValidationMessage.isEmpty()) {
                JOptionPane.showMessageDialog(frame, passwordValidationMessage, "Invalid Password", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                return;
            }
            Guest guest = new Guest(usernameField.getText(), password, nameField.getText(), addressField.getText(), emailField.getText());
            boolean success = guest.createAccount();
            if (success) {
                JOptionPane.showMessageDialog(frame, "Account created successfully!", "Create Account Status", JOptionPane.DEFAULT_OPTION, scaledSuccessIcon);
                switchToPanel(frame, "Login");
            } else {
                JOptionPane.showMessageDialog(frame, "Error creating account. Please ensure your details are correct and try again.", "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = labels.length + 2;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

        JButton backButton = createStyledButton("Back", BUTTON_FONT, BUTTON_COLOR);
        backButton.addActionListener(e -> {
            switchToPanel(frame, "Login");
            usernameField.setText("");
            passwordField.setText("");
            nameField.setText("");
            addressField.setText("");
            emailField.setText("");
        });        gbc.gridy = labels.length + 3;
        panel.add(backButton, gbc);

        return panel;
    }
}
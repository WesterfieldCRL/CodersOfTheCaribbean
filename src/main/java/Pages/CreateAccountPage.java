package Pages;

import Person.Guest;

import javax.swing.*;
import java.awt.*;

import static Pages.CruiseAppUtilities.*;

public class CreateAccountPage {
    public static JPanel createAccountPanel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        panel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,10,10,10);

        JLabel titleLabel = createStyledLabel("Create New Guest Account", LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel,gbc);

        gbc.gridwidth = 1;

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField nameField = new JTextField(15);
        JTextField addressField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField creditCardNumberField = new JTextField(15);
        JTextField creditCardExpirationDateField = new JTextField(15);

        String[] labels = {"Username:", "Password:", "Name:", "Address:", "Email:", "Credit Card Number:", "Credit Card Expiration Date:"};
        JTextField[] fields = {usernameField, passwordField, nameField, addressField, emailField, creditCardNumberField, creditCardExpirationDateField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            panel.add(createStyledLabel(labels[i], LABEL_FONT), gbc);

            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        JButton submitButton = createStyledButton("Submit", BUTTON_FONT, BUTTON_COLOR);
        submitButton.addActionListener(e -> {
            Guest guest = new Guest(usernameField.getText(), new String(passwordField.getPassword()), nameField.getText(),
                    addressField.getText(), emailField.getText());
            boolean success = guest.createAccount();
            if (success) {
                JOptionPane.showMessageDialog(null, "Account created successfully!");
                switchToPanel(frame, "Login");
            } else {
                JOptionPane.showMessageDialog(null, "Error creating account. Please ensure your details are correct and try again.");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = labels.length + 1;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

        return panel;



    }

}

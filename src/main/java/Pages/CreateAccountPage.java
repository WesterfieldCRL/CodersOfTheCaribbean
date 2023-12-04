package Pages;

import Person.Guest;
import Util.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


import static Pages.CruiseAppUtilities.*;

/**
 * The {@code CreateAccountPage} class encapsulates the user interface components for creating new guest accounts
 * in a cruise booking application.
 *
 * <p>The {@code createAccountPanel} method is the central method used to create a panel with a form for account
 * creation, including fields for username, password, name, address, and email, as well as password validation and
 * terms of service agreement. Upon successful creation of an account, the user is redirected to the login panel.</p>
 *
 * <p>Supporting utility methods like {@code clearLoginFields} are included to reset the form fields, ensuring a
 * clean slate for new account input or after successful submission. The class uses {@code CruiseAppUtilities}
 * for shared functionality across the application.</p>
 */
public class CreateAccountPage {
    static JTextField usernameField = new JTextField(15);
    static JPasswordField passwordField = new JPasswordField(15);
    static JTextField nameField = new JTextField(15);
    static JTextField addressField = new JTextField(15);
    static JTextField emailField = new JTextField(15);
    static JTextField ccNumberField = new JTextField(15);
    static JTextField ccExpirationField = new JTextField(15);

    /**
     * Constructs a {@code JPanel} that allows for the creation of guest accounts.
     *
     * <p>The panel is organized using {@code GridBagLayout} and includes the following elements:</p>
     * <ol>
     *   <li>Text fields for entering guest information such as username, password, name, address, and email.</li>
     *   <li>A label that specifies password requirements and updates dynamically based on user input.</li>
     *   <li>A 'Terms of Service' checkbox and button, with the checkbox being enabled after the ToS are viewed.</li>
     *   <li>A 'Submit' button that becomes active when the ToS checkbox is checked, processes the account creation,
     *       and informs the user of the outcome.</li>
     *   <li>A 'Back' button to navigate back to the 'Login' panel.</li>
     * </ol>
     *
     * @param frame the {@code JFrame} which serves as the parent window for dialogs and navigation between panels
     * @return a {@code JPanel} designed for creating guest accounts, complete with validation and submission functionality
     */
    public static JPanel createAccountPanel(JFrame frame) {
        AppLogger.getLogger().info("createAccountPanel");

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
        String[] labels = {
                "Username:", "Password:", "Name:", "Address:", "Email:",
                "Credit Card Number:", "Expiration Date (MM/YYYY):"
        };
        JTextField[] fields = {
                usernameField, passwordField, nameField, addressField, emailField,
                ccNumberField, ccExpirationField
        };

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
        gbc.gridy++;
//        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(passwordRequirementsLabel, gbc);

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                AppLogger.getLogger().info("In passwordField key listener");

                String password = new String(passwordField.getPassword());
                updateRequirementsLabel(password, requirements, passwordRequirementsLabel);
                panel.revalidate();
                panel.repaint();
            }
        });

        gbc.gridy++;

        JCheckBox tosCheckBox = new JCheckBox("I agree to the Terms of Service.");
        tosCheckBox.setEnabled(false);
        JButton tosButton = createStyledButton("View Terms of Service", BUTTON_FONT, BUTTON_COLOR);
        tosButton.addActionListener(e -> {
            JDialog tosDialog = TermsOfServicePanel.createTermsOfServiceDialog(frame);
            tosDialog.setVisible(true);
            tosCheckBox.setEnabled(true);
        });

        gbc.gridy++;
        panel.add(tosButton, gbc);
        gbc.gridy++;
        panel.add(tosCheckBox, gbc);

        JCheckBox isCorporateGuestCheckBox = new JCheckBox("Corporate Guest");
        gbc.gridy++;
        panel.add(isCorporateGuestCheckBox, gbc);


        JButton submitButton = createStyledButton("Submit", BUTTON_FONT, BUTTON_COLOR);
        submitButton.setEnabled(false);
        tosCheckBox.addActionListener(e -> submitButton.setEnabled(tosCheckBox.isSelected()));
        submitButton.addActionListener(e -> {
            AppLogger.getLogger().info("In submit button");

            String password = new String(passwordField.getPassword());
            String passwordValidationMessage = validatePassword(password);
            if (!passwordValidationMessage.isEmpty()) {
                JOptionPane.showMessageDialog(frame, passwordValidationMessage, "Invalid Password", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                return;
            }
            int ccLength = ccNumberField.getText().trim().length();

            if (ccLength!= 15 && ccLength != 16 ){
                JOptionPane.showMessageDialog(frame, "Invlaid Card Number. Must be 15 or 16 digits.", "Invalid Credit Card Number", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                return;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            YearMonth ccExpiration;
            try {
                AppLogger.getLogger().info("Checking ccExpirationField");
                ccExpiration = YearMonth.parse(ccExpirationField.getText(), formatter);
                YearMonth currentYearMonth = YearMonth.now();

                if (ccExpiration.isBefore(currentYearMonth)) {
                    JOptionPane.showMessageDialog(frame, "The expiration date cannot be in the past. Please enter a valid date.", "Invalid Expiration Date", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                    return;
                }
            } catch (DateTimeParseException ex) {
                AppLogger.getLogger().warning("Invalid CC Date format");
                JOptionPane.showMessageDialog(frame, "Invalid date format. Please enter the date in MM/yyyy format.", "Invalid Date", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                return;
            }
            Guest guest = new Guest(usernameField.getText(), password, nameField.getText(), addressField.getText(), emailField.getText(), ccNumberField.getText(), ccExpiration ,isCorporateGuestCheckBox.isSelected());
            boolean success = guest.createAccount();
            if (success) {
                AppLogger.getLogger().info("Success creating guest account");
                JOptionPane.showMessageDialog(frame, "Account created successfully!", "Create Account Status", JOptionPane.DEFAULT_OPTION, scaledSuccessIcon);
                switchToPanel(frame, "Login");
            } else {
                JOptionPane.showMessageDialog(frame, "Error creating account. Please ensure your details are correct and try again.", "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
            }
            clearLoginFields();
        });

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

        JButton backButton = createStyledButton("Back", BUTTON_FONT, BUTTON_COLOR);
        backButton.addActionListener(e -> {
            switchToPanel(frame, "Login");
            clearLoginFields();
        });
        gbc.gridy++;
        panel.add(backButton, gbc);

        return panel;
    }

    /**
     * Resets the content of input fields in the login and account creation forms to empty strings.
     *
     * This utility method is designed to clear the form fields after a user action such as
     * successful account registration or when navigating away from the login or account creation panels.
     * It specifically clears the following {@code JTextField} components:
     * <ol>
     *   <li>{@code usernameField} - Where the user's username is entered.</li>
     *   <li>{@code passwordField} - For entering the user's password.</li>
     *   <li>{@code nameField} - Where the user's name is provided.</li>
     *   <li>{@code addressField} - For entering the user's address.</li>
     *   <li>{@code emailField} - Where the user's email is entered.</li>
     * </ol>
     *
     * Each field is set to an empty string, effectively erasing any previous user input.
     */

    private static void clearLoginFields(){
        usernameField.setText("");
        passwordField.setText("");
        nameField.setText("");
        addressField.setText("");
        emailField.setText("");
    }
}
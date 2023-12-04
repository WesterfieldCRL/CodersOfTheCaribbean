package Pages;

import Util.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Pages.CruiseAppUtilities.*;
import static Pages.CruiseAppUtilities.switchToPanel;
import static Person.Guest.resetRequest;
import static Person.Guest.usernameExists;

/**
 * The {@code LoginPage} class in the {@code Pages} package is designed to manage the login interface.
 *
 * <p>This class includes static methods that facilitate the creation and handling of the login panel. It provides the necessary
 * UI components and logic for user authentication, account creation, and password reset functionalities.</p>
 *
 * <p>Key functionalities provided by the {@code LoginPage} class:</p>
 * <ul>
 *   <li>Creation of a login panel that includes input fields for username and password, as well as buttons for logging in, creating an account, and resetting a password.</li>
 *   <li>Implementation of action listeners for button clicks, enabling user authentication, transitioning to account creation panel, and initiating password reset processes.</li>
 *   <li>Integration of validation and user feedback mechanisms for a smooth authentication process.</li>
 * </ul>
 *
 * <p>The class collaborates with other components and utilities within the application, such as {@code CruiseAppUtilities} and person-related classes like {@code Guest}.
 * It serves as a central point for user interaction at the initial stages of using the application.</p>
 */
public class LoginPage {
    /**
     * Creates and returns a login panel.
     *
     * <p>This method constructs a {@code JPanel} specifically designed for user login. The panel is organized using a {@code GridBagLayout} to manage the layout of various components, which include:</p>
     * <ul>
     *   <li>A logo at the top of the panel for brand identity.</li>
     *   <li>Text fields for entering the user's username and password, enabling authentication input.</li>
     *   <li>A 'Login' button, which when clicked, triggers the 'handleLogin' method to authenticate the user.</li>
     *   <li>A 'Create Account' button that navigates the user to an account creation panel for new user registration.</li>
     *   <li>A 'Reset Password' button that opens a dialog for users to submit a request for password resetting.</li>
     * </ul>
     *
     * @param frame the {@code JFrame} utilized for handling actions such as switching views and displaying dialogs
     * @return a fully constructed {@code JPanel} designed for user login and account management operations
     */
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
            AppLogger.getLogger().info("Reset password button clicked");
            JTextField usernameFieldForReset = new JTextField(15);
            int result = JOptionPane.showConfirmDialog(frame, new Object[]{"Username:", usernameFieldForReset},
                    "Password Reset Request", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String username = usernameFieldForReset.getText().trim();
                if (!username.isEmpty() && usernameExists(username)) {
                    List<String> requirements = Arrays.asList(
                            "At least 8 characters",
                            "At least one digit",
                            "At least one letter",
                            "At least one special character (!@#$%^&*+=?-)"
                    );
                    JPasswordField newPasswordField = new JPasswordField(15);
                    JDialog passwordDialog = new JDialog(frame, "Set New Password", true);
                    passwordDialog.setLayout(new GridLayout(0, 1));
                    passwordDialog.add(new JLabel("New Password:"));
                    passwordDialog.add(newPasswordField);

                    JLabel requirementsLabel = new JLabel();
                    updateRequirementsLabel(requirements, requirementsLabel);
                    passwordDialog.add(requirementsLabel);

                    newPasswordField.addKeyListener(new KeyAdapter() {
                        public void keyReleased(KeyEvent e) {
                            String password = new String(newPasswordField.getPassword());
                            updateRequirementsLabel(password, new ArrayList<>(requirements), requirementsLabel);
                            passwordDialog.pack();
                        }
                    });

                    JButton confirmButton = new JButton("Confirm");
                    confirmButton.addActionListener(ev -> {
                        String password = new String(newPasswordField.getPassword()).trim();
                        String passwordValidationMessage = validatePassword(password);
                        if (passwordValidationMessage.isEmpty()) {
                            resetRequest(username, password);
                            JOptionPane.showMessageDialog(frame, "A password reset request has been submitted.",
                                    "Request Submitted", JOptionPane.INFORMATION_MESSAGE, scaledSuccessIcon);
                            passwordDialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(frame, passwordValidationMessage, "Invalid Password",
                                    JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                        }
                    });

                    passwordDialog.add(confirmButton);
                    passwordDialog.pack();
                    passwordDialog.setLocationRelativeTo(frame);
                    passwordDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Username cannot be empty or does not exist.",
                            "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
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

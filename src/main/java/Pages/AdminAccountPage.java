package Pages;

import Person.Guest;
import Person.TravelAgent;
import Util.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.Vector;

import static Pages.CruiseAppUtilities.*;

/**
 * The {@code AdminAccountPage} class provides a user interface for administrative tasks
 * related to managing guest accounts and travel agent accounts.
 * This class contains methods to create the panels for resetting passwords and creating new travel agent accounts,
 * and to update the display of guests who have requested password resets.
 *
 * <p>Methods in this class are static and provide JPanels that are added to a JTabbedPane in the main application frame.
 * The {@code createAdminTabbedPane} method serves as the entry point for generating the tabbed pane with the necessary
 * administrative panels.</p>
 *
 * <p>This class is a part of the Pages package and works closely with the {@code Guest} and {@code TravelAgent} classes
 * from the Person package to manage user-related operations.</p>
 */
public class AdminAccountPage {
    /**
     * Initializes a {@code JTabbedPane} for administrative tasks in the specified {@code JFrame}.
     *
     * <p>This method adds two tabs to the tabbed pane:</p>
     * <ol>
     *   <li>
     *     'Reset Password': Contains a panel created by {@code createResetPasswordPanel} method.
     *   </li>
     *   <li>
     *     'Create Travel Agent': Incorporates a panel created by {@code createAccountPanel} method.
     *   </li>
     * </ol>
     *
     * @param frame The {@code JFrame} to which this tabbed pane is added. This frame is used to
     *              pass context to the individual panel creation methods.
     * @return A {@code JTabbedPane} instance with tabs for 'Reset Password' and 'Create Travel Agent'.
     */
    public static JTabbedPane createAdminTabbedPane(JFrame frame) {
        AppLogger.getLogger().info("Creating admin tabbed pane");

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel resetPasswordPanel = createResetPasswordPanel(frame);
        tabbedPane.addTab("Reset Password", resetPasswordPanel);

        JPanel createAccountPanel = createAccountPanel(frame);
        tabbedPane.addTab("Create Travel Agent", createAccountPanel);

        return tabbedPane;
    }

    /**
     * Constructs a JPanel for resetting guest passwords.
     *
     * <p>The panel employs a {@code GridBagLayout} to organize its components, which include:</p>
     * <ul>
     *   <li>A {@code JList} displaying guests requesting password resets, fetched from
     *       {@code currentAdmin.getResetRequests()}.</li>
     *   <li>A 'Reset Password' button, which triggers a dialog for admin confirmation to reset the guest's password.</li>
     *   <li>A 'Logout' button to terminate the admin session and revert to the 'Login' panel.</li>
     * </ul>
     *
     * @param frame the parent {@code JFrame} context for this panel, facilitating panel switching within the application
     * @return the initialized {@code JPanel} for password reset functionality
     */
    public static JPanel createResetPasswordPanel(JFrame frame) {
        AppLogger.getLogger().info("In createResetPasswordPanel");

        JPanel resetPasswordPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        Collection<Guest> resetRequests = currentAdmin.getResetRequests();
        JList<Guest> resetList = new JList<>(new Vector<>(resetRequests));
        resetList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Guest guest = (Guest) value;
                value = guest.getUsername();
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        resetPasswordPanel.add(new JScrollPane(resetList), gbc);

        JButton resetButton = createStyledButton("Reset Password", BUTTON_FONT, BUTTON_COLOR);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        resetPasswordPanel.add(resetButton, gbc);

        JButton logoutButton = createStyledButton("Logout", BUTTON_FONT, BUTTON_COLOR);
        logoutButton.addActionListener(e -> {
            currentAdmin = null;
            switchToPanel(frame, "Login");
        });
        gbc.gridy++;
        resetPasswordPanel.add(logoutButton, gbc);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppLogger.getLogger().info("Reset button clicked");

                Guest selectedGuest = resetList.getSelectedValue();
                if (selectedGuest != null) {
                    String desiredPassword = selectedGuest.getChangedPassword();

                    JDialog resetDialog = new JDialog();
                    resetDialog.setLayout(new GridLayout(2, 2));
                    resetDialog.add(new JLabel("Desired New Password:"));
                    JLabel newPasswordLabel = new JLabel(desiredPassword);
                    resetDialog.add(newPasswordLabel);

                    JButton changeButton = new JButton("Confirm Change");
                    changeButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            AppLogger.getLogger().info("Confirm change clicked");
                            selectedGuest.resetPassword();
                            JOptionPane.showMessageDialog(resetDialog, "Password has been reset successfully.",
                                    "Password Reset", JOptionPane.INFORMATION_MESSAGE, scaledSuccessIcon);
                            resetDialog.dispose();
                            refreshResetRequestsList(resetList);
                        }
                    });

                    resetDialog.add(new JLabel());
                    resetDialog.add(changeButton);

                    resetDialog.pack();
                    resetDialog.setLocationRelativeTo(null);
                    resetDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(resetPasswordPanel, "Please select a guest from the list.",
                            "No Guest Selected", JOptionPane.WARNING_MESSAGE, scaledErrorImage);
                }
            }
        });

        return resetPasswordPanel;
    }

    /**
     * Constructs a JPanel for creating new travel agent accounts.
     *
     * <p>The panel is set with a {@code GridBagLayout} and includes several components for inputting new account details:</p>
     * <ol>
     *   <li>Text fields for the username, password, name, address, and email of the new travel agent.</li>
     *   <li>A dynamic label that displays password requirements and updates as the password input changes.</li>
     *   <li>A 'Create Account' button for submitting the new account details.</li>
     * </ol>
     *
     * <p>When the 'Create Account' button is pressed, the entered password is validated against predefined criteria
     * such as minimum length and character requirements. If validation passes, the {@code TravelAgent.createAccount}
     * method is invoked to persist the new account. The user is then notified of the success or failure of the account creation.</p>
     *
     * @param frame the {@code JFrame} that may be used by the button's action listeners to switch views or provide context for dialogs
     * @return a {@code JPanel} that facilitates the creation of new travel agent accounts
     */
    public static JPanel createAccountPanel (JFrame frame){
        AppLogger.getLogger().info("In createAccountPanel");
        JPanel createAccountPanel = new JPanel(new GridBagLayout());
        createAccountPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        createAccountPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.LINE_START;

        JLabel titleLabel = createStyledLabel("Create New Travel Agent Account", LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        createAccountPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField nameField = new JTextField(15);
        JTextField addressField = new JTextField(15);
        JTextField emailField = new JTextField(15);

        String[] labels = {"Username:", "Password:", "Name:", "Address:", "Email:"};
        JTextField[] fields = {usernameField, passwordField, nameField, addressField, emailField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            createAccountPanel.add(createStyledLabel(labels[i], LABEL_FONT), gbc);

            gbc.gridx = 1;
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, fields[i].getPreferredSize().height));
            createAccountPanel.add(fields[i], gbc);
            gbc.gridy++;
        }

        JLabel passwordRequirementsLabel = new JLabel("<html>Requirements:<br>" +
                "- At least 8 characters<br>" +
                "- At least one digit<br>" +
                "- At least one letter<br>" +
                "- At least one special character (!@#$%^&*+=?-)</html>");
        passwordRequirementsLabel.setFont(DEFAULT_FONT);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        createAccountPanel.add(passwordRequirementsLabel, gbc);

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                AppLogger.getLogger().info("In password Field");

                String password = new String(passwordField.getPassword());
                updateRequirementsLabel(password, requirements, passwordRequirementsLabel);
                createAccountPanel.revalidate();
                createAccountPanel.repaint();
            }
        });

        gbc.gridy++;

        JButton createButton = createStyledButton("Create Account", BUTTON_FONT, BUTTON_COLOR);

        gbc.gridx = 0;
        gbc.gridy = labels.length + 2;
        gbc.gridwidth = 2;
        createAccountPanel.add(createButton, gbc);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AppLogger.getLogger().info("In createButton");

                String password = new String(passwordField.getPassword());
                String validationMessage = validatePassword(password);
                if (!validationMessage.isEmpty()) {
                    JOptionPane.showMessageDialog(createAccountPanel, validationMessage, "Invalid Password", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                    return;
                }

                String username = usernameField.getText();
                String name = nameField.getText();
                String address = addressField.getText();
                String email = emailField.getText();

                TravelAgent newAgent = new TravelAgent(username, password, name, address, email);

                if (newAgent.createAccount()) {
                    JOptionPane.showMessageDialog(createAccountPanel, "Travel Agent account created successfully!", "Account Creation", JOptionPane.INFORMATION_MESSAGE, scaledSuccessIcon);
                    usernameField.setText("");
                    passwordField.setText("");
                    nameField.setText("");
                    addressField.setText("");
                    emailField.setText("");
                } else {
                    JOptionPane.showMessageDialog(createAccountPanel, "Failed to create Travel Agent account. Please check the details and try again.", "Account Creation Failed", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                }
                AppLogger.getLogger().info("Exiting create account button");

            }
        });

        return createAccountPanel;
    }

    /**
     * Updates the JList with the current list of guests requesting password resets.
     *
     * <p>This method retrieves the latest list of password reset requests from {@code currentAdmin.getResetRequests()},
     * which returns a collection of {@code Guest} objects. It clears the existing list and repopulates it with the
     * updated collection. This ensures that the displayed list reflects the current state of reset requests.</p>
     *
     * @param resetList the {@code JList<Guest>} to be updated with the latest password reset requests
     */

    private static void refreshResetRequestsList(JList<Guest> resetList) {
        Collection<Guest> resetRequests = currentAdmin.getResetRequests();
        resetList.setListData(new Vector<>(resetRequests));
    }
}

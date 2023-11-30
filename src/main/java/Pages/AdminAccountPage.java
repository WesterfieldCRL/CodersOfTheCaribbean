package Pages;

import Person.Guest;
import Person.TravelAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.Vector;

import static Pages.CruiseAppUtilities.*;

public class AdminAccountPage {
    public static JTabbedPane createAdminTabbedPane(JFrame frame) {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel resetPasswordPanel = createResetPasswordPanel(frame);
        tabbedPane.addTab("Reset Password", resetPasswordPanel);

        JPanel createAccountPanel = createAccountPanel(frame);
        tabbedPane.addTab("Create Travel Agent", createAccountPanel);

        return tabbedPane;
    }

    public static JPanel createResetPasswordPanel(JFrame frame) {
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
                            selectedGuest.resetPassword();
                            JOptionPane.showMessageDialog(resetDialog, "Password has been reset successfully.",
                                    "Password Reset", JOptionPane.INFORMATION_MESSAGE);
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
                            "No Guest Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        return resetPasswordPanel;
    }

    public static JPanel createAccountPanel (JFrame frame){
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
                String password = new String(passwordField.getPassword());
                String validationMessage = validatePassword(password);
                if (!validationMessage.isEmpty()) {
                    JOptionPane.showMessageDialog(createAccountPanel, validationMessage, "Invalid Password", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String username = usernameField.getText();
                String name = nameField.getText();
                String address = addressField.getText();
                String email = emailField.getText();

                TravelAgent newAgent = new TravelAgent(username, password, name, address, email);

                if (newAgent.createAccount()) {
                    JOptionPane.showMessageDialog(createAccountPanel, "Travel Agent account created successfully!", "Account Creation", JOptionPane.INFORMATION_MESSAGE);
                    usernameField.setText("");
                    passwordField.setText("");
                    nameField.setText("");
                    addressField.setText("");
                    emailField.setText("");
                } else {
                    JOptionPane.showMessageDialog(createAccountPanel, "Failed to create Travel Agent account. Please check the details and try again.", "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return createAccountPanel;
    }

    private static void refreshResetRequestsList(JList<Guest> resetList) {
        Collection<Guest> resetRequests = currentAdmin.getResetRequests();
        resetList.setListData(new Vector<>(resetRequests));
    }
}

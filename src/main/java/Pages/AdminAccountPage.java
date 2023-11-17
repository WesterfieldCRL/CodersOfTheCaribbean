package Pages;

import Person.Admin;
import Person.Guest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.Vector;

import static Pages.CruiseAppUtilities.*;

public class AdminAccountPage {
    public static JTabbedPane createAdminTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel resetPasswordPanel = new JPanel(new BorderLayout());
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

        resetPasswordPanel.add(new JScrollPane(resetList), BorderLayout.CENTER);
        JButton resetButton = CruiseAppUtilities.createStyledButton("Reset Password", CruiseAppUtilities.BUTTON_FONT, CruiseAppUtilities.BUTTON_COLOR);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Guest selectedGuest = resetList.getSelectedValue();
                if (selectedGuest != null) {
                    JDialog resetDialog = new JDialog();
                    resetDialog.setLayout(new GridLayout(3, 2));
                    resetDialog.add(new JLabel("New Password:"));
                    JPasswordField newPasswordField = new JPasswordField();
                    resetDialog.add(newPasswordField);
                    resetDialog.add(new JLabel("Confirm Password:"));
                    JPasswordField confirmPasswordField = new JPasswordField();
                    resetDialog.add(confirmPasswordField);

                    JButton confirmButton = new JButton("Confirm");
                    confirmButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String newPassword = new String(newPasswordField.getPassword());
                            String confirmPassword = new String(confirmPasswordField.getPassword());
                            if(newPassword.equals(confirmPassword) && !newPassword.isEmpty()) {
                                selectedGuest.resetPassword();
                                JOptionPane.showMessageDialog(resetDialog, "Password has been reset successfully.", "Password Reset", JOptionPane.INFORMATION_MESSAGE);
                                resetDialog.dispose();
                            } else {
                                JOptionPane.showMessageDialog(resetDialog, "Passwords do not match or are empty.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    resetDialog.add(new JLabel());
                    resetDialog.add(confirmButton);

                    resetDialog.pack();
                    resetDialog.setLocationRelativeTo(null);
                    resetDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(tabbedPane, "Please select a guest from the list.", "No Guest Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });



        resetPasswordPanel.add(resetButton, BorderLayout.SOUTH);

        JPanel createAccountPanel = new JPanel();
        createAccountPanel.setLayout(new BoxLayout(createAccountPanel, BoxLayout.Y_AXIS));
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField nameField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JButton createButton = CruiseAppUtilities.createStyledButton("Create Account", CruiseAppUtilities.BUTTON_FONT, CruiseAppUtilities.BUTTON_COLOR);

        createAccountPanel.add(CruiseAppUtilities.createStyledLabel("Username: ", CruiseAppUtilities.LABEL_FONT));
        createAccountPanel.add(usernameField);
        createAccountPanel.add(CruiseAppUtilities.createStyledLabel("Password: ", CruiseAppUtilities.LABEL_FONT));
        createAccountPanel.add(passwordField);
        createAccountPanel.add(CruiseAppUtilities.createStyledLabel("Name: ", CruiseAppUtilities.LABEL_FONT));
        createAccountPanel.add(nameField);
        createAccountPanel.add(CruiseAppUtilities.createStyledLabel("Address: ", CruiseAppUtilities.LABEL_FONT));
        createAccountPanel.add(addressField);
        createAccountPanel.add(CruiseAppUtilities.createStyledLabel("Email: ", CruiseAppUtilities.LABEL_FONT));
        createAccountPanel.add(emailField);
        createAccountPanel.add(createButton);

        tabbedPane.addTab("Reset Password", resetPasswordPanel);
        tabbedPane.addTab("Create Travel Agent", createAccountPanel);

        return tabbedPane;
    }
}

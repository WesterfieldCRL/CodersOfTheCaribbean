package Pages;

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
        JButton resetButton = createStyledButton("Reset Password", BUTTON_FONT, BUTTON_COLOR);
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
                    JOptionPane.showMessageDialog(tabbedPane, "Please select a guest from the list.",
                            "No Guest Selected", JOptionPane.WARNING_MESSAGE);
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
        JButton createButton = createStyledButton("Create Account", BUTTON_FONT, BUTTON_COLOR);

        createAccountPanel.add(createStyledLabel("Username: ", LABEL_FONT));
        createAccountPanel.add(usernameField);
        createAccountPanel.add(createStyledLabel("Password: ", LABEL_FONT));
        createAccountPanel.add(passwordField);
        createAccountPanel.add(createStyledLabel("Name: ", LABEL_FONT));
        createAccountPanel.add(nameField);
        createAccountPanel.add(createStyledLabel("Address: ", LABEL_FONT));
        createAccountPanel.add(addressField);
        createAccountPanel.add(createStyledLabel("Email: ", LABEL_FONT));
        createAccountPanel.add(emailField);
        createAccountPanel.add(createButton);

        tabbedPane.addTab("Reset Password", resetPasswordPanel);
        tabbedPane.addTab("Create Travel Agent", createAccountPanel);

        return tabbedPane;
    }

    private static void refreshResetRequestsList(JList<Guest> resetList) {
        Collection<Guest> resetRequests = currentAdmin.getResetRequests();
        resetList.setListData(new Vector<>(resetRequests));
    }
}

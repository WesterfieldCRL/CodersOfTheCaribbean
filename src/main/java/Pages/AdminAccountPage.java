package Pages;

import Person.Admin;
import Person.Guest;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Vector;

import static Pages.CruiseAppUtilities.*;

public class AdminAccountPage {
    public static JTabbedPane createAdminTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel resetPasswordPanel = new JPanel(new BorderLayout());
        Collection<Guest> resetRequests = currentAdmin.getResetRequests();
        JList<Guest> resetList = new JList<>(new Vector<>(resetRequests));
        resetPasswordPanel.add(new JScrollPane(resetList), BorderLayout.CENTER);
        JButton resetButton = CruiseAppUtilities.createStyledButton("Reset Password", CruiseAppUtilities.BUTTON_FONT, CruiseAppUtilities.BUTTON_COLOR);
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

package Pages;

import Billing.Expenses;
import Util.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static Pages.CruiseAppUtilities.*;

/**
 * This {@code ManagerAccountPage} in this {@code Pages} package provides the manager view
 *
 * The {@code GuestAccountPage} class provides a graphical user interface for managers to interact with various
 * functionalities of the cruise system. This class includes methods for creating and managing
 * different panels and tabs tailored for managerial tasks, such as room selection, viewing current reservations,
 * modifying reservations, generating billing reports, and handling logout functionality.
 *
 * Key functionalities provided by this class include:
 * <ul>
 *   <li>Creating a tabbed pane for the manager view, with specialized panels for room selection, current reservations, and billing reports.</li>
 *   <li>Enabling managers to generate and view billing reports, and to report errors associated with specific bills.</li>
 *   <li>Allowing managers to log out of the system and return to the login view.</li>
 *   <li>Updating room, reservation, and billing report lists dynamically based on manager interactions and system changes.</li>
 * </ul>
 */
public class ManagerAccountPage {
    /**
     * Creates and returns a JPanel for the manager's view in the application.
     *
     * This panel includes functionality for generating billing reports, logging out, and reporting errors. It is laid out using
     * GridBagLayout for flexible arrangement of components. The panel includes a scrollable list to display reports and
     * buttons for various management actions. Each button is equipped with an ActionListener to define its behavior upon being clicked.
     *
     * @param frame The JFrame in which the panel will be displayed. It is used for context in action listeners.
     * @return A JPanel containing the manager view interface, with components for report management and system interaction.
     */
    public static JPanel createManagerViewPanel (JFrame frame){
        AppLogger.getLogger().info("Manager account page");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton generateReportButton = new JButton("Generate Billing Report");
        JButton logoutButton = new JButton("Logout");
        JButton reportButton = new JButton("Notify of Error");

        DefaultListModel<String> reportListModel = new DefaultListModel<>();
        JList<String> reportList = new JList<>(reportListModel);
        JScrollPane scrollPane = new JScrollPane(reportList);
        scrollPane.setPreferredSize(new Dimension(500, 700));

        logoutButton.addActionListener(e -> {
            AppLogger.getLogger().info("Logout button clicked from manager");
            currentManager = null;
            switchToPanel(frame, "Login");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(scrollPane, gbc);

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonsGbc = new GridBagConstraints();
        buttonsGbc.insets = new Insets(5, 5, 5, 5);
        buttonsGbc.weightx = 1;

        buttonsGbc.gridx = 0;
        buttonsGbc.gridy = 0;
        buttonsGbc.anchor = GridBagConstraints.LINE_END;
        buttonsPanel.add(reportButton, buttonsGbc);

        buttonsGbc.gridx = 1;
        buttonsGbc.anchor = GridBagConstraints.CENTER;
        buttonsPanel.add(generateReportButton, buttonsGbc);

        buttonsGbc.gridx = 2;
        buttonsGbc.anchor = GridBagConstraints.LINE_START;
        buttonsPanel.add(logoutButton, buttonsGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_END;
        panel.add(buttonsPanel, gbc);

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshExpensesList(reportListModel);
            }
        });

        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppLogger.getLogger().info("Report button clicked");
                String selectedValue = reportList.getSelectedValue();
                if (selectedValue == null || selectedValue.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please select a bill from the list.", "No Bill Selected", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                    return;
                }
                int billId = extractBillId(selectedValue);

                String errorDescription = JOptionPane.showInputDialog(frame, "Enter error description for Bill ID: " + billId);

                if (errorDescription != null && !errorDescription.trim().isEmpty()) {
                    Expenses expenses = new Expenses();
                    boolean success = expenses.updateError(errorDescription, billId);
                    if (success) {
                        JOptionPane.showMessageDialog(frame, "Error description updated successfully for Bill ID: " + billId, "Update Successful", JOptionPane.PLAIN_MESSAGE, scaledSuccessIcon);
                        refreshExpensesList(reportListModel);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to update error description for Bill ID: " + billId, "Update Failed", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                    }
                }
            }

            private int extractBillId(String billInfo) {
                try {
                    AppLogger.getLogger().info("Exracting bill iD");
                    String[] parts = billInfo.split(",");
                    String idPart = parts[0];
                    return Integer.parseInt(idPart.split(":")[1].trim());
                } catch (Exception ex) {
                    AppLogger.getLogger().warning("Failed to extract bill");
                    JOptionPane.showMessageDialog(frame, "Failed to extract Bill ID from the selected item.", "Error", JOptionPane.ERROR_MESSAGE,scaledErrorImage);
                    return -1;
                }
            }
        });

        return panel;
    }

    /**
     * Refreshes and updates the expenses list displayed in the manager's view.
     *
     * This method clears the existing reports from the provided list model and repopulates it with the latest expense data.
     * It retrieves a list of expenses, iterates through them, and formats each expense into a string detailing its ID,
     * the name it is billed to, the date, the amount, and any error descriptions if present. These formatted strings
     * are then added to the report list model. If there are no expenses, a message indicating the absence of billing
     * reports is added to the list model.
     *
     * @param reportListModel The DefaultListModel used to display the expenses in the UI. This model is modified to reflect
     *                        the current state of expenses.
     */
    private static void refreshExpensesList(DefaultListModel<String> reportListModel) {
        reportListModel.clear();
        Expenses expenses = new Expenses();
        List<Expenses> expenseList = expenses.getExpenses();

        for (Expenses expense : expenseList) {
            String report = "ID: " + expense.getId() + ", Bill for: " + expense.getName() +
                    ", Date: " + expense.getDate().toString() +
                    ", Amount: $" + String.format("%.2f", expense.getPrice());
            if (!expense.getErrorDescription().isEmpty()) {
                report += ", Error: " + expense.getErrorDescription();
            }
            reportListModel.addElement(report);
        }

        if (expenseList.isEmpty()) {
            reportListModel.addElement("No billing reports available.");
        }
    }
}
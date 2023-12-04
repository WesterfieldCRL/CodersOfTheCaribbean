package Pages;

import Billing.Expenses;
import Util.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static Pages.CruiseAppUtilities.*;

public class ManagerAccountPage {

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
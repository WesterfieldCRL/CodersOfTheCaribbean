package Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerAccountPage {

    //! Placeholder Styling, Change once billing is done
    public static JPanel createManagerViewPanel (JFrame frame){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton generateReportButton = new JButton("Generate Billing Report");
        DefaultListModel<String> reportListModel = new DefaultListModel<>();
        JList<String> reportList = new JList<>(reportListModel);
        JScrollPane scrollPane = new JScrollPane(reportList);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.PAGE_START;
        panel.add(generateReportButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reportListModel.addElement("Billing Report 1");
                reportListModel.addElement("Billing Report 2");
                reportListModel.addElement("Billing Report 3");
            }
        });

        return panel;
    }
}

package Pages;

import javax.swing.*;
import java.awt.*;

import static Pages.CruiseAppUtilities.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class GuestAccountPage {
    public static JPanel createGuestViewPanel(JFrame frame) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        // Create table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Image");
        model.addColumn("Cruise Name");
        model.addColumn("Duration");
        model.addColumn("Ship Name");
        model.addColumn("Price");
        //fix so the images actually show

        ImageIcon cruise1Image = new ImageIcon("Cruise1.png");
        Image scaledImage = cruise1Image.getImage().getScaledInstance(90,90,Image.SCALE_SMOOTH);

        ImageIcon cruise1Icon = new ImageIcon(scaledImage);

        model.addRow(new Object[]{cruise1Icon, "Name", "Nights", "Ship", "$42242424"});
        model.addRow(new Object[]{new ImageIcon("Cruise2.png"), "Name", "Nights", "Ship", "$424242"});

        // Create table and customize
        JTable cruiseTable = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) {
                    return ImageIcon.class;
                } else {
                    return Object.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Table cells are not editable
            }
        };

        cruiseTable.setFont(LABEL_FONT);
        cruiseTable.setRowHeight(150);
        cruiseTable.setBackground(Color.WHITE);
        cruiseTable.setGridColor(BACKGROUND_COLOR);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cruiseTable.getColumnCount(); i++) {
            cruiseTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(cruiseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
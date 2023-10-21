package Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import Cruise.Cruise;
import Cruise.Room;

import static Pages.CruiseAppUtilities.*;
import static Cruise.Cruise.*;

public class GuestAccountPage {

    private static final String CRUISE_FILE = "cruiseList.txt";

    public static JPanel createGuestViewPanel(JFrame frame) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        // Drop downs and date fields
        JComboBox<Room.Quality> qualityComboBox = new JComboBox<>(Room.Quality.values());
        JComboBox<Integer> numBedsComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});// Possibly Adjust
        JComboBox<Room.BedType> bedTypeComboBox = new JComboBox<>(Room.BedType.values());
        JCheckBox isSmokingCheckBox = new JCheckBox("Smoking?");
        JFormattedTextField startDateField = new JFormattedTextField(new SimpleDateFormat("MM/dd/yyyy"));
        JFormattedTextField endDateField = new JFormattedTextField(new SimpleDateFormat("MM/dd/yyyy"));

        Dimension dateFieldSize = new Dimension(100, 24);

        startDateField.setPreferredSize(dateFieldSize);
        endDateField.setPreferredSize(dateFieldSize);

        // Organize above components in a panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Quality:"));
        filterPanel.add(qualityComboBox);
        filterPanel.add(new JLabel("Num Beds:"));
        filterPanel.add(numBedsComboBox);
        filterPanel.add(new JLabel("Bed Type:"));
        filterPanel.add(bedTypeComboBox);
        filterPanel.add(isSmokingCheckBox);
        filterPanel.add(new JLabel("Start Date:"));
        filterPanel.add(startDateField);
        filterPanel.add(new JLabel("End Date:"));
        filterPanel.add(endDateField);

        // Buttons for cruise search
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnCruise1 = new JButton("Cruise1");
        JButton btnCruise2 = new JButton("Cruise2");
        JButton btnCruise3 = new JButton("Cruise3");


        // Model and JList
        DefaultListModel<String> roomListModel = new DefaultListModel<>();
        JList<String> roomList = new JList<>(roomListModel);
        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(roomList);

        btnCruise1.addActionListener((ActionEvent e) -> {
            updateRoomList("cruise1", qualityComboBox, numBedsComboBox, bedTypeComboBox, isSmokingCheckBox, startDateField, endDateField, roomListModel);
        });

        btnCruise2.addActionListener((ActionEvent e) -> {
            updateRoomList("cruise2", qualityComboBox, numBedsComboBox, bedTypeComboBox, isSmokingCheckBox, startDateField, endDateField, roomListModel);
        });

        btnCruise3.addActionListener((ActionEvent e) -> {
            updateRoomList("cruise3", qualityComboBox, numBedsComboBox, bedTypeComboBox, isSmokingCheckBox, startDateField, endDateField, roomListModel);
        });

        buttonPanel.add(btnCruise1);
        buttonPanel.add(btnCruise2);
        buttonPanel.add(btnCruise3);

        Dimension buttonPanelDimensions = new Dimension(100, 100);
        buttonPanel.setPreferredSize(buttonPanelDimensions);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(filterPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);


        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER); // Put the list in the center so it gets more space

        return panel;
    }

    private static void updateRoomList(String cruiseName, JComboBox<Room.Quality> qualityComboBox, JComboBox<Integer> numBedsComboBox, JComboBox<Room.BedType> bedTypeComboBox,
                                       JCheckBox isSmokingCheckBox, JFormattedTextField startDateField, JFormattedTextField endDateField, DefaultListModel<String> roomListModel) {
        Optional<Cruise> cruise = getCruise(cruiseName);
        if (cruise.isPresent()) {
            Optional<Room> room = cruise.get().isRoomAvailable(
                    (Room.Quality) qualityComboBox.getSelectedItem(),
                    (Integer) numBedsComboBox.getSelectedItem(),
                    (Room.BedType) bedTypeComboBox.getSelectedItem(),
                    isSmokingCheckBox.isSelected(),
                    (Date) startDateField.getValue(),
                    (Date) endDateField.getValue()
            );

            roomListModel.clear();
            room.ifPresent(r -> roomListModel.addElement("Room ID: " + r.getID()));  // Adjust later
        }
    }
}

package Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.time.*;
import java.util.Date;
import java.util.Optional;

import Cruise.Cruise;
import Cruise.Room;

import static Pages.CruiseAppUtilities.*;
import static Cruise.Cruise.*;

public class GuestAccountPage {

    private static Room NO_ROOMS_FOUND = new Room();
    public static Cruise currentCruise;

    public static JPanel createGuestViewPanel(JFrame frame) {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        // Drop downs and date fields
        JComboBox<Room.Quality> qualityComboBox = new JComboBox<>(Room.Quality.values());
        JComboBox<Integer> numBedsComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});
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

        for (Component comp : filterPanel.getComponents()) {
            comp.setFont(DEFAULT_FONT);
        }

        // Buttons for cruise search
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnCruise1 = createStyledButton("Cruise1", DEFAULT_FONT, BUTTON_COLOR);
        JButton btnCruise2 = createStyledButton("Cruise2", DEFAULT_FONT, BUTTON_COLOR);
        JButton btnCruise3 = createStyledButton("Cruise3", DEFAULT_FONT, BUTTON_COLOR);


        DefaultListModel<Room> roomListModel = new DefaultListModel<>();
        JList<Room> roomList = new JList<>(roomListModel);

        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setEnabled(true);
        JScrollPane scrollPane = new JScrollPane(roomList);
        roomList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                // Set the default background and foreground colors
                label.setBackground(BACKGROUND_COLOR);
                label.setForeground(Color.BLACK);
                label.setOpaque(true);

                if (isSelected) {
                    label.setBackground(Color.BLUE);
                    label.setForeground(Color.WHITE);
                }

                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                if (value == NO_ROOMS_FOUND) {
                    label.setText("No Rooms Found");
                } else if (value instanceof Room) {
                    Room room = (Room) value;
                    label.setText("Room Quality: " + room.getQuality()
                            + " Number of Beds: " + room.getNumBeds()
                            + " Type of Beds: " + room.getBedType()
                            + " Smoking Status: " + room.isSmoking());
                }

                return label;
            }
        });

        //i know its repetitive ill fix later
        btnCruise1.addActionListener((ActionEvent e) -> {
            updateAllRoomsForCruise("cruise1", roomListModel);
            Optional<Cruise> optionalCruise = getCruise("cruise1");
            if (optionalCruise.isPresent()) {
                currentCruise = optionalCruise.get();
            }
        });

        btnCruise2.addActionListener((ActionEvent e) -> {
            updateAllRoomsForCruise("cruise2", roomListModel);
            Optional<Cruise> optionalCruise = getCruise("cruise2");
            if (optionalCruise.isPresent()) {
                currentCruise = optionalCruise.get();
            }
        });

        btnCruise3.addActionListener((ActionEvent e) -> {
            updateAllRoomsForCruise("cruise3", roomListModel);
            Optional<Cruise> optionalCruise = getCruise("cruise3");
            if (optionalCruise.isPresent()) {
                currentCruise = optionalCruise.get();
            }
        });

        JButton applyFiltersButton = createStyledButton("Apply Filters", DEFAULT_FONT, BUTTON_COLOR);
        applyFiltersButton.addActionListener((ActionEvent e) -> {
            updateRoomList(currentCruise.getName(), qualityComboBox, numBedsComboBox, bedTypeComboBox,
                    isSmokingCheckBox, startDateField, endDateField, roomListModel);
        });
        filterPanel.add(applyFiltersButton);

        roomList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // double click
                    Room selectedRoom = roomList.getSelectedValue();
                    if (selectedRoom != null) {
                        openReservationDetailPanel(selectedRoom, startDateField, endDateField, currentCruise);
                    }
                }
            }
        });

        JLabel displayInstructions = new JLabel("Display rooms for:");
        buttonPanel.add(displayInstructions);


        buttonPanel.add(btnCruise1);
        buttonPanel.add(btnCruise2);
        buttonPanel.add(btnCruise3);


        Dimension buttonPanelDimensions = new Dimension(100, 100);
        buttonPanel.setPreferredSize(buttonPanelDimensions);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 240));
        JLabel usageInstructions = new JLabel("First Select a Cruise to Display. Then use the filters to select " +
                "your choice of room. YOU MUST SELECT A VALID START AND END DATE. Reserve by double click");
        usageInstructions.setFont(new Font("Arial", Font.BOLD, 12));
        usageInstructions.setForeground(new Color(50, 50, 50));
        usageInstructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(usageInstructions, BorderLayout.AFTER_LAST_LINE);

        topPanel.add(buttonPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(usageInstructions, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER); // Put the list in the center so it gets more space

        return panel;
    }

    private static void updateAllRoomsForCruise(String cruiseName, DefaultListModel<Room> roomListModel) {
        Optional<Cruise> cruise = getCruise(cruiseName);
        if (cruise.isPresent()) {
            ArrayList<Room> rooms = cruise.get().getRoomList();
            roomListModel.clear();
            for (Room room : rooms) {
                roomListModel.addElement(room);
            }
        }
    }


    private static void updateRoomList(String cruiseName, JComboBox<Room.Quality> qualityComboBox,
                                       JComboBox<Integer> numBedsComboBox, JComboBox<Room.BedType> bedTypeComboBox,
                                       JCheckBox isSmokingCheckBox, JFormattedTextField startDateField,
                                       JFormattedTextField endDateField, DefaultListModel<Room> roomListModel) {
        Optional<Cruise> cruise = getCruise(cruiseName);
        if (cruise.isPresent()) {
            Optional<Room> room = cruise.get().isRoomAvailable(
                    (Room.Quality) qualityComboBox.getSelectedItem(),
                    (Integer) numBedsComboBox.getSelectedItem(),
                    (Room.BedType) bedTypeComboBox.getSelectedItem(),
                    isSmokingCheckBox.isSelected(),
                    ((Date) startDateField.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
                    ((Date) endDateField.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            );

            roomListModel.clear();
            room.ifPresent(roomListModel::addElement); // Directly add the Room object
            if (roomListModel.isEmpty()){
                roomListModel.addElement(NO_ROOMS_FOUND);
            }
        }
    }

    private static void openReservationDetailPanel(Room room, JFormattedTextField startDateField,
                                                   JFormattedTextField endDateField, Cruise cruise) {
        JDialog reservationDialog = new JDialog();
        reservationDialog.setTitle("Reservation Details");
        reservationDialog.setModal(true);
        reservationDialog.setSize(400, 300);

        LocalDate start = ((Date) startDateField.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate end = ((Date) endDateField.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        JPanel panel = new JPanel(new BorderLayout());

        JTextArea detailArea = new JTextArea();
        detailArea.setEditable(false);
        detailArea.setText("Room Details:\n" + "Start Date: " + startDateField.getText()
                + "\n" + "End Date: " + endDateField.getText() + "\n" + "Room Quality: " + room.getQuality()
                + "\n" + "Number of Beds: " + room.getNumBeds());
        //Reservation reservation = new Reservation(currentGuest, cruise, room, start, end);
        //double totalReservationCost = reservation.getTotalCost();
        detailArea.append("\nTotal Cost: " + room.getTotalCost(start, end));

        JScrollPane scrollPane = new JScrollPane(detailArea);

        JButton makeReservationButton = new JButton("Make Reservation");
        makeReservationButton.addActionListener(e -> {
            boolean success = currentGuest.makeReservation(room, start, end, cruise);

            if (success) {
                JOptionPane.showMessageDialog(reservationDialog, "Reservation made successfully!",
                        "Reservation Status", JOptionPane.DEFAULT_OPTION, scaledSuccessIcon);
            } else {
                JOptionPane.showMessageDialog(reservationDialog, "Failed to make a reservation.", "Error",
                        JOptionPane.ERROR_MESSAGE, scaledErrorImage);
            }

            reservationDialog.dispose();
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(makeReservationButton, BorderLayout.SOUTH);

        reservationDialog.add(panel);
        reservationDialog.setVisible(true);
    }

}

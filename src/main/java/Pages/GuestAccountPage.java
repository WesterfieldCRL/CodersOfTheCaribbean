package Pages;

import javax.management.modelmbean.ModelMBean;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.time.*;
import java.util.Date;
import java.util.Optional;
import Person.Guest.*;

import Cruise.Cruise;
import Cruise.Room;

import static Pages.CruiseAppUtilities.*;
import static Cruise.Cruise.*;

public class GuestAccountPage {

    private static Room NO_ROOMS_FOUND = new Room();
    public static Cruise currentCruise;
    protected static DefaultListModel<Reservation> reservationListModel = new DefaultListModel<>();
    protected static JList<Reservation> reservationsList = new JList<>(reservationListModel);


    public static JTabbedPane createGuestViewTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel roomSelectionPanel = createRoomSelectionPanel();
        tabbedPane.addTab("Select Room", null, roomSelectionPanel, "Select a room for reservation");

        JPanel currentReservationsPanel = createCurrentReservationsPanel(reservationListModel, reservationsList);
        tabbedPane.addTab("Current Reservations", null, currentReservationsPanel, "View your current reservations");

        return tabbedPane;
    }


    public static JPanel createRoomSelectionPanel() {


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
//        filterPanel.setVisible(false);
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

        JTabbedPane cruiseTabs = new JTabbedPane();
        JPanel panelCruise1 = new JPanel();
        JPanel panelCruise2 = new JPanel();
        JPanel panelCruise3 = new JPanel();

        cruiseTabs.addTab("Cruise1", panelCruise1);
        cruiseTabs.addTab("Cruise2", panelCruise2);
        cruiseTabs.addTab("Cruise3", panelCruise3);



        DefaultListModel<Room> roomListModel = new DefaultListModel<>();
        JList<Room> roomList = new JList<>(roomListModel);

        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setEnabled(true);
        JScrollPane scrollPane = new JScrollPane(roomList);
        roomList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

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
                    label.putClientProperty("isRoom", false);
                } else if (value instanceof Room) {
                    Room room = (Room) value;
                    label.setText("Room Quality: " + room.getQuality()
                            + " Number of Beds: " + room.getNumBeds()
                            + " Type of Beds: " + room.getBedType()
                            + " Smoking Status: " + room.isSmoking());
                    label.putClientProperty("isRoom", true);
                }

                return label;
            }
        });

        cruiseTabs.addChangeListener(e -> {
            JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
            int index = sourceTabbedPane.getSelectedIndex();

            switch(index) {
                case 0:
                    updateAllRoomsForCruise("cruise1", roomListModel);
                    Optional<Cruise> optionalCruise = getCruise("cruise1");
                    optionalCruise.ifPresent(cruise -> currentCruise = cruise);
                    break;
                case 1:
                    updateAllRoomsForCruise("cruise2", roomListModel);
                    optionalCruise = getCruise("cruise2");
                    optionalCruise.ifPresent(cruise -> currentCruise = cruise);

                    break;
                case 2:
                    updateAllRoomsForCruise("cruise3", roomListModel);
                    optionalCruise = getCruise("cruise3");
                    optionalCruise.ifPresent(cruise -> currentCruise = cruise);

                    break;
            }
        });
        updateAllRoomsForCruise("cruise1", roomListModel);
        Optional<Cruise> optionalCruise = getCruise("cruise1");
        optionalCruise.ifPresent(cruise -> currentCruise = cruise);


        JButton applyFiltersButton = createStyledButton("Apply Filters", DEFAULT_FONT, BUTTON_COLOR);
        applyFiltersButton.addActionListener((ActionEvent e) -> {
            if (startDateField.getValue() == null || endDateField.getValue() == null) {
                JOptionPane.showMessageDialog(null, "Please select a start and end date before selecting rooms.",
                        "Date Selection Required", JOptionPane.WARNING_MESSAGE, scaledErrorImage);
                return;
            }
            updateRoomList(currentCruise.getName(), qualityComboBox, numBedsComboBox, bedTypeComboBox,
                    isSmokingCheckBox, startDateField, endDateField, roomListModel);
        });
        filterPanel.add(applyFiltersButton);

        roomList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // double click
                    Room selectedRoom = roomList.getSelectedValue();
                    JLabel rendererComponent = (JLabel) roomList.getCellRenderer()
                            .getListCellRendererComponent(roomList, selectedRoom, roomList.getSelectedIndex(), false, false);

                    Boolean isRoom = (Boolean) rendererComponent.getClientProperty("isRoom");
                    if (isRoom != null && !isRoom) {
                        return;
                    }
                    if (startDateField.getValue() == null || endDateField.getValue() == null) {
                        JOptionPane.showMessageDialog(null, "Please select a start and end date before selecting rooms.",
                                "Date Selection Required", JOptionPane.WARNING_MESSAGE, scaledErrorImage);
                        return;
                    }
                    if (selectedRoom != null) {
                        openReservationDetailPanel(selectedRoom, startDateField, endDateField, currentCruise, qualityComboBox,
                                                    numBedsComboBox,bedTypeComboBox,isSmokingCheckBox,roomListModel);
                    }
                }
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 240));
        JLabel usageInstructions = new JLabel("First Select a Cruise to Display. Then use the filters to select " +
                "your choice of room. YOU MUST SELECT A VALID START AND END DATE. Reserve by double click");
        usageInstructions.setFont(new Font("Arial", Font.BOLD, 12));
        usageInstructions.setForeground(new Color(50, 50, 50));
        usageInstructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(usageInstructions, BorderLayout.AFTER_LAST_LINE);

        topPanel.add(cruiseTabs, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(usageInstructions, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel createCurrentReservationsPanel(DefaultListModel<Reservation> reservationListModel, JList<Reservation> reservationsList) {
        JPanel panel = new JPanel(new BorderLayout());
        for (Reservation reservation : currentGuest.getReservations()) {
            reservationListModel.addElement(reservation);
        }

        reservationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservationsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Reservation reservation = (Reservation) value;
                String text = String.format("Reservation ID: %d, Cruise: %s, Start: %s, End: %s",
                        reservation.getId(), reservation.getCruiseName(),
                        reservation.getStartDate(), reservation.getEndDate());
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });

        JScrollPane scrollPane = new JScrollPane(reservationsList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton modifyButton = new JButton("Modify Reservation");
        JButton deleteButton = new JButton("Cancel Reservation");

        modifyButton.addActionListener(e -> {
            Reservation selectedReservation = reservationsList.getSelectedValue();
            if (selectedReservation != null) {
                JTextField startDateField = new JTextField(selectedReservation.getStartDate().toString());
                JTextField endDateField = new JTextField(selectedReservation.getEndDate().toString());

                Object[] message = {
                        "Start Date (yyyy-mm-dd):", startDateField,
                        "End Date (yyyy-mm-dd):", endDateField
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Modify Reservation", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    LocalDate newStartDate = LocalDate.parse(startDateField.getText());
                    LocalDate newEndDate = LocalDate.parse(endDateField.getText());
                    String cruiseName = selectedReservation.getCruiseName();
                    Optional<Cruise> cruiseOptional = getCruise(cruiseName);
                    Cruise cruise = cruiseOptional.get();

                    boolean success = currentGuest.modifyReservation(selectedReservation.getId(), selectedReservation.getRoom(), newStartDate, newEndDate, cruise);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Reservation modified successfully.");
                        updateReservationsList(reservationListModel);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to modify reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No reservation selected.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            Reservation selectedReservation = reservationsList.getSelectedValue();
            if (selectedReservation != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to cancel this reservation?",
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = currentGuest.cancelReservation(selectedReservation.getId());
                    if (success) {
                        reservationListModel.removeElement(selectedReservation);
                        currentGuest.getReservations().remove(selectedReservation);
                        JOptionPane.showMessageDialog(null, "Reservation cancelled successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to cancel reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No reservation selected.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(modifyButton);
        buttonsPanel.add(deleteButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private static void updateReservationsList(DefaultListModel<Reservation> model) {
        currentGuest.getReservations();
        model.clear();
        for (Reservation reservation : currentGuest.getReservations()) {
            model.addElement(reservation);
        }
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
                                                   JFormattedTextField endDateField, Cruise cruise,
                                                   JComboBox<Room.Quality> qualityComboBox,
                                                   JComboBox<Integer> numBedsComboBox,
                                                   JComboBox<Room.BedType> bedTypeComboBox,
                                                   JCheckBox isSmokingCheckBox,
                                                   DefaultListModel<Room> roomListModel) {
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
        detailArea.append("\nTotal Cost: " + room.getTotalCost(start, end));

        JScrollPane scrollPane = new JScrollPane(detailArea);

        JButton makeReservationButton = new JButton("Make Reservation");
        makeReservationButton.addActionListener(e -> {
            boolean success = currentGuest.makeReservation(room, start, end, cruise);

            if (success) {
                updateReservationsList(reservationListModel);
                JOptionPane.showMessageDialog(reservationDialog, "Reservation made successfully!",
                        "Reservation Status", JOptionPane.DEFAULT_OPTION, scaledSuccessIcon);
                updateRoomList(currentCruise.getName(), qualityComboBox, numBedsComboBox, bedTypeComboBox,
                        isSmokingCheckBox, startDateField, endDateField, roomListModel);
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

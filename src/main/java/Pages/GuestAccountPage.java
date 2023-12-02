package Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.time.*;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import Person.Guest.*;

import Cruise.Cruise;
import Cruise.Room;

import static Pages.CruiseAppUtilities.*;
import static Cruise.Cruise.*;

/**
 * The {@code GuestAccountPage} class represents the guest account interface.
 *
 * <p>This class provides the user interface components and functionality necessary for a guest to interact with the
 * cruise booking system. It includes methods for creating and managing different panels and tabs for room selection,
 * viewing current reservations, and modifying reservations.</p>
 *
 * <p>Key features of the {@code GuestAccountPage} include:</p>
 * <ul>
 *   <li>Creating a tabbed pane for the guest view, with tabs for room selection and current reservations.</li>
 *   <li>Providing a room selection interface, enabling guests to choose and reserve rooms based on various criteria.</li>
 *   <li>Offering a panel to view and manage current reservations, including options to modify or cancel bookings.</li>
 *   <li>Updating room and reservation lists dynamically based on guest interactions and system changes.</li>
 * </ul>
 *
 * <p>The class heavily relies on other components of the system, such as the {@code Room}, {@code Cruise}, and
 * {@code Reservation} classes, to provide its functionality.</p>
 */
public class GuestAccountPage {

    private static Room NO_ROOMS_FOUND = new Room();
    public static Cruise currentCruise;
    protected static DefaultListModel<Reservation> reservationListModel = new DefaultListModel<>();
    protected static JList<Reservation> reservationsList = new JList<>(reservationListModel);
    static boolean filtersApplied = false;

    /**
     * Constructs a {@code JTabbedPane} for the guest view.
     *
     * <p>This method creates a {@code JTabbedPane} and adds two specialized panels for guest interactions:</p>
     * <ol>
     *   <li>
     *     'Select Room' panel: Formed using 'createRoomSelectionPanel'. This tab enables guests to choose
     *     rooms for reservation. It is titled "Select Room" and comes with a tooltip "Select a room for reservation".
     *   </li>
     *   <li>
     *     'Current Reservations' panel: Formed using 'createCurrentReservationsPanel'. This tab presents
     *     the guest's existing reservations. It is labeled "Current Reservations" and includes a tooltip
     *     "View your current reservations".
     *   </li>
     * </ol>
     *
     * @param frame the {@code JFrame} in which this tabbed pane will be utilized
     * @return a {@code JTabbedPane} specifically designed for guest interactions
     */
    public static JTabbedPane createGuestViewTabbedPane(JFrame frame) {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel roomSelectionPanel = createRoomSelectionPanel(frame);
        tabbedPane.addTab("Select Room", null, roomSelectionPanel, "Select a room for reservation");

        JPanel currentReservationsPanel = createCurrentReservationsPanel(reservationListModel, reservationsList, frame);
        tabbedPane.addTab("Current Reservations", null, currentReservationsPanel, "View your current reservations");

        return tabbedPane;
    }

    /**
     * Creates a room selection panel enabling guests to choose and reserve rooms on a cruise.
     *
     * <p>This panel is equipped with interactive components to facilitate room selection based on various preferences:</p>
     * <ul>
     *   <li>Combo boxes for selecting room quality, number of beds, and bed type, along with a checkbox for smoking preferences.</li>
     *   <li>Date fields for specifying the start and end dates of the stay.</li>
     *   <li>A tabbed pane displaying different cruises, each tab representing a distinct cruise.</li>
     *   <li>A list of rooms, updated dynamically in response to applied filters, presented within a scroll pane.</li>
     *   <li>An 'Apply Filters' button for refreshing the room list according to chosen criteria.</li>
     *   <li>Double-click functionality on the room list.</li>
     *   <li>A 'Back' button, primarily for agents to navigate back to the agent view (visible in agent access mode).</li>
     * </ul>
     *
     * @param frame the {@code JFrame} used for adding this panel and facilitating view transitions
     * @return a {@code JPanel} that constitutes the room selection interface for guests
     */
    public static JPanel createRoomSelectionPanel(JFrame frame) {
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
        //filterPanel.setVisible(false);
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

        JLabel validDatesLabel = new JLabel();
        validDatesLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        validDatesLabel.setForeground(Color.DARK_GRAY);

        cruiseTabs.addChangeListener(e -> {
            JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
            int index = sourceTabbedPane.getSelectedIndex();

            switch(index) {
                case 0:
                    updateAllRoomsForCruise("cruise1", roomListModel);
                    roomList.setVisible(false);
                    Optional<Cruise> optionalCruise = getCruise("cruise1");
                    optionalCruise.ifPresent(cruise -> currentCruise = cruise);
                    updateValidDatesLabel(validDatesLabel, "cruise1");
                    break;
                case 1:
                    updateAllRoomsForCruise("cruise2", roomListModel);
                    roomList.setVisible(false);
                    optionalCruise = getCruise("cruise2");
                    optionalCruise.ifPresent(cruise -> currentCruise = cruise);
                    updateValidDatesLabel(validDatesLabel, "cruise2");
                    break;
                case 2:
                    updateAllRoomsForCruise("cruise3", roomListModel);
                    roomList.setVisible(false);
                    optionalCruise = getCruise("cruise3");
                    optionalCruise.ifPresent(cruise -> currentCruise = cruise);
                    updateValidDatesLabel(validDatesLabel, "cruise3");
                    break;
            }
        });
        updateAllRoomsForCruise("cruise1", roomListModel);
        updateValidDatesLabel(validDatesLabel, "cruise1");

        Optional<Cruise> optionalCruise = getCruise("cruise1");
        optionalCruise.ifPresent(cruise -> currentCruise = cruise);

        roomList.setVisible(false);

        JButton applyFiltersButton = createStyledButton("Apply Filters", DEFAULT_FONT, BUTTON_COLOR);
        applyFiltersButton.addActionListener((ActionEvent e) -> {
            if (startDateField.getValue() == null || endDateField.getValue() == null) {
                JOptionPane.showMessageDialog(null, "Please select a start and end date before selecting rooms.",
                        "Date Selection Required", JOptionPane.WARNING_MESSAGE, scaledErrorImage);
                return;
            }
            updateRoomList(currentCruise.getName(), qualityComboBox, numBedsComboBox, bedTypeComboBox,
                    isSmokingCheckBox, startDateField, endDateField, roomListModel);
            filtersApplied = true;
            roomList.setVisible(true);
        });
        filterPanel.add(applyFiltersButton);

        roomList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!filtersApplied) {
                    JOptionPane.showMessageDialog(null, "Please apply filters before selecting a room.",
                            "Apply Filters", JOptionPane.WARNING_MESSAGE, scaledErrorImage);
                    return;
                }
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
        topPanel.add(validDatesLabel, BorderLayout.SOUTH);

        topPanel.add(cruiseTabs, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        if (currentAgent != null) {
            JButton backButton = createStyledButton("Back", BUTTON_FONT,BUTTON_COLOR);
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentGuest = null;
                    addAgentPanelToFrame(frame);
                    switchToPanel(frame, "Agent View");
                }
            });

            bottomPanel.add(backButton);
        }

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates a panel for guests to view and manage their current cruise reservations.
     *
     * <p>This panel features a list of the guest's current reservations, displayed in a {@code JList} with each
     * reservation's details formatted as a specific string pattern. It provides functionalities for guests to modify or
     * cancel their reservations through buttons equipped with action listeners.</p>
     *
     * <p>Key features of the panel include:</p>
     * <ul>
     *   <li>A {@code JScrollPane} housing a {@code JList} that displays the reservations.</li>
     *   <li>'Modify Reservation' button: Opens a dialog allowing the guest to change dates for the selected reservation.</li>
     *   <li>'Cancel Reservation' button: Offers an option to cancel the selected reservation.</li>
     *   <li>'Logout' button: Logs out the current guest and navigates back to the login panel (visible only in guest mode).</li>
     * </ul>
     *
     * @param reservationListModel the model providing data for the reservation list
     * @param reservationsList the {@code JList} component that displays the guest's reservations
     * @param frame the {@code JFrame} used for facilitating view transitions and logout operations
     * @return a {@code JPanel} tailored for guests to view and manage their current cruise reservations
     */
    private static JPanel createCurrentReservationsPanel(DefaultListModel<Reservation> reservationListModel, JList<Reservation> reservationsList, JFrame frame) {
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
                    try {
                        LocalDate newStartDate = LocalDate.parse(startDateField.getText());
                        LocalDate newEndDate = LocalDate.parse(endDateField.getText());

                        if(newStartDate.isBefore(LocalDate.now()) || newEndDate.isBefore(LocalDate.now()) || newEndDate.isBefore(newStartDate)) {
                            JOptionPane.showMessageDialog(null, "Invalid Dates.", "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                            return;
                        }

                        String cruiseName = selectedReservation.getCruiseName();
                        Optional<Cruise> cruiseOptional = getCruise(cruiseName);

                        if (cruiseOptional.isPresent()) {
                            Cruise cruise = cruiseOptional.get();
                            Room currentRoom = selectedReservation.getRoom();

                            Optional<Room> availableRoom = cruise.isRoomAvailable(
                                    currentRoom.getQuality(),
                                    currentRoom.getNumBeds(),
                                    currentRoom.getBedType(),
                                    currentRoom.isSmoking(),
                                    newStartDate,
                                    newEndDate
                            );

                            if (availableRoom.isPresent()) {
                                boolean success = currentGuest.modifyReservation(
                                        selectedReservation.getId(),
                                        availableRoom.get(),
                                        newStartDate,
                                        newEndDate,
                                        cruise
                                );
                                if (success) {
                                    JOptionPane.showMessageDialog(null, "Reservation modified successfully.",
                                            "Success", JOptionPane.PLAIN_MESSAGE, scaledSuccessIcon);
                                    updateReservationsList(reservationListModel);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Failed to modify reservation.",
                                            "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "The room is not available for the selected dates.",
                                        "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Cruise not found.", "Error",
                                    JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                        }
                    } catch (DateTimeParseException dtpe) {
                        JOptionPane.showMessageDialog(null, "Invalid date format. Please use yyyy-mm-dd.",
                                "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No reservation selected.", "Error",
                        JOptionPane.WARNING_MESSAGE, scaledErrorImage);
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
                    double refundSub = currentGuest.calculateRefund(selectedReservation.getId());
                    double reserveCost = selectedReservation.getRoom().getTotalCost(selectedReservation.getStartDate(),selectedReservation.getEndDate());
                    double refundAmt = reserveCost - refundSub;
                    boolean success = currentGuest.cancelReservation(selectedReservation.getId());
                    if (success) {
                        reservationListModel.removeElement(selectedReservation);
                        currentGuest.calculateRefund(selectedReservation.id);
                        currentGuest.getReservations().remove(selectedReservation);
                        JOptionPane.showMessageDialog(null,
                                "Reservation cancelled successfully. You will be refunded: " + refundAmt
                        );
                    } else {
                        JOptionPane.showMessageDialog(null, "You cannot cancel this reservation.",
                                "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No reservation selected.", "Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            currentGuest = null;
            switchToPanel(frame, "Login");
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(modifyButton);
        buttonsPanel.add(deleteButton);
        if (currentAgent == null) {
            buttonsPanel.add(logoutButton);
        }
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Updates a {@code JLabel} with valid reservation dates for a specified cruise.
     *
     * <p>This method retrieves information about a cruise using its name and determines the valid dates available for reservation
     * on that specific cruise. It formats this into string and updates the provided {@code JLabel}. If the cruise is found, the label displays
     * a list of valid dates; otherwise, it shows a message indicating that no valid dates are available for the cruise.</p>
     *
     * @param validDatesLabel the {@code JLabel} to be updated with the valid reservation dates
     * @param cruiseName the name of the cruise for which valid dates are to be retrieved
     */
    public static void updateValidDatesLabel(JLabel validDatesLabel, String cruiseName) {
        Optional<Cruise> cruiseOpt = getCruise(cruiseName);
        if (cruiseOpt.isPresent()) {
            Cruise cruise = cruiseOpt.get();
            ArrayList<LocalDate> validDates = cruise.getValidReservationDates();
            StringBuilder validDatesText = new StringBuilder("Valid reservation dates based on travel path: ");
            for (LocalDate date : validDates) {
                validDatesText.append(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))).append(", ");
            }
            if (validDates.size() > 0) {
                validDatesText.setLength(validDatesText.length() - 2);
            }
            validDatesLabel.setText(validDatesText.toString());
        } else {
            validDatesLabel.setText("No valid dates available for this cruise.");
        }
    }

    /**
     * Refreshes the reservation list model with the latest reservations of the current guest.
     *
     * <p>This method clears the provided DefaultListModel of Reservation objects and repopulates it with
     * the current reservations of the guest.</p>
     *
     * <p>Use this method to keep the UI's display of reservations updated, particularly after changes in the
     * guest's reservation status, such as new bookings, modifications, or cancellations.</p>
     *
     * @param model The DefaultListModel of Reservation objects to be updated with the guest's current reservations.
     */
    private static void updateReservationsList(DefaultListModel<Reservation> model) {

        currentGuest.getReservations();
        model.clear();
        for (Reservation reservation : currentGuest.getReservations()) {
            model.addElement(reservation);
        }
    }

    /**
     * Updates the room list model with all available rooms for a specified cruise.
     *
     * <p>This method resets the 'filtersApplied' flag to false, indicating that no specific room filters are currently applied.
     * It then attempts to retrieve the cruise by its name using 'getCruise'. If the cruise is found, the method clears the
     * existing room list model and repopulates it with all rooms available on that cruise. Each room from the cruise's room list
     * is added to the model.</p>
     *
     * @param cruiseName     the name of the cruise for which room details are to be retrieved
     * @param roomListModel  the DefaultListModel of Room objects to be updated with rooms from the specified cruise
     */
    private static void updateAllRoomsForCruise(String cruiseName, DefaultListModel<Room> roomListModel) {
        filtersApplied = false;
        Optional<Cruise> cruise = getCruise(cruiseName);
        if (cruise.isPresent()) {
            ArrayList<Room> rooms = cruise.get().getRoomList();
            roomListModel.clear();
            for (Room room : rooms) {
                roomListModel.addElement(room);
            }
        }
    }

    /**
     * Updates the room list model based on selected filters for a specified cruise.
     *
     * <p>This method retrieves the specified cruise by name and filters available rooms based on user-selected criteria,
     * including room quality, number of beds, bed type, smoking preference, and reservation dates. It obtains these filter
     * values from the provided combo boxes and check box. If a room matches the selected criteria, it is added to the
     * room list model. If no rooms match the criteria, a placeholder (NO_ROOMS_FOUND) is added to indicate the absence
     * of available rooms.</p>
     *
     * @param cruiseName        The name of the cruise for which rooms are being searched.
     * @param qualityComboBox   The combo box for selecting room quality.
     * @param numBedsComboBox   The combo box for selecting the number of beds.
     * @param bedTypeComboBox   The combo box for selecting the type of bed.
     * @param isSmokingCheckBox The check box for selecting smoking preference.
     * @param startDateField    The field for inputting the reservation start date.
     * @param endDateField      The field for inputting the reservation end date.
     * @param roomListModel     The DefaultListModel of Room objects to be updated.
     */
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

    /**
     * Opens a dialog displaying detailed information for a room reservation and allows the user to make a reservation.
     *
     * <p>This method creates a modal {@code JDialog} displaying details of the selected room and the chosen reservation dates.
     * It includes a {@code JTextArea} within a {@code JScrollPane} showing the room's quality, number of beds, and total cost for the
     * specified duration. A 'Make Reservation' button is provided to finalize the reservation.</p>
     *
     * <p>Upon clicking the 'Make Reservation' button, the method attempts to make a reservation for the current guest.
     * It updates the reservations list and room list based on the outcome, displaying a success or error message accordingly.</p>
     *
     * @param room              The {@code Room} object for which the reservation is being made.
     * @param startDateField    The field containing the selected start date of the reservation.
     * @param endDateField      The field containing the selected end date of the reservation.
     * @param cruise            The {@code Cruise} object associated with the reservation.
     * @param qualityComboBox   The combo box used for selecting room quality.
     * @param numBedsComboBox   The combo box used for selecting the number of beds.
     * @param bedTypeComboBox   The combo box used for selecting the bed type.
     * @param isSmokingCheckBox The check box for smoking preference.
     * @param roomListModel     The {@code DefaultListModel} of {@code Room} objects to be updated post-reservation.
     */
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
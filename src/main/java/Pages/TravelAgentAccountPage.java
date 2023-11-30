package Pages;

import Controllers.GuestController;
import Cruise.Room;
import Cruise.*;
import Person.Guest;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import static Cruise.Room.*;
import static Pages.CruiseAppUtilities.*;
import static Person.Guest.usernameExists;
import static Person.TravelAgent.modifyTravelAgentAccount;


/**
 * The {@code TravelAgentAccountPage} class in the {@code Pages} package is dedicated to creating and managing the user
 * interface components for travel agents.
 *
 * <p>This class consists of static methods that generate different panels and tabs designed for travel agent activities.</p>
 *
 * <p>Key functionalities and features provided by this class include:</p>
 * <ul>
 *   <li>Generating a tabbed pane that organizes various travel agent-related tasks into distinct tabs. This includes tabs for room management,
 *   guest reservation booking, and account options.</li>
 *   <li>Creating panels that allow agents to add and modify room details, book reservations on behalf of guests, and update their account information.</li>
 *   <li>Implementing dialog boxes for modifying room attributes and adding new rooms to a cruise</li>
 * </ul>
 */
public class TravelAgentAccountPage {
    /**
     * Initializes and returns a {@code JTabbedPane} tailored for the travel agent interface.
     *
     * <p>This method constructs a {@code JTabbedPane} with multiple tabs, each dedicated to a specific function relevant to travel agents. The tabs are designed to
     * facilitate various aspects of cruise booking and account management. The following tabs are included:</p>
     * <ul>
     *   <li>'Manage Rooms' tab: Uses 'createAddRoomPanel'. This tab allows agents to add new rooms to the system and manage existing ones.</li>
     *   <li>'Book For Guest' tab: Uses 'createReserveForGuestPanel'. This tab enables agents to make bookings on behalf of guests.</li>
     *   <li>'Account Options' tab: Uses 'createModifyAccountPanel'. This tab provides functionalities for agents to modify their account settings and includes logout capabilities.</li>
     * </ul>
     *
     * <p>Each tab is appropriately labeled and comes with a tooltip offering a brief description of its purpose and functionality.</p>
     *
     * @param frame the {@code JFrame} in which this tabbed pane is to be integrated
     * @return a fully-configured {@code JTabbedPane} designed specifically to cater to the various tasks and responsibilities of travel agents
     */
    public static JTabbedPane createTravelAgentViewTabbedPane(JFrame frame){
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel addRoomPanel = createAddRoomPanel(frame);
        tabbedPane.addTab("Manage Rooms", null, addRoomPanel, "Add rooms and manage existing rooms");

        JPanel reserveForGuestPanel = createReserveForGuestPanel(frame);
        tabbedPane.addTab("Book For Guest", null, reserveForGuestPanel, "Reserve on behalf of guest");

        JPanel modifyAccountPanel = createModifyAccountPanel(frame);
        tabbedPane.addTab("Account Options", null ,modifyAccountPanel, "Modify Account | Logout");
        return tabbedPane;
    }

    /**
     * Creates and returns a panel designed for adding and managing rooms on different cruises.
     *
     * <p>This panel is structured with a {@code GridBagLayout} and includes several key components to facilitate room management for travel agents:</p>
     * <ul>
     *   <li>A {@code JComboBox} to select from a list of available cruises. This component allows agents to choose a specific cruise for which they intend to manage room allocations.</li>
     *   <li>A {@code JList} displayed within a {@code JScrollPane}, showing a list of rooms associated with the selected cruise. This list updates dynamically based on the selected cruise.</li>
     *   <li>'Add Room' and 'Modify Room' buttons. The 'Add Room' button launches a dialog for adding new rooms to the cruise, while the 'Modify Room' button opens a dialog for editing details of an existing room.</li>
     * </ul>
     *
     * <p>The panel is specifically for travel agents, allowing manage and update room availability across various cruises.</p>
     *
     * @param frame the {@code JFrame} used for displaying dialogs and managing interactive actions related to room management
     * @return a {@code JPanel} used for adding and modifying rooms on cruises
     */
    public static JPanel createAddRoomPanel(JFrame frame){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        String[] cruiseOptions = {"CRUISE1", "CRUISE2", "CRUISE3"};
        JComboBox<String> cruiseComboBox = new JComboBox<>(cruiseOptions);

        DefaultListModel<String> roomListModel = new DefaultListModel<>();
        JList<String> roomList = new JList<>(roomListModel);
        JScrollPane listScrollPane = new JScrollPane(roomList);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(cruiseComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(listScrollPane, gbc);

        JButton addRoomButton = createStyledButton("Add Room", BUTTON_FONT, BUTTON_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addRoomButton, gbc);
        updateRoomListForCruise(cruiseComboBox.getItemAt(0),roomListModel);
        cruiseComboBox.addActionListener(e -> {
            String selectedCruise = (String) cruiseComboBox.getSelectedItem();
            if (selectedCruise != null) {
                updateRoomListForCruise(selectedCruise,roomListModel);
            }
        });
        addRoomButton.addActionListener(e -> {
            String selectedCruise = (String) cruiseComboBox.getSelectedItem();
            if (selectedCruise != null) {
                Optional<Cruise> optCruise = Cruise.getCruise(selectedCruise);
                if (optCruise.isPresent()) {
                    Cruise currentCruise = optCruise.get();
                    //Third param is runnable
                    openAddRoomDialog(frame, currentCruise, () -> {
                        updateRoomListForCruise(selectedCruise, roomListModel);
                    });
                } else {
                    JOptionPane.showMessageDialog(frame, "Could not find cruise details", "Error",
                            JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                }
            }
        });

        JButton modifyRoomButton = createStyledButton("Modify Room", BUTTON_FONT, BUTTON_COLOR);
        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(modifyRoomButton, gbc);

        modifyRoomButton.addActionListener(e -> {
            String selectedRoomInfo = roomList.getSelectedValue();
            if (selectedRoomInfo != null && !selectedRoomInfo.isEmpty()) {
                String selectedCruise = (String) cruiseComboBox.getSelectedItem();
                if (selectedCruise != null) {
                    Optional<Cruise> optCruise = Cruise.getCruise(selectedCruise);
                    if (optCruise.isPresent()) {
                        Cruise cruise = optCruise.get();
                        String[] parts = selectedRoomInfo.split(", ");
                        String idPart = parts[0];
                        String[] idSplit = idPart.split(" ");
                        int roomId = Integer.parseInt(idSplit[1]);

                        System.out.println(cruiseComboBox.getSelectedItem().toString() + " " + roomId);

                        Room selectedRoom = getRoom(cruiseComboBox.getSelectedItem().toString(), roomId);
                        openModifyRoomDialog(frame, cruise, selectedRoom, () -> {
                                    updateRoomListForCruise(selectedCruise, roomListModel);
                                });
                    } else {
                        JOptionPane.showMessageDialog(frame, "Could not find cruise details", "Error",
                                JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a room to modify.", "No Room Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        });


        return panel;
    }

    /**
     * Initiates and displays a dialog for editing the attributes of a selected room within a specific cruise.
     *
     * <p>This method creates a {@code JDialog} that provides a form for modifying various characteristics of a room.
     * The fields in the form are pre-populated with the existing details of the room. Key features of the dialog include:</p>
     * <ul>
     *   <li>A panel containing input fields and corresponding labels for each attribute of the room.</li>
     *   <li>A 'Modify' button, which, upon being clicked, saves the changes to the room and updates the information within the cruise data.</li>
     *   <li>A 'Cancel' button that allows closing the dialog without implementing any changes.</li>
     * </ul>
     *
     * <p>Upon successful modification, a confirmation message is displayed to the user, and the {@code onModificationSuccess} {@code Runnable} is executed to
     * update the user interface. If the modification process encounters an error, an error message is presented.</p>
     *
     * <p>This dialog is tailored for use by travel agents, allowing them a way to update room details within a cruise.</p>
     *
     * @param frame the parent {@code JFrame} to which this dialog is attached,
     * @param cruise the {@code Cruise} object that contains the room to be modified
     * @param room the {@code Room} object undergoing modification
     * @param onModificationSuccess a {@code Runnable} to be executed upon successful modification of the room, used for updating UI elements
     */
    private static void openModifyRoomDialog(JFrame frame, Cruise cruise, Room room, Runnable onModificationSuccess) {
        JDialog modifyDialog = new JDialog(frame, "Modify Room", true);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField bedsField = new JTextField(String.valueOf(room.getNumBeds()), 10);
        JComboBox<Room.Quality> qualityComboBox = new JComboBox<>(Room.Quality.values());
        qualityComboBox.setSelectedItem(room.getQuality());
        JComboBox<Room.BedType> bedTypeComboBox = new JComboBox<>(Room.BedType.values());
        bedTypeComboBox.setSelectedItem(room.getBedType());
        JCheckBox smokingCheckBox = new JCheckBox("Smoking", room.isSmoking());

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Beds:"), gbc);
        gbc.gridx = 1;
        formPanel.add(bedsField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Quality:"), gbc);
        gbc.gridx = 1;
        formPanel.add(qualityComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Bed Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(bedTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Smoking:"), gbc);
        gbc.gridx = 1;
        formPanel.add(smokingCheckBox, gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton modifyButton = new JButton("Modify");
        modifyButton.addActionListener(e -> {
            int numBeds = Integer.parseInt(bedsField.getText());
            Room.Quality quality = (Room.Quality) qualityComboBox.getSelectedItem();
            Room.BedType bedType = (Room.BedType) bedTypeComboBox.getSelectedItem();
            boolean isSmoking = smokingCheckBox.isSelected();

            room.setNumBeds(numBeds);
            room.setQuality(quality);
            room.setBedType(bedType);
            room.setSmoking(isSmoking);


            if (cruise.modifyRoom(room)) {
                JOptionPane.showMessageDialog(frame, "Room updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                onModificationSuccess.run();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update room.", "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
            }

            modifyDialog.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> modifyDialog.dispose());

        buttonsPanel.add(modifyButton);
        buttonsPanel.add(cancelButton);

        modifyDialog.setLayout(new BorderLayout());
        modifyDialog.add(formPanel, BorderLayout.CENTER);
        modifyDialog.add(buttonsPanel, BorderLayout.SOUTH);

        modifyDialog.pack();
        modifyDialog.setLocationRelativeTo(frame);
        modifyDialog.setVisible(true);
    }

    /**
     * Constructs and returns a panel that enables travel agents to make room reservations for guests.
     *
     * <p>This panel is designed to assist travel agents in booking reservations on behalf of guests.
     * It includes:</p>
     * <ul>
     *   <li>A {@code JComboBox} to select a cruise from a set of predefined options ("CRUISE1", "CRUISE2", "CRUISE3").
     *   This allows the agent to specify the cruise for which the reservation is being made.</li>
     *   <li>A {@code JList} displayed within a {@code JScrollPane}, showcasing a list of guests.
     *   This list is retrieved from 'GuestController.getGuestList()' and is used by the agent to select the guest for
     *   whom the reservation is being made.</li>
     *   <li>A 'Book Reservation' button that, when clicked, initiates the reservation process for the selected guest
     *   on the chosen cruise.</li>
     * </ul>
     *
     * <p>The guest list is presented in the {@code JList} using a custom cell renderer, which displays each guest's
     * username for easy identification. On selecting a guest and clicking the 'Book Reservation' button, the panel
     * transitions to the 'Guest View' interface, pre-loaded with the selected guest's details.</p>
     *
     * <p>The layout of the panel is organized using a {@code GridBagLayout}, with {@code GridBagConstraints}.</p>
     *
     * @param frame the {@code JFrame} in which this panel is to be displayed
     * @return a {@code JPanel} designed for travel agents to facilitate the booking of reservations on behalf of guests
     */
    public static JPanel createReserveForGuestPanel(JFrame frame){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JComboBox<String> cruiseComboBox = new JComboBox<>(new String[]{"CRUISE1", "CRUISE2", "CRUISE3"});
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(cruiseComboBox, gbc);

        List<Guest> guests = GuestController.getGuestList();

        DefaultListModel<Guest> guestListModel = new DefaultListModel<>();
        for (Guest guest : guests) {
            guestListModel.addElement(guest);
        }

        JList<Guest> guestJList = new JList<>(guestListModel);
        guestJList.setCellRenderer(new ListCellRenderer<Guest>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Guest> list, Guest value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value.getUsername());
                label.setOpaque(true);
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }
                return label;
            }
        });

        JScrollPane guestScrollPane = new JScrollPane(guestJList);
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(guestScrollPane, gbc);

        JButton bookButton = new JButton("Book Reservation");
            bookButton.addActionListener(e -> {
                if (guestJList.getSelectedValue() == null ){
                    JOptionPane.showMessageDialog(frame, "Please Select A Guest To Continue.", "Error",
                            JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                    return;
                }
                currentGuest = guestJList.getSelectedValue();
                CruiseAppUtilities.addGuestPanelToFrame(frame);
                switchToPanel(frame, "Guest View");
            });
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(bookButton, gbc);

        return panel;
    }

    /**
     * Creates and returns a panel that allows travel agents to modify their account information.
     *
     * <p>This panel is for travel agents to update their account details. It provides a coherent layout using a {@code GridBagLayout}
     * . The fields for account information are pre-filled with the current agent's data.</p>
     *
     * <p>Key features of the panel include:</p>
     * <ul>
     *   <li>Text fields for the username, password, and address, pre-populated with the agent's existing information.</li>
     *   <li>An 'Update' button, which on clicking, saves the changes made to the account. This process includes a check
     *   for username availability and updates the account through 'modifyTravelAgentAccount'.</li>
     *   <li>A 'Logout' button, which facilitates the agent's logout process and transitions back to the login panel.</li>
     * </ul>
     *
     * @param frame the {@code JFrame} used for displaying dialogs and managing actions
     * @return a {@code JPanel} dedicated to enabling account modifications for travel agents
     */
    public static JPanel createModifyAccountPanel(JFrame frame){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        usernameField.setText(currentAgent.getUsername());

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setText(currentAgent.getPassword());

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(20);
        addressField.setText(currentAgent.getAddress());

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            String newUsername = usernameField.getText();
            String newPassword = new String(passwordField.getPassword());
            String newAddress = addressField.getText();

            if (!newUsername.equals(currentAgent.getUsername()) && usernameExists(newUsername)) {
                JOptionPane.showMessageDialog(frame, "Username already exists, please choose another one.", "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
            } else {
                boolean success = modifyTravelAgentAccount( currentAgent.getUsername() ,newUsername, newPassword, newAddress);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Account updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    currentAgent.setUsername(newUsername);
                    currentAgent.setPassword(newPassword);
                    currentAgent.setAddress(newAddress);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                }
            }
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            currentAgent = null;
            switchToPanel(frame, "Login");
        });


        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameLabel, gbc);
        panel.add(usernameField, gbc);
        panel.add(passwordLabel, gbc);
        panel.add(passwordField, gbc);
        panel.add(addressLabel, gbc);
        panel.add(addressField, gbc);
        panel.add(updateButton, gbc);
        panel.add(logoutButton, gbc);

        return panel;
    }

    /**
     * Populates and updates the given {@code DefaultListModel} with the details of all rooms for a specified cruise.
     *
     * <p>This method is responsible for fetching and displaying room information for a particular cruise.
     * It begins by retrieving the {@code Cruise} object based on the provided cruise name. If the specified cruise is found,
     * the method proceeds to obtain the list of rooms associated with that cruise. Each room's information is formatted
     * into a string representation and added to the model.</p>
     *
     * <p>The primary use of this method is to refresh and display updated room data in user interface components, such as a {@code JList}.</p>
     *
     * @param selectedCruise the name of the cruise for which room details need to be retrieved and displayed
     * @param roomListModel the {@code DefaultListModel} that is to be updated with the detailed information of the rooms associated with the specified cruise
     */

    private static void updateRoomListForCruise(String selectedCruise, DefaultListModel<String> roomListModel) {
        Optional<Cruise> optCruise = Cruise.getCruise(selectedCruise);
        optCruise.ifPresent(cruise -> {
            ArrayList<Room> rooms = cruise.getRoomList();
            roomListModel.clear();
            for (Room room : rooms) {
                String roomDetails = String.format("ID: %d, Beds: %d, Type: %s, Quality: %s, Smoking: %s",
                        room.getID(),
                        room.getNumBeds(),
                        room.getBedType().toString(),
                        room.getQuality().toString(),
                        room.isSmoking() ? "Yes" : "No"
                );
                roomListModel.addElement(roomDetails);
            }
        });
    }

    /**
     * Launches a dialog for the addition of a new room to a specific cruise.
     *
     * <p>This method creates a {@code JDialog} designed with a {@code GridLayout} with input fields and a submission button.
     * The dialog includes:</p>
     * <ul>
     *   <li>'Number of Beds': A {@code JComboBox} to select the number of beds in the new room.</li>
     *   <li>'Bed Type': A {@code JComboBox} for choosing the type of bed.</li>
     *   <li>'Room Quality': A {@code JComboBox} to determine the quality grade of the room.</li>
     *   <li>'Smoking': A {@code JCheckBox} to specify whether the room allows for smoking or not.</li>
     *   <li>'Submit' button: Upon clicking, this button captures the entered data, constructs a {@code Room} object,
     *   and invokes 'cruiseInstance.addRoom' to add the new room to the cruise. A confirmation message is displayed following a successful addition.</li>
     * </ul>
     *
     * <p>The dialog is modal and is linked to the provided parent {@code JFrame}.
     * The 'onSuccessCallback' {@code Runnable} is used to update the user interface or refreshing a room list immediately after a room is
     * successfully added.</p>
     *
     * @param parentFrame the parent {@code JFrame} to which this dialog is attached
     * @param cruiseInstance the {@code Cruise} object to which the new room will be added
     * @param onSuccessCallback a {@code Runnable} that is executed upon successful addition of the room
     */
    private static void openAddRoomDialog(JFrame parentFrame, Cruise cruiseInstance, Runnable onSuccessCallback) {
        JDialog addRoomDialog = new JDialog(parentFrame, "Add Room", true);
        addRoomDialog.setLayout(new GridLayout(7, 2));

        JLabel numOfBedsLabel = createStyledLabel("Number of Beds:", new Font("Arial", Font.PLAIN, 12));
        JComboBox<Integer> numOfBedsDropdown = new JComboBox<>(new Integer[]{1, 2, 3, 4});

        JLabel bedTypeLabel = createStyledLabel("Bed Type:", new Font("Arial", Font.PLAIN, 12));
        JComboBox<Room.BedType> bedTypeDropdown = new JComboBox<>(Room.BedType.values());

        JLabel qualityLabel = createStyledLabel("Room Quality:", new Font("Arial", Font.PLAIN, 12));
        JComboBox<Room.Quality> qualityDropdown = new JComboBox<>(Room.Quality.values());


        JLabel smokingLabel = createStyledLabel("Smoking:", new Font("Arial", Font.PLAIN, 12));
        JCheckBox smokingCheckbox = new JCheckBox("Is Smoking?");

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            //int roomId = Integer.parseInt(idField.getText());
            int numBeds = (int) numOfBedsDropdown.getSelectedItem();
            Room.BedType bedType = (Room.BedType) bedTypeDropdown.getSelectedItem();
            Room.Quality quality = (Room.Quality) qualityDropdown.getSelectedItem();
            boolean isSmoking = smokingCheckbox.isSelected();

            Room newRoom = new Room(0, numBeds, bedType, quality, isSmoking);

            boolean success = cruiseInstance.addRoom(newRoom);

            if (success){
                Component reservationFrame = null;
                JOptionPane.showMessageDialog(reservationFrame, "Room Added Successfully!",
                        "Room Status", JOptionPane.DEFAULT_OPTION,scaledSuccessIcon);
                onSuccessCallback.run();
            }
            addRoomDialog.dispose();
        });

        // Add components to the dialog
        addRoomDialog.add(numOfBedsLabel);
        addRoomDialog.add(numOfBedsDropdown);
        addRoomDialog.add(bedTypeLabel);
        addRoomDialog.add(bedTypeDropdown);
        addRoomDialog.add(qualityLabel);
        addRoomDialog.add(qualityDropdown);
        addRoomDialog.add(smokingLabel);
        addRoomDialog.add(smokingCheckbox);
        addRoomDialog.add(submitButton);

        addRoomDialog.pack();
        addRoomDialog.setLocationRelativeTo(parentFrame);
        addRoomDialog.setVisible(true);
    }
}
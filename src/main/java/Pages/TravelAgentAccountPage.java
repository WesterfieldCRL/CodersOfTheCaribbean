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

import static Pages.CruiseAppUtilities.*;
import static Person.Guest.usernameExists;
import static Person.TravelAgent.modifyTravelAgentAccount;


public class TravelAgentAccountPage {
    public static JTabbedPane createTravelAgentViewTabbedPane(JFrame frame){
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel addRoomPanel = createAddRoomPanel(frame);
        tabbedPane.addTab("Manage Rooms", null, addRoomPanel, "Add rooms and manage existing rooms");

        JPanel reserveForGuestPanel = createReserveForGuestPanel(frame);
        tabbedPane.addTab("Book For Guest", null, reserveForGuestPanel, "Reserve on behalf of guest");

        JPanel modifyAccountPanel = createModifyAccountPanel(frame);
        tabbedPane.addTab("Modify Account", null ,modifyAccountPanel, "Modify your account info");
        return tabbedPane;
    }

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
                    JOptionPane.showMessageDialog(frame, "Could not find cruise details", "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                }
            }
        });

        return panel;
    }

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
            public Component getListCellRendererComponent(JList<? extends Guest> list, Guest value, int index, boolean isSelected, boolean cellHasFocus) {
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
                    JOptionPane.showMessageDialog(frame, "Please Select A Guest To Continue.", "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                    return;
                }
                currentGuest = guestJList.getSelectedValue();
                addGuestBookToFrame(frame);
                switchToPanel(frame, "Guest View");
            });
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(bookButton, gbc);

        return panel;
    }

    public static void addGuestBookToFrame(JFrame frame) {
        JPanel guestPanel = GuestAccountPage.createRoomSelectionPanel(frame);
        frame.add(guestPanel, "Guest View");
        frame.revalidate();
        frame.repaint();
    }

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

        return panel;
    }

    private static void updateRoomListForCruise(String selectedCruise, DefaultListModel<String> roomListModel) {
        Optional<Cruise> optCruise = Cruise.getCruise(selectedCruise);
        optCruise.ifPresent(cruise -> {
            ArrayList<Room> rooms = cruise.getRoomList();//cruise.getRoomsList(LocalDate.now(), LocalDate.now().plusDays(1));
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



    //Runnable added for callback. Allows for room list to be updated immediately after the room is added. (Apparantly
    //better than how I was doing it on other pages).
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
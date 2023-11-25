package Pages;

import Cruise.Room;
import Cruise.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ArrayList;

import static Pages.CruiseAppUtilities.*;


public class TravelAgentAccountPage {
    public static JTabbedPane createTravelAgentViewTabbedPane(JFrame frame){
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel addRoomPanel = createAddRoomPanel(frame);
        tabbedPane.addTab("Manage Rooms", null, addRoomPanel, "Add rooms and manage existing rooms");

        JPanel reserveForGuestPanel = createReserveForGuestPanel(frame);
        tabbedPane.addTab("Book For Guest", null, reserveForGuestPanel, "Reserve on behalf of guest");

        JPanel modifyAccountPanel = createModifyAccountPanel(frame);
        tabbedPane.addTab("Modify Account", scaledErrorImage,modifyAccountPanel, "Modify your account info");
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

        JButton addRoomButton = createStyledButton("Add Room", new Font("Arial", Font.BOLD, 12), BUTTON_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addRoomButton, gbc);

        cruiseComboBox.addActionListener(e -> {
            String selectedCruise = (String) cruiseComboBox.getSelectedItem();
            if (selectedCruise != null) {
                Optional<Cruise> optCruise = Cruise.getCruise(selectedCruise);
                optCruise.ifPresent(cruise -> {
                    ArrayList<Room> rooms = cruise.getRoomsList(LocalDate.now(), LocalDate.now().plusDays(1));
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
        });

        addRoomButton.addActionListener(e -> {
            String selectedCruise = (String) cruiseComboBox.getSelectedItem();
            if (selectedCruise != null) {
                Optional<Cruise> optCruise = Cruise.getCruise(selectedCruise);
                if (optCruise.isPresent()) {
                    Cruise currentCruise = optCruise.get();
                    openAddRoomDialog(frame, currentCruise);
                } else {
                    JOptionPane.showMessageDialog(frame, "Could not find cruise details", "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                }
            }
        });

        return panel;
    }

    public static JPanel createReserveForGuestPanel(JFrame frame){
        JPanel panel = new JPanel(new GridBagLayout());

        return panel;
    }

    public static JPanel createModifyAccountPanel(JFrame frame){
        JPanel panel = new JPanel(new GridBagLayout());

        return panel;
    }



    private static void openAddRoomDialog(JFrame parentFrame, Cruise cruiseInstance) {
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
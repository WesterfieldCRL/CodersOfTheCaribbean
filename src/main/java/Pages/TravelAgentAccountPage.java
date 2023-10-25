package Pages;

import Cruise.Room;
import Cruise.*;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import static Pages.CruiseAppUtilities.*;


public class TravelAgentAccountPage {
    public static JPanel createTravelAgentViewPane(JFrame frame){
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Add a JComboBox for cruise selection
        String[] cruiseOptions = {"CRUISE1", "CRUISE2", "CRUISE3"};
        JComboBox<String> cruiseComboBox = new JComboBox<>(cruiseOptions);
        panel.add(cruiseComboBox, BorderLayout.CENTER);

        JButton addRoomButton = createStyledButton("Add Room", new Font("Arial", Font.BOLD, 14), Color.GREEN);
        addRoomButton.addActionListener(e -> {
            String selectedCruise = (String) cruiseComboBox.getSelectedItem();
            if (selectedCruise != null) {
                Optional<Cruise> optCruise = Cruise.getCruise(selectedCruise);
                if (optCruise.isPresent()) {
                    Cruise currentCruise = optCruise.get();
                    // Open the dialog to add a room
                    openAddRoomDialog(frame, currentCruise);
                } else {
                    JOptionPane.showMessageDialog(frame, "Could not find cruise details", "Error", JOptionPane.ERROR_MESSAGE, scaledErrorImage);
                }
            }
        });
        panel.add(addRoomButton, BorderLayout.SOUTH);

        return panel;
    }

    private static void openAddRoomDialog(JFrame parentFrame, Cruise cruiseInstance) {
        JDialog addRoomDialog = new JDialog(parentFrame, "Add Room", true);
        addRoomDialog.setLayout(new GridLayout(7, 2));

        // Components for Room ID
        //JLabel idLabel = createStyledLabel("Room ID:", new Font("Arial", Font.PLAIN, 12));
        //JTextField idField = new JTextField();

        // Components for Number of Beds
        JLabel numOfBedsLabel = createStyledLabel("Number of Beds:", new Font("Arial", Font.PLAIN, 12));
        JComboBox<Integer> numOfBedsDropdown = new JComboBox<>(new Integer[]{1, 2, 3, 4});

        // Components for Bed Type
        JLabel bedTypeLabel = createStyledLabel("Bed Type:", new Font("Arial", Font.PLAIN, 12));
        JComboBox<Room.BedType> bedTypeDropdown = new JComboBox<>(Room.BedType.values());

        // Components for Room Quality
        JLabel qualityLabel = createStyledLabel("Room Quality:", new Font("Arial", Font.PLAIN, 12));
        JComboBox<Room.Quality> qualityDropdown = new JComboBox<>(Room.Quality.values());

        // Components for Smoking Status
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

            cruiseInstance.addRoom(newRoom);

            addRoomDialog.dispose();
        });

        // Add components to the dialog
        //addRoomDialog.add(idLabel);
        //addRoomDialog.add(idField);
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

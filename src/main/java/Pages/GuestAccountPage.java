package Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import Cruise.Cruise;
import Cruise.Room;

import static Pages.CruiseAppUtilities.*;
import static Cruise.Cruise.*;


public class GuestAccountPage {
    private static final String CRUISE_FILE = "cruiseList.txt";  // Adjust this path

    public static JPanel createGuestViewPanel(JFrame frame) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        // Buttons for cruise search
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnCruise1 = new JButton("Cruise1");
        JButton btnCruise2 = new JButton("Cruise2");
        JButton btnCruise3 = new JButton("Cruise3");

//        JTextArea textArea = new JTextArea(10, 40); // adjust size as needed
//// add the text area to your frame or panel
//
//        btnCruise1.addActionListener((ActionEvent e) -> {
//            Cruise cruise = searchCruise(CRUISE_FILE, "cruise1");
//            StringBuilder displayInfo = new StringBuilder();
//            if (cruise != null) {
//                ArrayList<Room> rooms = cruise.getRoomList();
//                displayInfo.append("Rooms for Cruise1:\n");
//                for (Room room : rooms) {
//                    displayInfo.append(room.getRoomInfo()).append("\n\n");
//                }
//            } else {
//                displayInfo.append("Cruise1 not found.");
//            }
//            textArea.setText(displayInfo.toString());
//        });
        // Create the model and JList
        DefaultListModel<String> roomListModel = new DefaultListModel<>();
        JList<String> roomList = new JList<>(roomListModel);
        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // ensure single selection
        roomList.setEnabled(false); // disable item selection

        // Wrap the JList in a JScrollPane for scrolling
        JScrollPane scrollPane = new JScrollPane(roomList);

        btnCruise1.addActionListener((ActionEvent e) -> {
            Cruise cruise = searchCruise(CRUISE_FILE, "cruise1");
            roomListModel.clear(); // clear the previous data
            if (cruise != null) {
                ArrayList<Room> rooms = cruise.getRoomList();
                roomListModel.addElement("Rooms for Cruise1:");
                for (Room room : rooms) {
                    roomListModel.addElement(room.getRoomInfo());
                }
            } else {
                roomListModel.addElement("Cruise1 not found.");
            }
        });

        btnCruise2.addActionListener((ActionEvent e) -> {
            Cruise cruise = searchCruise(CRUISE_FILE, "cruise2");
            roomListModel.clear(); // clear the previous data
            if (cruise != null) {
                ArrayList<Room> rooms = cruise.getRoomList();
                roomListModel.addElement("Rooms for Cruise3:");
                for (Room room : rooms) {
                    roomListModel.addElement(room.getRoomInfo());
                }
            } else {
                roomListModel.addElement("Cruise3 not found.");
            }        });

        btnCruise3.addActionListener((ActionEvent e) -> {
            Cruise cruise = searchCruise(CRUISE_FILE, "cruise3");
            roomListModel.clear(); // clear the previous data
            if (cruise != null) {
                ArrayList<Room> rooms = cruise.getRoomList();
                roomListModel.addElement("Rooms for Cruise3:");
                for (Room room : rooms) {
                    roomListModel.addElement(room.getRoomInfo());
                }
            } else {
                roomListModel.addElement("Cruise3 not found.");
            }        });

        buttonPanel.add(btnCruise1);
        buttonPanel.add(btnCruise2);
        buttonPanel.add(btnCruise3);


        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane);

        return panel;
    }
}
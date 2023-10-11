import javax.swing.*;
import java.awt.*;

public class CruiseApp {
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel cruiseLabel;
    private JList<Room> roomsList;
    private JTextArea travelPathArea;

    public CruiseApp(Cruise cruise) {
        frame = new JFrame("Cruise Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        cruiseLabel = new JLabel("Cruise Name: " + cruise.getName());
        mainPanel.add(cruiseLabel);

        // Display rooms
        DefaultListModel<Room> listModel = new DefaultListModel<>();
        for (Room room : cruise.getRoomList()) {
            listModel.addElement(room);
        }
        roomsList = new JList<>(listModel);
        roomsList.setCellRenderer(new RoomRenderer());
        mainPanel.add(new JScrollPane(roomsList));

        // Display travel path
        travelPathArea = new JTextArea(cruise.getTravelPath().toString());
        travelPathArea.setEditable(false);
        mainPanel.add(new JScrollPane(travelPathArea));

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Custom list renderer for Room
    class RoomRenderer extends JLabel implements ListCellRenderer<Room> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Room> list, Room room, int index, boolean isSelected, boolean cellHasFocus) {
            setText("ID: " + room.getID() + ", Quality: " + room.getQuality() + ", Beds: " + room.getNumBeds() + ", Bed Type: " + room.getBedType());
            return this;
        }
    }

    public static void main(String[] args) {
        // Initialize Cruise object and pass to app
        Cruise cruise = new Cruise("Titanic"); // or your cruise object
        new CruiseApp(cruise);
    }
}

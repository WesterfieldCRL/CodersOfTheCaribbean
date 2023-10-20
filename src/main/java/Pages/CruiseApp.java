package Pages;

import javax.swing.*;
import java.awt.*;

import Cruise.Room;
import Cruise.Cruise;
import Person.Person;
import Person.*;

import java.awt.image.BufferedImage;
import java.util.Optional;

import static Pages.CruiseAppUtilities.*;
import static Pages.LandingPage.*;
import static Pages.LoginPage.*;
import static Pages.CreateAccountPage.*;

public class CruiseApp {

    //TODO: CLEAR TEXTS FIELDS AFTER SUBMIT

    public static void main(String[] args) {
        Cruise c = new Cruise("cool cruise");
        c.readCruiseRooms("cruise1.csv");
        Room room = new Room(1, 1, Room.BedType.KING, Room.Quality.ECONOMY, false, Room.RoomStatus.NOT_RESERVED);
        c.addRoom(room, "cruise1.csv");
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JFrame setupMainFrame() {
        JFrame frame = new JFrame("Cruise.Cruise Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new CardLayout());
        frame.setLocationRelativeTo(null);
        return frame;
    }


}
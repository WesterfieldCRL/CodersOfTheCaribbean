import Cruise.*;
import Person.*;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Person.Guest.Reservation;

import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CheckOutTest {

    /**
     * Runs before each test to backup the database
     *
     * <p>The method backs up the database before each test.</p>
     *
     * <ul>
     *   <li>Backs up the database.</li>
     * </ul>
     *
     * @throws SQLException if the database cannot be backed up
     */
    @BeforeEach
    public void backupDB()
    {
        try (Connection connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase")) {
            CallableStatement cs = connection.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)");
            cs.setString(1, "backup");
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Runs after each test to restore the database
     *
     * <p>The method restores the database after each test.</p>
     *
     * <ul>
     *   <li>Restores the database.</li>
     * </ul>
     */
    @AfterEach
    public void restoreDatabase() {
        try {
            DriverManager.getConnection("jdbc:derby:cruiseDatabase;shutdown=true");

        } catch (SQLException e) {
            //Shutting down db always produces SQLException
            //e.printStackTrace();
        }

        try {
            replaceDatabaseWithBackup();

            // Restart Derby
            DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Replaces the database with the backup
     *
     * <p>The method replaces the database with the backup.</p>
     *
     * <ul>
     *   <li>Replaces the database with the backup.</li>
     * </ul>
     */
    private void replaceDatabaseWithBackup() {
        try {
            File databaseDir = new File("cruiseDatabase");
            File backupDir = new File("backup/cruiseDatabase");

            FileUtils.copyDirectory(backupDir, databaseDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * JUnit test case for the check-out functionality in the reservation system.
     *
     * <p>This test method covers the following steps:</p>
     * <ol>
     *   <li>Creation of a {@link Guest} and a {@link TravelAgent} for testing purposes.</li>
     *   <li>Creation of a reservation for the guest using the travel agent.</li>
     *   <li>Check-in and check-out for the reservation.</li>
     *   <li>Verification of the successful check-out process.</li>
     * </ol>
     *
     * <p>The test includes the following scenarios:</p>
     * <ul>
     *   <li>Successfully finding and reserving an available room for the guest.</li>
     *   <li>Checking in and checking out for each reservation made by the guest.</li>
     *   <li>Ensuring the correctness of the check-out status for each reservation.</li>
     * </ul>
     *
     * <p>This test is crucial for validating the proper functioning of the check-out process and
     * the accuracy of the check-out status tracking in the reservation system.</p>
     */
    @Test
    public void testCheckOut() {
        Guest guest = new Guest("test", "test", "test", "test", "test");
        guest.createAccount();
        Optional<Cruise> optionalCruise = Cruise.getCruise("CRUISE1");
        if (optionalCruise.isPresent()) {
            Cruise cruise1 = optionalCruise.get();

            ArrayList<LocalDate> validDates = cruise1.getValidReservationDates();

            ArrayList<Room> roomList = cruise1.getAvailableRoomsList(validDates.get(0), validDates.get(validDates.size()-1));

            Optional<Room> optionalRoom = cruise1.isRoomAvailable(roomList.get(0).getQuality(), roomList.get(0).getNumBeds(), roomList.get(0).getBedType(), roomList.get(0).isSmoking(), validDates.get(0), validDates.get(validDates.size()-1));

            if (optionalRoom.isPresent())
            {
                Room room = optionalRoom.get();
                boolean output = guest.makeReservation(room, validDates.get(0), validDates.get(validDates.size()-1), cruise1);

                assertTrue(output);
                return;
            }
            fail("could not find room");
        }
        fail("could not find cruise");

        TravelAgent travelAgent = new TravelAgent("test", "test", "test", "test", "test");
        boolean result;

        for (Reservation r : guest.getReservations()) {
            result = travelAgent.checkIn(r.getId());
        }

        for (Reservation r : guest.getReservations()) {
            result = travelAgent.checkOut(r.getId());
            assertTrue(result);
        }
    }


    /**
     * JUnit test case for verifying the check-out status of reservations in the reservation system.
     *
     * <p>This test method covers the following steps:</p>
     * <ol>
     *   <li>Creation of a {@link Guest} and a {@link TravelAgent} for testing purposes.</li>
     *   <li>Creation of a reservation for the guest using the travel agent.</li>
     *   <li>Check-in and check-out for the reservation.</li>
     *   <li>Verification of the correct check-out status for each reservation.</li>
     * </ol>
     *
     * <p>The test includes the following scenarios:</p>
     * <ul>
     *   <li>Successfully finding and reserving an available room for the guest.</li>
     *   <li>Checking in and checking out for each reservation made by the guest.</li>
     *   <li>Ensuring the correctness of the check-out status for each reservation.</li>
     * </ul>
     *
     * <p>This test is essential for validating the accuracy of the check-out status tracking in the
     * reservation system and ensuring the consistency of the check-out process.</p>
     */
    @Test
    public void testCheckOutStatus() {
        Guest guest = new Guest("test", "test", "test", "test", "test");
        guest.createAccount();
        Optional<Cruise> optionalCruise = Cruise.getCruise("CRUISE1");
        if (optionalCruise.isPresent()) {
            Cruise cruise1 = optionalCruise.get();

            ArrayList<LocalDate> validDates = cruise1.getValidReservationDates();

            ArrayList<Room> roomList = cruise1.getAvailableRoomsList(validDates.get(0), validDates.get(validDates.size()-1));

            Optional<Room> optionalRoom = cruise1.isRoomAvailable(roomList.get(0).getQuality(), roomList.get(0).getNumBeds(), roomList.get(0).getBedType(), roomList.get(0).isSmoking(), validDates.get(0), validDates.get(validDates.size()-1));

            if (optionalRoom.isPresent())
            {
                Room room = optionalRoom.get();
                boolean output = guest.makeReservation(room, validDates.get(0), validDates.get(validDates.size()-1), cruise1);

                assertTrue(output);
                return;
            }
            fail("could not find room");
        }
        fail("could not find cruise");

        TravelAgent travelAgent = new TravelAgent("test", "test", "test", "test", "test");
        boolean result;

        for (Reservation r : guest.getReservations()) {
            result = travelAgent.checkIn(r.getId());
            result = travelAgent.checkOut(r.getId());
        }

        for (Reservation r : guest.getReservations()) {
            result = travelAgent.getCheckOutStatus(r.getId());
            assertTrue(result);
        }
    }
}


import Cruise.*;
import Person.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The {@code CancelReservationTest} class serves as a test driver for the application.
 *
 * <p>This class contains the test methods for canceling a reservation.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Canceling a reservation.</li>
 * </ul>
 */
public class CancelReservationTest {

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
     * Tests the successful cancellation of a reservation for a guest.
     *
     * <p>This method creates a new guest, registers the guest by creating an account, retrieves an existing cruise,
     * checks for available rooms, makes a reservation, cancels the reservation, and verifies the successful cancellation.</p>
     *
     * <p>The method follows these steps:</p>
     * <ol>
     *   <li>Creates a new guest with predefined personal information using the {@link Guest} constructor.</li>
     *   <li>Registers the guest by creating an account using the {@link Guest#createAccount()} method.</li>
     *   <li>Retrieves an existing cruise by its identifier ("CRUISE1") using the {@link Cruise#getCruise(String)} method.</li>
     *   <li>If the cruise is present, retrieves the valid reservation dates and available rooms for the cruise.</li>
     *   <li>Checks if an available room is present for reservation using the {@link Cruise#isRoomAvailable(Room.Quality, int, Room.BedType, boolean, LocalDate, LocalDate)} method.</li>
     *   <li>If a room is available, makes a reservation for the guest using the {@link Guest#makeReservation(Room, LocalDate, LocalDate, Cruise)} method.</li>
     *   <li>Gets the list of reservations for the guest and cancels the first reservation using the {@link Guest#cancelReservation(int)} method.</li>
     *   <li>Verifies that the cancellation is successful by checking the boolean output of the cancellation operation.</li>
     *   <li>If the cancellation is successful, the test passes; otherwise, it fails.</li>
     * </ol>
     *
     * <p>Note: The method relies on the functionality of the {@link Guest}, {@link Cruise}, and {@link Room} classes.
     * It assumes a specific structure for reservations and may print error messages if certain conditions are not met.</p>
     */
    @Test
    void testCancelReservationSuccess() {
        Guest guest = new Guest("test", "test", "test", "test", "test");
        guest.createAccount();
        Optional<Cruise> optionalCruise = Cruise.getCruise("CRUISE1");
        if (optionalCruise.isPresent())
        {
            Cruise cruise1 = optionalCruise.get();

            ArrayList<LocalDate> validDates = cruise1.getValidReservationDates();

            ArrayList<Room> roomList = cruise1.getAvailableRoomsList(validDates.get(0), validDates.get(validDates.size()-1));

            Optional<Room> optionalRoom = cruise1.isRoomAvailable(roomList.get(0).getQuality(), roomList.get(0).getNumBeds(), roomList.get(0).getBedType(), roomList.get(0).isSmoking(), validDates.get(0), validDates.get(validDates.size()-1));

            if (optionalRoom.isPresent())
            {
                Room room = optionalRoom.get();
                guest.makeReservation(room, validDates.get(0), validDates.get(validDates.size()-1), cruise1);

                List<Guest.Reservation> reservations = guest.getReservations();

                boolean output = guest.cancelReservation(reservations.get(0).getId());

                assertTrue(output);
                return;
            }
            fail("could not find room");
        }
        fail("could not find cruise");
    }

    /**
     * Tests the unsuccessful cancellation of a reservation for a guest.
     *
     * <p>This method creates a new guest, registers the guest by creating an account, retrieves an existing cruise,
     * checks for available rooms, makes a reservation, sets the clock to a future date, and attempts to cancel the reservation.</p>
     *
     * <p>The method follows these steps:</p>
     * <ol>
     *   <li>Creates a new guest with predefined personal information using the {@link Guest} constructor.</li>
     *   <li>Registers the guest by creating an account using the {@link Guest#createAccount()} method.</li>
     *   <li>Retrieves an existing cruise by its identifier ("CRUISE1") using the {@link Cruise#getCruise(String)} method.</li>
     *   <li>If the cruise is present, retrieves the valid reservation dates and available rooms for the cruise.</li>
     *   <li>Checks if an available room is present for reservation using the {@link Cruise#isRoomAvailable(Room.Quality, int, Room.BedType, boolean, LocalDate, LocalDate)} method.</li>
     *   <li>If a room is available, makes a reservation for the guest using the {@link Guest#makeReservation(Room, LocalDate, LocalDate, Cruise)} method.</li>
     *   <li>Sets the clock to a future date using the {@link Guest#setClock(Clock)} method.</li>
     *   <li>Gets the list of reservations for the guest and attempts to cancel the first reservation using the {@link Guest#cancelReservation(int)} method.</li>
     *   <li>Verifies that the cancellation is unsuccessful by checking the boolean output of the cancellation operation.</li>
     *   <li>If the cancellation is unsuccessful, the test passes; otherwise, it fails.</li>
     * </ol>
     *
     * <p>Note: The method relies on the functionality of the {@link Guest}, {@link Cruise}, and {@link Room} classes.
     * It assumes a specific structure for reservations and may print error messages if certain conditions are not met.</p>
     */
    @Test
    void testCancelReservationFailure() {
        Guest guest = new Guest("test", "test", "test", "test", "test");
        guest.createAccount();
        Optional<Cruise> optionalCruise = Cruise.getCruise("CRUISE1");
        if (optionalCruise.isPresent())
        {
            Cruise cruise1 = optionalCruise.get();

            ArrayList<LocalDate> validDates = cruise1.getValidReservationDates();

            ArrayList<Room> roomList = cruise1.getAvailableRoomsList(validDates.get(0), validDates.get(validDates.size()-1));

            Optional<Room> optionalRoom = cruise1.isRoomAvailable(roomList.get(0).getQuality(), roomList.get(0).getNumBeds(), roomList.get(0).getBedType(), roomList.get(0).isSmoking(), validDates.get(0), validDates.get(validDates.size()-1));

            if (optionalRoom.isPresent())
            {
                Room room = optionalRoom.get();
                guest.makeReservation(room, validDates.get(0), validDates.get(validDates.size()-1), cruise1);

                LocalDateTime fixedDateTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
                Clock fixedClock = Clock.fixed(fixedDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

                guest.setClock(fixedClock);
                List<Guest.Reservation> reservations = guest.getReservations();

                boolean output = guest.cancelReservation(reservations.get(0).getId());

                assertFalse(output);
                return;
            }
            fail("could not find room");
        }
        fail("could not find cruise");
    }
}

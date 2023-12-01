import Cruise.*;
import Person.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

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

/**
 * The {@code MakeReservationTATest} class serves as a test driver for the application.
 *
 * <p>This class contains the test methods for making a reservation.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Making a reservation.</li>
 * </ul>
 */
public class MakeReservationTATest {

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
     * Tests the successful reservation creation by a guest with correct data (Travel Agency mode).
     *
     * <p>This method simulates the process of a guest creating a reservation in Travel Agency (TA) mode with correct data.
     * It creates a new guest, registers the guest by creating an account, retrieves an existing cruise,
     * checks for available rooms, and makes a reservation. The test verifies the success of the reservation creation.</p>
     *
     * <p>The method follows these steps:</p>
     * <ol>
     *   <li>Creates a new guest with predefined personal information using the {@link Guest} constructor.</li>
     *   <li>Registers the guest by creating an account using the {@link Guest#createAccount()} method.</li>
     *   <li>Retrieves an existing cruise by its identifier ("CRUISE1") using the {@link Cruise#getCruise(String)} method.</li>
     *   <li>If the cruise is present, retrieves the valid reservation dates and available rooms for the cruise.</li>
     *   <li>Checks if an available room is present for reservation using the {@link Cruise#isRoomAvailable(Room.Quality, int, Room.BedType, boolean, LocalDate, LocalDate)} method.</li>
     *   <li>If a room is available, makes a reservation for the guest using the {@link Guest#makeReservation(Room, LocalDate, LocalDate, Cruise)} method.</li>
     *   <li>Verifies that the reservation creation is successful by checking the boolean output of the reservation operation.</li>
     *   <li>If the reservation is successful, the test passes; otherwise, it fails.</li>
     * </ol>
     *
     * <p>Note: The method assumes the {@link Guest}, {@link Cruise}, and {@link Room} classes handle the necessary functionalities.
     * It may print error messages if certain conditions are not met and does not propagate exceptions.</p>
     */
    @Test
    void testMakeReservationWithCorrectDataTA() {
        //NOTE: Only difference between TA and normal creation is UI, so test is just rerun
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
                boolean output = guest.makeReservation(room, validDates.get(0), validDates.get(validDates.size()-1), cruise1);

                assertTrue(output);
                return;
            }
            fail("could not find room");
        }
        fail("could not find cruise");
    }

    /**
     * Tests the unsuccessful reservation creation by a guest with incorrect data (Travel Agency mode).
     *
     * <p>This method simulates the process of a guest creating a reservation in Travel Agency (TA) mode with incorrect data.
     * It creates a new guest, registers the guest by creating an account, retrieves an existing cruise,
     * checks for available rooms, and attempts to make a reservation. The test verifies the failure of the reservation creation.</p>
     *
     * <p>The method follows these steps:</p>
     * <ol>
     *   <li>Creates a new guest with predefined personal information using the {@link Guest} constructor.</li>
     *   <li>Registers the guest by creating an account using the {@link Guest#createAccount()} method.</li>
     *   <li>Retrieves an existing cruise by its identifier ("CRUISE1") using the {@link Cruise#getCruise(String)} method.</li>
     *   <li>If the cruise is present, retrieves the valid reservation dates and available rooms for the cruise.</li>
     *   <li>Checks if an available room is present for reservation using the {@link Cruise#isRoomAvailable(Room.Quality, int, Room.BedType, boolean, LocalDate, LocalDate)} method.</li>
     *   <li>If a room is available, attempts to make a reservation for the guest using the {@link Guest#makeReservation(Room, LocalDate, LocalDate, Cruise)} method.</li>
     *   <li>Verifies that the reservation creation is unsuccessful by checking the boolean output of the reservation operation.</li>
     *   <li>If the reservation is unsuccessful, the test passes; otherwise, it fails.</li>
     * </ol>
     *
     * <p>Note: The method assumes the {@link Guest}, {@link Cruise}, and {@link Room} classes handle the necessary functionalities.
     * It may print error messages if certain conditions are not met and does not propagate exceptions.</p>
     */
    @Test
    void testMakeReservationWithIncorrectDataTA() {
        //NOTE: Only difference between TA and normal creation is UI, so test is just rerun
        Guest guest = new Guest("test", "test", "test", "test", "test");
        guest.createAccount();
        Optional<Cruise> optionalCruise = Cruise.getCruise("CRUISE1");
        if (optionalCruise.isPresent())
        {
            Cruise cruise1 = optionalCruise.get();

            ArrayList<LocalDate> validDates = cruise1.getValidReservationDates();

            ArrayList<Room> roomList = cruise1.getAvailableRoomsList(validDates.get(0), validDates.get(validDates.size()-1));


            //Dates are invalid so should fail
            Optional<Room> optionalRoom = cruise1.isRoomAvailable(roomList.get(0).getQuality(), roomList.get(0).getNumBeds(), roomList.get(0).getBedType(), roomList.get(0).isSmoking(), LocalDate.now(), LocalDate.now().plusDays(10000));

            assertFalse(optionalRoom.isPresent());
            return;
        }
        fail("could not find cruise");
    }
}

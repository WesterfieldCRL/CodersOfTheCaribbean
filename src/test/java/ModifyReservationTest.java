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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * The {@code ModifyReservationTest} class serves as a test driver for the application.
 *
 * <p>This class contains the test methods for modifying a reservation.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Modifying a reservation.</li>
 * </ul>
 */
public class ModifyReservationTest {

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
     * Tests modifying a reservation with valid information
     *
     * <p>The method tests modifying a reservation with valid information.</p>
     *
     * <ul>
     *   <li>Creates a guest.</li>
     *   <li>Creates a cruise.</li>
     *   <li>Creates a reservation.</li>
     *   <li>Modifies the reservation.</li>
     *   <li>Checks if the reservation was modified.</li>
     * </ul>
     */
    @Test
    void testModifyValidInfo() {
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

                boolean output = guest.modifyReservation(reservations.get(0).getId(), roomList.get(1), validDates.get(0), validDates.get(validDates.size()-1), cruise1);
                assertTrue(output);
                return;
            }
            fail("room not found");
        }
        fail("cruise not found");
    }

    /**
     * Tests modifying a reservation with invalid information
     *
     * <p>The method tests modifying a reservation with invalid information.</p>
     *
     * <ul>
     *   <li>Creates a guest.</li>
     *   <li>Creates a cruise.</li>
     *   <li>Creates a reservation.</li>
     *   <li>Modifies the reservation.</li>
     *   <li>Checks if the reservation was modified.</li>
     * </ul>
     */
    @Test
    void testModifyInvalidInfo() {
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


                boolean output = guest.modifyReservation(reservations.get(0).getId(), roomList.get(1), validDates.get(0), LocalDate.now().plusDays(1000000), cruise1);
                assertFalse(output);
                return;
            }
            fail("room not found");
        }
        fail("cruise not found");
    }
}

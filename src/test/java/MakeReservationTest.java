import org.apache.commons.io.FileUtils;
import org.apache.derby.iapi.services.io.FileUtil;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import Cruise.*;
import Person.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

/**
 * The {@code MakeReservationTest} class serves as a test driver for the application.
 *
 * <p>This class contains the test methods for making a reservation.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Making a reservation.</li>
 * </ul>
 */
public class MakeReservationTest {

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
     * Tests making a reservation with correct data
     *
     * <p>The method tests making a reservation with correct data.</p>
     *
     * <ul>
     *   <li>Creates a guest.</li>
     *   <li>Gets a cruise.</li>
     *   <li>Gets valid dates.</li>
     *   <li>Gets available rooms.</li>
     *   <li>Checks if room is available.</li>
     *   <li>Makes a reservation.</li>
     *   <li>Checks if reservation was made.</li>
     * </ul>
     */
    @Test
    void testMakeReservationWithCorrectData() {
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
     * Tests making a reservation with incorrect data
     *
     * <p>The method tests making a reservation with incorrect data.</p>
     *
     * <ul>
     *   <li>Creates a guest.</li>
     *   <li>Gets a cruise.</li>
     *   <li>Gets valid dates.</li>
     *   <li>Gets available rooms.</li>
     *   <li>Checks if room is available.</li>
     *   <li>Makes a reservation.</li>
     *   <li>Checks if reservation was made.</li>
     * </ul>
     */
    @Test
    void testMakeReservationWithIncorrectData() {
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

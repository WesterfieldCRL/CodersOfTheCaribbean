import Person.*;
import Cruise.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
 * The {@code RoomAvailabilityCheckTest} class serves as a test driver for the application.
 *
 * <p>This class contains the test methods for checking room availability.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Checking room availability.</li>
 * </ul>
 */
public class RoomAvailabilityCheckTest {

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
     * Tests the successful room availability check for a specific room configuration.
     *
     * <p>This method simulates the process of checking room availability for a specific room configuration on a cruise.
     * It retrieves an existing cruise, adds a new room with predefined attributes to the cruise, and checks for the
     * availability of a room matching the specified criteria. The test verifies the success of the room availability check.</p>
     *
     * <p>The method follows these steps:</p>
     * <ol>
     *   <li>Retrieves an existing cruise by its identifier ("CRUISE1") using the {@link Cruise#getCruise(String)} method.</li>
     *   <li>If the cruise is present, adds a new room to the cruise with predefined attributes using the {@link Room} constructor and {@link Cruise#addRoom(Room)} method.</li>
     *   <li>Retrieves the valid reservation dates for the cruise using the {@link Cruise#getValidReservationDates()} method.</li>
     *   <li>Checks if a room matching the specified criteria is available for reservation using the {@link Cruise#isRoomAvailable(Room.Quality, int, Room.BedType, boolean, LocalDate, LocalDate)} method.</li>
     *   <li>Verifies that the room availability check is successful by checking the presence of the optional room.</li>
     *   <li>If the room is available, the test passes; otherwise, it fails.</li>
     * </ol>
     *
     * <p>Note: The method assumes the {@link Cruise} and {@link Room} classes handle the necessary functionalities.
     * It may print error messages if certain conditions are not met and does not propagate exceptions.</p>
     */
    @Test
    void testRoomAvailabilityCheckSuccess() {
        Optional<Cruise> cruiseOptional = Cruise.getCruise("CRUISE1");

        if (cruiseOptional.isPresent())
        {
            Cruise cruise1 = cruiseOptional.get();
            Room newRoom = new Room(0, -1, Room.BedType.FULL, Room.Quality.BUSINESS, false);
            cruise1.addRoom(newRoom);

            ArrayList<LocalDate> validDates = cruise1.getValidReservationDates();

            Optional<Room> optionalRoom = cruise1.isRoomAvailable(Room.Quality.BUSINESS, -1, Room.BedType.FULL, false, validDates.get(0), validDates.get(validDates.size()-1));

            assertTrue(optionalRoom.isPresent());
            return;
        }
        fail();
    }

    /**
     * Tests the unsuccessful room availability check for a specific room configuration.
     *
     * <p>This method simulates the process of checking room availability for a specific room configuration on a cruise.
     * It retrieves an existing cruise, adds a new room with predefined attributes to the cruise, and checks for the
     * availability of a room matching the specified criteria. The test verifies the failure of the room availability check.</p>
     *
     * <p>The method follows these steps:</p>
     * <ol>
     *   <li>Retrieves an existing cruise by its identifier ("CRUISE1") using the {@link Cruise#getCruise(String)} method.</li>
     *   <li>If the cruise is present, adds a new room to the cruise with predefined attributes using the {@link Room} constructor and {@link Cruise#addRoom(Room)} method.</li>
     *   <li>Retrieves the valid reservation dates for the cruise using the {@link Cruise#getValidReservationDates()} method.</li>
     *   <li>Checks if a room matching the specified criteria is available for reservation using the {@link Cruise#isRoomAvailable(Room.Quality, int, Room.BedType, boolean, LocalDate, LocalDate)} method.</li>
     *   <li>Verifies that the room availability check is unsuccessful by checking the absence of the optional room.</li>
     *   <li>If the room is unavailable, the test passes; otherwise, it fails.</li>
     * </ol>
     *
     * <p>Note: The method assumes the {@link Cruise} and {@link Room} classes handle the necessary functionalities.
     * It may print error messages if certain conditions are not met and does not propagate exceptions.</p>
     */
    @Test
    void testRoomAvailabilityCheckFailure() {
        Optional<Cruise> cruiseOptional = Cruise.getCruise("CRUISE1");

        if (cruiseOptional.isPresent())
        {
            Cruise cruise1 = cruiseOptional.get();
            Room newRoom = new Room(0, -1, Room.BedType.FULL, Room.Quality.BUSINESS, false);
            cruise1.addRoom(newRoom);

            ArrayList<LocalDate> validDates = cruise1.getValidReservationDates();

            Optional<Room> optionalRoom = cruise1.isRoomAvailable(Room.Quality.BUSINESS, -1, Room.BedType.FULL, false, validDates.get(0), LocalDate.now().plusDays(10000));

            assertFalse(optionalRoom.isPresent());
            return;
        }
        fail();
    }
}

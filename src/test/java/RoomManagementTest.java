import Cruise.*;
import Person.*;
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
 * The {@code RoomManagementTest} class serves as a test driver for the application.
 *
 * <p>This class contains the test methods for modifying a room.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Modifying a room.</li>
 * </ul>
 */
public class RoomManagementTest {

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
     * Tests the successful modification of a room within a cruise.
     *
     * <p>This method simulates the process of modifying a room within an existing cruise.
     * It retrieves an existing cruise, gets the list of rooms, selects a room to modify,
     * changes its number of beds, and then tests the success of the room modification using
     * the {@link Cruise#modifyRoom(Room)} method.</p>
     *
     * <p>The method follows these steps:</p>
     * <ol>
     *   <li>Retrieves an existing cruise by its identifier ("CRUISE1") using the {@link Cruise#getCruise(String)} method.</li>
     *   <li>If the cruise is present, retrieves the list of rooms for the cruise using the {@link Cruise#getRoomList()} method.</li>
     *   <li>Selects a room to modify from the list of rooms (in this case, the first room) and changes its number of beds using the {@link Room#setNumBeds(int)} method.</li>
     *   <li>Tests the success of modifying the room within the cruise using the {@link Cruise#modifyRoom(Room)} method.</li>
     *   <li>Verifies that the room modification is successful by checking the boolean output of the modification operation.</li>
     *   <li>If the modification is successful, the test passes; otherwise, it fails.</li>
     * </ol>
     *
     * <p>Note: The method assumes the {@link Cruise} and {@link Room} classes handle the necessary functionalities.
     * It may print error messages if certain conditions are not met and does not propagate exceptions.</p>
     */
    @Test
    void testRoomManagementSuccess() {
        Optional<Cruise> cruiseOptional = Cruise.getCruise("CRUISE1");

        if (cruiseOptional.isPresent())
        {
            Cruise cruise1 = cruiseOptional.get();
            ArrayList<Room> rooms = cruise1.getRoomList();
            Room modifyRoom = rooms.get(0);

            modifyRoom.setNumBeds(200);
            boolean output = cruise1.modifyRoom(modifyRoom);
            assertTrue(output);
            return;
        }
        fail();
    }

    /**
     * Tests the unsuccessful modification of a room within a cruise.
     *
     * <p>This method simulates the process of modifying a room within an existing cruise.
     * It retrieves an existing cruise, gets the list of rooms, selects a room to modify,
     * changes its ID to an invalid value, and then tests the failure of the room modification using
     * the {@link Cruise#modifyRoom(Room)} method.</p>
     *
     * <p>The method follows these steps:</p>
     * <ol>
     *   <li>Retrieves an existing cruise by its identifier ("CRUISE1") using the {@link Cruise#getCruise(String)} method.</li>
     *   <li>If the cruise is present, retrieves the list of rooms for the cruise using the {@link Cruise#getRoomList()} method.</li>
     *   <li>Selects a room to modify from the list of rooms (in this case, the first room) and changes its ID to an invalid value using the {@link Room#setID(int)} method.</li>
     *   <li>Tests the failure of modifying the room within the cruise using the {@link Cruise#modifyRoom(Room)} method.</li>
     *   <li>Verifies that the room modification is unsuccessful by checking the boolean output of the modification operation.</li>
     *   <li>If the modification is unsuccessful, the test passes; otherwise, it fails.</li>
     * </ol>
     *
     * <p>Note: The method assumes the {@link Cruise} and {@link Room} classes handle the necessary functionalities.
     * It may print error messages if certain conditions are not met and does not propagate exceptions.</p>
     */
    @Test
    void testRoomManagementFailure() {
        Optional<Cruise> cruiseOptional = Cruise.getCruise("CRUISE1");

        if (cruiseOptional.isPresent())
        {
            Cruise cruise1 = cruiseOptional.get();
            ArrayList<Room> rooms = cruise1.getRoomList();
            Room modifyRoom = rooms.get(0);

            modifyRoom.setID(-1);
            boolean output = cruise1.modifyRoom(modifyRoom);
            assertFalse(output);
            return;
        }
        fail();
    }
}

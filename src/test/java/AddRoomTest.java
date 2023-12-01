import Cruise.*;
import Person.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The {@code AddRoomTest} class serves as a test driver for the application.
 *
 * <p>This class contains the test methods for adding a room to a cruise.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Adding a room to a cruise.</li>
 * </ul>
 */
public class AddRoomTest {

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
     *
     * @throws SQLException if the database cannot be restored
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
     * Tests the addition of a room to a cruise and verifies the database update.
     *
     * <p>This method creates a new room with specified characteristics, adds it to an existing cruise,
     * and checks whether the database is updated accordingly. The test fails if the room addition is
     * unsuccessful or if the database is not updated as expected.</p>
     *
     * <p>The method follows these steps:</p>
     * <ol>
     *   <li>Retrieves an existing cruise by its identifier ("CRUISE1") using the {@link Cruise#getCruise(String)} method.</li>
     *   <li>If the cruise is present, creates a new room with predefined attributes (quality, bed type, smoking status, and number of beds).</li>
     *   <li>Adds the created room to the cruise using the {@link Cruise#addRoom(Room)} method.</li>
     *   <li>Connects to the database and queries for the added room based on its bed number.</li>
     *   <li>Verifies whether the database contains the added room by checking the result set.</li>
     *   <li>If the room is found, the test passes; otherwise, it fails.</li>
     * </ol>
     *
     * <p>Note: The method uses an embedded Derby database and assumes a specific table structure for the cruise data.
     * Also, the test may print stack traces in case of exceptions but does not propagate them, as this is a JUnit test.</p>
     */
    @Test
    void testAddRoom() {

        Optional<Cruise> optionalCruise = Cruise.getCruise("CRUISE1");

        if (optionalCruise.isPresent())
        {
            Cruise cruise = optionalCruise.get();
            Room room = new Room();
            room.setQuality(Room.Quality.BUSINESS);
            room.setBedType(Room.BedType.FULL);
            room.setSmoking(false);
            room.setNumBeds(200);

            cruise.addRoom(room);

            Connection connection = null;

            try {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

                connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM CRUISE1 WHERE BEDNUMBER = ?"); //Ignore error, works fine

                statement.setInt(1, 200);

                ResultSet rs = statement.executeQuery();
                if (rs.next())
                {
                    assertTrue(true);
                    return;
                }

            } catch (ClassNotFoundException | SQLException e)
            {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null)
                    {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        fail();

    }
}

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

import Cruise.*;
import Person.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The {@code GuestRegistrationTest} class serves as a test driver for the application.
 *
 * <p>This class contains the test methods for registering a guest.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Registering a guest.</li>
 * </ul>
 */
public class GuestRegistrationTest {
    
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
     * Tests the successful registration of a guest with valid information.
     *
     * <p>This method creates a new guest with valid personal information and tests the registration process
     * by calling the {@link Guest#createAccount()} method.</p>
     *
     * <p>The method follows these steps:</p>
     * <ol>
     *   <li>Creates a new guest with predefined valid personal information using the {@link Guest} constructor.</li>
     *   <li>Attempts to register the guest by creating an account using the {@link Guest#createAccount()} method.</li>
     *   <li>Verifies that the registration is successful by checking the boolean output of the registration operation.</li>
     *   <li>If the registration is successful, the test passes; otherwise, it fails.</li>
     * </ol>
     *
     * <p>Note: The method assumes that the {@link Guest} class handles the registration process and returns a boolean
     * indicating the success of the operation. It does not propagate exceptions and may print stack traces in case of errors.</p>
     */
    @Test
    void testRegisterWithValidInfo() {
        Guest guest = new Guest("test", "test", "test", "test", "test");
        boolean output = guest.createAccount();
        assertTrue(output);
    }

    /**
     * Tests the unsuccessful registration of a guest with invalid information.
     *
     * <p>This method creates a new guest with invalid personal information and tests the registration process
     * by calling the {@link Guest#createAccount()} method.</p>
     *
     * <p>The method follows these steps:</p>
     * <ol>
     *   <li>Creates a new guest with predefined invalid personal information using the {@link Guest} constructor.</li>
     *   <li>Attempts to register the guest by creating an account using the {@link Guest#createAccount()} method.</li>
     *   <li>Verifies that the registration is unsuccessful by checking the boolean output of the registration operation.</li>
     *   <li>If the registration is unsuccessful, the test passes; otherwise, it fails.</li>
     * </ol>
     *
     * <p>Note: The method assumes that the {@link Guest} class handles the registration process and returns a boolean
     * indicating the success of the operation. It does not propagate exceptions and may print stack traces in case of errors.</p>
     */
    @Test
    void testRegisterWithInvalidInfo() {
        Guest guest = new Guest("", "", "", "", "");
        boolean output = guest.createAccount();
        assertFalse(output);
    }
}

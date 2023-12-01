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
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * The {@code PasswordResetTest} class serves as a test driver for the application.
 *
 * <p>This class contains the test methods for resetting a password.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Resetting a password.</li>
 * </ul>
 */
public class PasswordResetTest {

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
     * Tests the reset password functionality with invalid information
     *
     * <p>The method tests the reset password functionality.</p>
     *
     * <ul>
     *   <li>Creates a guest.</li>
     *   <li>Requests a password reset.</li>
     *   <li>Creates an admin.</li>
     *   <li>Gets the reset requests.</li>
     *   <li>Attempts to reset the password.</li>
     *   <li>Attempts to login with the old password.</li>
     *   <li>Asserts that the login was unsuccessful.</li>
     * </ul>
     */
    @Test
    void testResetFailure() {
        Guest guest = new Guest("test", "test", "test", "test", "test");
        guest.createAccount();

        Guest.resetRequest("test", "newPassword");

        Admin admin = new Admin("testAdmin", "test", "test", "test", "test");
        admin.createAccount();

        Collection<Guest> guests = admin.getResetRequests();

        for (Guest thing : guests)
        {
            if (thing.getUsername().equals("test"))
            {
                thing.resetPassword();
            }
        }

        Optional<Person> newGuest = Person.login("test", "test");

        assertFalse(newGuest.isPresent());
    }

    /**
     * Tests the reset password functionality with valid information
     *
     * <p>The method tests the reset password functionality.</p>
     *
     * <ul>
     *   <li>Creates a guest.</li>
     *   <li>Requests a password reset.</li>
     *   <li>Creates an admin.</li>
     *   <li>Gets the reset requests.</li>
     *   <li>Resets the password.</li>
     *   <li>Attempts to login with the new password.</li>
     *   <li>Asserts that the login was successful.</li>
     * </ul>
     */
    @Test
    void testResetSuccess() {
        Guest guest = new Guest("test", "test", "test", "test", "test");
        guest.createAccount();

        Guest.resetRequest("test", "newPassword");

        Admin admin = new Admin("testAdmin", "test", "test", "test", "test");
        admin.createAccount();

        Collection<Guest> guests = admin.getResetRequests();

        for (Guest thing : guests)
        {
            if (thing.getUsername().equals("test"))
            {
                thing.resetPassword();
            }
        }

        Optional<Person> newGuest = Person.login("test", "newPassword");

        assertTrue(newGuest.isPresent());


    }
}

import Billing.Expenses;
import Person.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Cruise.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for billing generation functionality.
 *
 * <p>This class contains test methods to verify the billing generation process. It utilizes a
 * database backup and restore mechanism to ensure a consistent state for each test.</p>
 *
 */
public class BillingGenerationTest {
    /**
     * Replaces the database with the backup
     *
     * <p>The method replaces the database with the backup.</p>
     *
     * <ul>
     *   <li>Replaces the database with the backup.</li>
     * </ul>
     */
    @BeforeEach
    public void replaceDatabaseWithBackup() {
        try {
            File databaseDir = new File("cruiseDatabase");
            File backupDir = new File("backup/cruiseDatabase");

            FileUtils.copyDirectory(backupDir, databaseDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
     * Test case for generating billing.
     *
     * <p>This test creates a temporary guest, generates a billing, and verifies the billing
     * information in the database.</p>
     *
     * <ul>
     *   <li>Creates a temporary guest.</li>
     *   <li>Generates billing with a specified amount.</li>
     *   <li>Verifies the billing information in the database.</li>
     * </ul>
     */
    @Test
    void generateBillingTest(){
        Guest temp = new Guest("testing", "0", "String name", "String" , "String email");
        temp.createAccount();
        temp.generateBilling(6.90);

        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement selectQuery = connection.prepareStatement(
                    "SELECT * FROM BILLS WHERE GUEST = ?");

            selectQuery.setString(1, "testing");

            ResultSet rs = selectQuery.executeQuery();

            rs.next();
            String guestName = rs.getString("GUEST");
            Double amount = rs.getDouble("AMOUNT");
            LocalDate date = rs.getDate("DATE").toLocalDate();


            String error = rs.getString("ERROR_DESCRIPTION");

            assertEquals(guestName, "testing");
            LocalDateTime currentDateTime = LocalDateTime.now();


            LocalDate currentDate = currentDateTime.toLocalDate();
            assertEquals(date, currentDate);
            assertEquals(amount,6.90);
            assertEquals("", error);

            return;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        fail();
    }



}

import Billing.*;
import Person.Guest;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the {@link Expenses} class.
 */
public class ProvideExpenseSummaryTest {
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
    private Expenses expenses;
    /**
     * Initializes the {@link Expenses} object before each test.
     */
    @BeforeEach
    void setUp() {
        expenses = new Expenses();
    }
    /**
     * Tests the {@link Expenses#setPrice(Double)} and {@link Expenses#getPrice()} methods.
     */
    @Test
    void testSetPrice() {
        expenses.setPrice(100.0);
        assertEquals(100.0, expenses.getPrice());
    }
    /**
     * Tests the {@link Expenses#setName(String)} and {@link Expenses#getName()} methods.
     */
    @Test
    void testSetName() {
        expenses.setName("Groceries");
        assertEquals("Groceries", expenses.getName());
    }
    /**
     * Tests the {@link Expenses#setDate(LocalDate)} and {@link Expenses#getDate()} methods.
     */
    @Test
    void testSetDate() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        expenses.setDate(date);
        assertEquals(date, expenses.getDate());
    }
    /**
     * Tests the {@link Expenses#updateError(String, int)} method.
     */
    @Test
    void testUpdateError() {
        assertTrue(expenses.updateError("New error description", 1));
    }
    /**
     * Tests the {@link Expenses#removeError(int)} method.
     */
    @Test
    void testRemoveError() {
        Guest guest = new Guest("testing", "0", "String name", "String" , "String email");
        guest.createAccount();
        guest.generateBilling(6.90);

        assertTrue(expenses.removeError(1));
    }
    /**
     * Tests the {@link Expenses#getError(int)} and {@link Expenses#getErrorDescription()} methods.
     */
    @Test
    void testGetError() {
        Guest guest = new Guest("testing", "0", "String name", "String" , "String email");
        guest.createAccount();
        guest.generateBilling(6.90);

        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM BILLS WHERE GUEST = ?");
            statement.setString(1, "testing");

            ResultSet rs = statement.executeQuery();
            rs.next();
            int i = rs.getInt("ID");
            expenses.getError(i);
            assertEquals(expenses.getErrorDescription(), "");
            return;
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
        fail("oopsie");

    }
    /**
     * Tests the {@link Expenses#getExpenses()} method.
     */
    @Test
    void testGetExpenses() {

        List<Expenses> expenseList = expenses.getExpenses();
        assertNotNull(expenseList);
        assertFalse(expenseList.isEmpty());
    }
    /**
     * Tests the {@link Expenses#getId()} method.
     */
    @Test
    void testGetId() {
        expenses = new Expenses(1, "Shopping", LocalDate.now(), 50.0, "No errors");
        assertEquals(1, expenses.getId());
    }
}

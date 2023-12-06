import Billing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
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

        assertTrue(expenses.removeError(1));
    }
    /**
     * Tests the {@link Expenses#getError(int)} and {@link Expenses#getErrorDescription()} methods.
     */
    @Test
    void testGetError() {

        assertTrue(expenses.getError(1));
        assertNotNull(expenses.getErrorDescription());
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

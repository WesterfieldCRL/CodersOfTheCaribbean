package Billing;



import java.sql.*;
import java.time.LocalDate;
import java.util.*;
/**
 * Represents an expense entry with details such as name, date, price, and error description.
 */
public class Expenses{
    private Double price;
    private String name;
    private String errorDescription;
    private LocalDate date;
    private int id;

    /**
     * Default constructor for Expenses.
     */
    public Expenses(){
    }
    /**
     * Constructs an Expenses object with specified parameters.
     *
     * @param tName           The name of the expense.
     * @param tDate           The date of the expense.
     * @param tPrice          The price of the expense.
     * @param error           The error description related to the expense.
     */
    public Expenses(String tName, LocalDate tDate, Double tPrice, String error){
        name = tName;
        date = tDate;
        price = tPrice;
        errorDescription = error;
    }
    /**
     * Constructs an Expenses object with an ID and specified parameters.
     *
     * @param id              The unique identifier for the expense.
     * @param tName           The name of the expense.
     * @param tDate           The date of the expense.
     * @param tPrice          The price of the expense.
     * @param error           The error description related to the expense.
     */
    public Expenses(int id, String tName, LocalDate tDate, Double tPrice, String error) {
        this.id = id;
        this.name = tName;
        this.date = tDate;
        this.price = tPrice;
        this.errorDescription = error;
    }
    /**
     * Updates the error description for a specific expense identified by its ID.
     *
     * @param error The new error description.
     * @param ID    The ID of the expense to update.
     * @return True if the update is successful, false otherwise.
     */
    public boolean updateError(String error, int ID){
        try (Connection connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;")) {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            String updateQuery = "UPDATE BILLS SET ERROR_DESCRIPTION = ? WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(2, ID);

                preparedStatement.setString(1,error);
                preparedStatement.executeUpdate();


                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Removes the error description for a specific expense identified by its ID.
     *
     * @param ID The ID of the expense to update.
     * @return True if the removal is successful, false otherwise.
     */
    public boolean removeError(int ID){
        try (Connection connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;")) {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            String updateQuery = "UPDATE BILLS SET ERROR_DESCRIPTION = ? WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(2, ID);
                preparedStatement.setString(1,"");


                int affectedRows = preparedStatement.executeUpdate();
                return affectedRows > 0;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Retrieves the error description for a specific expense identified by its ID.
     *
     * @param ID The ID of the expense to retrieve the error for.
     * @return True if the error is successfully retrieved, false otherwise.
     */
    public Boolean getError(int ID) {
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            PreparedStatement selectQuery = connection.prepareStatement("SELECT * FROM BILLS WHERE ID = ?");
            selectQuery.setInt(1, ID);
            ResultSet rs = selectQuery.executeQuery();

            if (rs.next()) {
                String error = rs.getString("ERROR_DESCRIPTION");
                this.errorDescription = error;



                return true;
            }
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

        return false;
    }
    /**
     * Gets the error description for the expense.
     *
     * @return The error description.
     */
    public String getErrorDescription(){
        return errorDescription;
    }
    /**
     * Sets the price of the expense.
     *
     * @param newPrice The new price to set.
     */
    public void setPrice(Double newPrice){
        price = newPrice;
    }
    /**
     * Gets the price of the expense.
     *
     * @return The price of the expense.
     */
    public Double getPrice(){return price;}
    /**
     * Sets the name of the expense.
     *
     * @param newName The new name to set.
     */
    public void setName(String newName){
        name = newName;
    }
    /**
     * Gets the name of the expense.
     *
     * @return The name of the expense.
     */
    public String getName(){return name;}
    /**
     * Sets the date of the expense.
     *
     * @param newDate The new date to set.
     */
    public void setDate(LocalDate newDate){
        date = newDate;
    }
    /**
     * Gets the date of the expense.
     *
     * @return The date of the expense.
     */
    public LocalDate getDate(){return date;}
    /**
     * Retrieves a list of all expenses from the database.
     *
     * @return List of Expenses representing all expenses.
     */
    public List<Expenses> getExpenses() {
        List<Expenses> expenseSummary = new ArrayList<>();
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement selectQuery = connection.prepareStatement(
                    "SELECT * FROM BILLS");


            ResultSet rs = selectQuery.executeQuery();


            while (rs.next()) {
                int id = rs.getInt("ID");
                String guestName = rs.getString("GUEST");
                Double amount = rs.getDouble("AMOUNT");
                LocalDate date = rs.getDate("DATE").toLocalDate();


                String error = rs.getString("ERROR_DESCRIPTION");

                Expenses expense = new Expenses(id, guestName, date, amount, error);


                expenseSummary.add(expense);

            }

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
        return expenseSummary;
    }
    /**
     * Gets the unique identifier (ID) of the expense.
     *
     * @return The ID of the expense.
     */
    public int getId() {
        return this.id;
    }
}

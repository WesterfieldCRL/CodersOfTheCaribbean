package Billing;



import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class Expenses{
    private Double price;
    private String name;
    private String errorDescription;
    private LocalDate date;
    private int id;


    public Expenses(){
    }
    public Expenses(String tName, LocalDate tDate, Double tPrice, String error){
        name = tName;
        date = tDate;
        price = tPrice;
        errorDescription = error;
    }
    public Expenses(int id, String tName, LocalDate tDate, Double tPrice, String error) {
        this.id = id;
        this.name = tName;
        this.date = tDate;
        this.price = tPrice;
        this.errorDescription = error;
    }
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
    public String getErrorDescription(){
        return errorDescription;
    }
    public void setPrice(Double newPrice){
        price = newPrice;
    }
    public Double getPrice(){return price;}
    public void setName(String newName){
        name = newName;
    }
    public String getName(){return name;}
    public void setDate(LocalDate newDate){
        date = newDate;
    }
    public LocalDate getDate(){return date;}
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

    public int getId() {
        return this.id;
    }
}

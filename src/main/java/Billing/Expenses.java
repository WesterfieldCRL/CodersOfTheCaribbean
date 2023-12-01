package Billing;



import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class Expenses{
  private Double price;
  private String name;
  private String errorDescription;
  private LocalDate date;

  
  public Expenses(){
  }
  public Expenses(String tName, LocalDate tDate, Double tPrice, String error){
    name = tName;
    date = tDate;
    price = tPrice;
    errorDescription = error;
  }
  public Expenses (String tName, LocalDate tDate, Double tPrice){
    this(tName, tDate, tPrice, "");
  }
  public boolean updateError(String error, int ID){
    try (Connection connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;")) {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

      String updateQuery = "UPDATE BILLS SET ERROR_DESCRIPTION = ? WHERE ID = ?";
      try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
        preparedStatement.setInt(4, ID);
        preparedStatement.setString(3,error);


        int affectedRows = preparedStatement.executeUpdate();
        return affectedRows > 0;
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
        preparedStatement.setInt(4, ID);
        preparedStatement.setString(3,"");


        int affectedRows = preparedStatement.executeUpdate();
        return affectedRows > 0;
      }
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
  public  String getError() {
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
        String guestName = rs.getString("GUEST");
        Double amount = rs.getDouble("AMOUNT");
        LocalDate date = rs.getDate("DATE").toLocalDate();


        String error = rs.getString("ERROR_DESCRIPTION");

        Expenses expense = new Expenses(guestName, date,amount, error);


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
}

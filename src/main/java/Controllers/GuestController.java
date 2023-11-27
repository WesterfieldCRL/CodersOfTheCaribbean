package Controllers;

import Person.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestController {
    public static List<Guest> getGuestList() {
        List<Guest> guests = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            String sql = "SELECT USERNAME, PASSWORD, NAME, ADDRESS, EMAIL FROM LOGINDATA WHERE \"AccountType\" = 'GUEST'";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("USERNAME");
                String password = resultSet.getString("PASSWORD");
                String name = resultSet.getString("NAME");
                String address = resultSet.getString("ADDRESS");
                String email = resultSet.getString("EMAIL");

                Guest guest = new Guest(username, password, name, address, email);
                guests.add(guest);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return guests;
    }


}

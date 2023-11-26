package Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.SQLException;

public class TravelAgent extends Person {
    public TravelAgent(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public boolean createAccount()
    {
        return createGenericAccount("TRAVELAGENT");
    }

    public static boolean modifyTravelAgentAccount(String currentUsername, String newUsername, String newPassword, String newAddress) {
        Connection connection = null;
        boolean updateSuccess = false;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            String updateSQL = "UPDATE LOGINDATA SET USERNAME = ?, PASSWORD = ?, ADDRESS = ? WHERE USERNAME = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSQL);

            updateStmt.setString(1, newUsername);
            updateStmt.setString(2, newPassword);
            updateStmt.setString(3, newAddress);
            updateStmt.setString(4, currentUsername);

            int rowsAffected = updateStmt.executeUpdate();
            updateSuccess = rowsAffected > 0;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            updateSuccess = false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return updateSuccess;
    }
}
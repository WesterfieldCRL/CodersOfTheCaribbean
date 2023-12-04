package Person;

import java.sql.*;

/**
 * The {@code TravelAgent} class represents a travel agent in the cruise booking system.
 *
 * <p>This class extends the {@code Person} class and provides additional functionality specific to travel agents,
 * such as account creation.</p>
 *
 * <p>Key features of the {@code TravelAgent} class include:</p>
 * <ul>
 *   <li>Account creation for travel agents using the {@code createAccount} method.</li>
 * </ul>
 */
public class TravelAgent extends Person {

    /**
     * Constructs a {@code TravelAgent} object with the specified username, password, name, address, and email.
     *
     * @param username the username of the travel agent
     * @param password the password of the travel agent
     * @param name the name of the travel agent
     * @param address the address of the travel agent
     * @param email the email of the travel agent
     */
    public TravelAgent(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    /**
     * Creates a new account with generic details for a Travel Agent.
     * <p>
     * This method is a convenience method that invokes the {@code createGenericAccount}
     * method with the account type set to "TRAVELAGENT". It creates a new Travel Agent
     * account entry in the 'LOGINDATA' table with default details.
     * </p>
     *
     * @return {@code true} if the Travel Agent account is successfully created, {@code false} otherwise.
     * @see #createGenericAccount(String)
     */
    public boolean createAccount()
    {
        return createGenericAccount("TRAVELAGENT");
    }

    /**
     * Modifies the details of a Travel Agent account in the 'LOGINDATA' table.
     * <p>
     * This method connects to the 'cruiseDatabase' using JDBC and performs the following steps:
     * </p>
     * <ol>
     *   <li>Updates the 'LOGINDATA' table with the provided new username, new password, and new address
     *   for the Travel Agent account identified by the current username.</li>
     *   <li>Returns {@code true} if the account details are successfully modified, {@code false} otherwise.</li>
     * </ol>
     *
     * @param currentUsername the current username of the Travel Agent account to be modified.
     * @param newUsername     the new username for the Travel Agent account.
     * @param newPassword     the new password for the Travel Agent account.
     * @param newAddress      the new address for the Travel Agent account.
     * @return {@code true} if the account details are successfully modified, {@code false} otherwise.
     */
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

    /**
     * Marks a reservation as checked out in the database.
     *
     * This method updates the status of a reservation to indicate that the guest has checked out.
     * It connects to the database, prepares a statement to update the check-out status, and executes the update.
     * If the update is successful, the method returns true, otherwise, it returns false, indicating
     * the check-out operation was not completed due to an error.
     *
     * @param reservationId The ID of the reservation to be checked out.
     * @return true if the check-out was successful, false otherwise.
     */
    public boolean checkIn(int reservationId) {
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            String updateSQL = "UPDATE CHECKIN SET ISCHECKEDIN = true WHERE ID = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSQL);

            updateStmt.setInt(1, reservationId);
            updateStmt.executeUpdate();
            updateStmt.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * Marks a reservation as checked in in the database.
     *
     * This method updates the status of a reservation to indicate that the guest has checked in.
     * It connects to the database, prepares a statement to update the check-in status, and executes the update.
     * If the update is successful, the method returns true, otherwise, it returns false, indicating
     * the check-in operation was not completed due to an error.
     *
     * @param reservationId The ID of the reservation to be checked in.
     * @return true if the check-in was successful, false otherwise.
     */
    public boolean checkOut(int reservationId) {
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            String updateSQL = "UPDATE CHECKIN SET ISCHECKEDOUT = true WHERE ID = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSQL);

            updateStmt.setInt(1, reservationId);
            updateStmt.executeUpdate();
            updateStmt.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * Retrieves the check-in status for a reservation.
     *
     * Queries the database to determine if the reservation associated with the provided
     * reservation ID has been marked as checked in. This method establishes a connection to
     * the database, executes a SQL query, and returns the check-in status.
     *
     * @param reservationId The unique identifier for the reservation.
     * @return A Boolean value representing the check-in status: {@code true} if checked in,
     *         {@code false} if not, and {@code null} if the reservation ID is not found or
     *         an error occurs.
     */
    public Boolean getCheckInStatus(int reservationId) {
        Connection connection = null;
        Boolean isCheckedIn = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            String querySQL = "SELECT ISCHECKEDIN FROM CHECKIN WHERE ID = ?";
            PreparedStatement queryStmt = connection.prepareStatement(querySQL);

            queryStmt.setInt(1, reservationId);
            ResultSet rs = queryStmt.executeQuery();

            if (rs.next()) {
                isCheckedIn = rs.getBoolean("ISCHECKEDIN");
            }
            rs.close();
            queryStmt.close();

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

        return isCheckedIn;
    }

    /**
     * Retrieves the check-out status for a reservation.
     *
     * Queries the database to determine if the reservation associated with the provided
     * reservation ID has been marked as checked out. This method establishes a connection to
     * the database, executes a SQL query, and returns the check-out status.
     *
     * @param reservationId The unique identifier for the reservation.
     * @return A Boolean value representing the check-out status: {@code true} if checked out,
     *         {@code false} if not, and {@code null} if the reservation ID is not found or
     *         an error occurs.
     */
    public Boolean getCheckOutStatus(int reservationId) {
        Connection connection = null;
        Boolean isCheckedOut = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            String querySQL = "SELECT ISCHECKEDOUT FROM CHECKIN WHERE ID = ?";
            PreparedStatement queryStmt = connection.prepareStatement(querySQL);

            queryStmt.setInt(1, reservationId);
            ResultSet rs = queryStmt.executeQuery();

            if (rs.next()) {
                isCheckedOut = rs.getBoolean("ISCHECKEDOUT");
            }
            rs.close();
            queryStmt.close();

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

        return isCheckedOut;
    }
}
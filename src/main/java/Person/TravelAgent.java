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
}
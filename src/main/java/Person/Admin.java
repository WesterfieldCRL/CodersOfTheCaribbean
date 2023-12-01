package Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The {@code Admin} class represents an administrator in the cruise booking system.
 *
 * <p>This class extends the {@code Person} class and provides additional functionality specific to administrators,
 * such as account creation and handling password reset requests.</p>
 *
 * <p>Key features of the {@code Admin} class include:</p>
 * <ul>
 *   <li>Account creation for administrators using the {@code createAccount} method.</li>
 *   <li>Retrieval of password reset requests from the database using the {@code getResetRequests} method.</li>
 * </ul>
 */
public class Admin extends Person {

    /**
     * Constructs an {@code Admin} object with the specified username, password, name, address, and email.
     *
     * @param username the username of the administrator
     * @param password the password of the administrator
     * @param name the name of the administrator
     * @param address the address of the administrator
     * @param email the email of the administrator
     */
    public Admin(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    /**
     * Creates an administrator account in the database.
     *
     * @return {@code true} if the account was created successfully, {@code false} otherwise
     */
    public boolean createAccount()
    {
        return createGenericAccount("ADMIN");
    }

    /**
     * Retrieves all password reset requests from the database.
     *
     * @return a collection of {@code Guest} objects representing the guests who have requested a password reset
     */
    public Collection<Guest> getResetRequests() //If no requests, collection will be empty
    {
        ArrayList<Guest> guests = new ArrayList<Guest>();
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement selectQuery = connection.prepareStatement(
                    "SELECT * FROM PASSWORDRESETS INNER JOIN LOGINDATA on LOGINDATA.USERNAME = PASSWORDRESETS.USERNAME");
            ResultSet rs = selectQuery.executeQuery();

            while (rs.next())
            {
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String name = rs.getString("NAME");
                String email = rs.getString("EMAIL");
                String address =rs.getString("ADDRESS");
                Guest guest = new Guest(username, password, name, address, email);
                guest.setChangedPassword(rs.getString("NEWPASSWORD"));
                guests.add(guest);
            }

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

        return guests;
    }

}


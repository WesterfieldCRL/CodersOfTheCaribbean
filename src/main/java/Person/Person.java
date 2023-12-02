package Person;

import java.sql.*;
import java.util.*;

/**
 * The {@code Person} class represents a person in the cruise booking system.
 *
 * <p>This class provides functionality common to all types of people in the system, such as login and account creation.</p>
 *
 * <p>Key features of the {@code Person} class include:</p>
 * <ul>
 *   <li>Login functionality using the {@code login} method.</li>
 *   <li>Account creation functionality using the {@code createGenericAccount} method.</li>
 * </ul>
 */
public class Person {
    private String username;
    private String password;
    private String name;
    private String address;
    private String email;

    /**
     * Constructs a {@code Person} object with the specified username, password, name, address, and email.
     *
     * @param username the username of the person
     * @param password the password of the person
     * @param name the name of the person
     * @param address the address of the person
     * @param email the email of the person
     */
    public Person(String username, String password, String name, String address, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    /**
     * Authenticates a user login based on the provided username and password.
     * <p>
     * This method connects to the 'cruiseDatabase' using JDBC and performs the following steps:
     * </p>
     * <ol>
     *   <li>Queries the 'LOGINDATA' table to verify the provided username and password.</li>
     *   <li>If a matching user is found, constructs the corresponding type of person (Guest, TravelAgent, Manager, Admin).</li>
     *   <li>Loads reservations for Guest accounts using the {@code getReservations} method.</li>
     *   <li>Returns an {@code Optional} containing the authenticated person or an empty {@code Optional} if login fails.</li>
     * </ol>
     *
     * @param username the username for the login attempt.
     * @param password the password for the login attempt.
     * @return an {@code Optional} containing the authenticated person or an empty {@code Optional} if login fails.
     * @see Guest
     * @see TravelAgent
     * @see Manager
     * @see Admin
     * @see Guest#getReservations()
     */
    public static Optional<Person> login(String username, String password)
    {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM LOGINDATA WHERE USERNAME = ? AND PASSWORD = ?");
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) //There should never be duplicates
            {
                return switch (rs.getString("AccountType")) {
                    case "GUEST" -> {
                        Guest guest = new Guest(rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getString("NAME"),
                                rs.getString("ADDRESS"),
                                rs.getString("EMAIL"));

                        //Loads reservations into guest
                        guest.getReservations();

                        connection.close();
                        yield Optional.of(guest);
                    }
                    case "TRAVELAGENT" -> {
                        TravelAgent tav = new TravelAgent(rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getString("NAME"),
                                rs.getString("ADDRESS"),
                                rs.getString("EMAIL"));
                        connection.close();
                        yield Optional.of(tav);
                    }
                    case "MANAGER" -> {
                        Manager manager = new Manager(rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getString("NAME"),
                                rs.getString("ADDRESS"),
                                rs.getString("EMAIL"));
                        connection.close();
                        yield Optional.of(manager);
                    }
                    case "ADMIN" -> {
                        Admin admin = new Admin(rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getString("NAME"),
                                rs.getString("ADDRESS"),
                                rs.getString("EMAIL"));
                        connection.close();
                        yield Optional.of(admin);
                    }
                    default -> {
                        System.out.println("An unexpected value was found while reading loginData");
                        connection.close();
                        yield Optional.empty();
                    }
                };
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
        return Optional.empty();
    }

    /**
     * Creates a new account with generic details based on the specified account type.
     * <p>
     * This method connects to the 'cruiseDatabase' using JDBC and performs the following steps:
     * </p>
     * <ol>
     *   <li>Checks if an account with the provided username and password already exists in the 'LOGINDATA' table.</li>
     *   <li>If the account does not exist and the required fields (username, password, name, email, address) are not empty,
     *   inserts a new account entry with generic details into the 'LOGINDATA' table.</li>
     *   <li>Returns {@code true} if the account is successfully created, {@code false} otherwise.</li>
     * </ol>
     *
     * @param accountType the type of account to be created (e.g., "GUEST", "TRAVELAGENT", "MANAGER", "ADMIN").
     * @return {@code true} if the account is successfully created, {@code false} otherwise.
     */
    protected boolean createGenericAccount(String accountType)
    {
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            //Check if account exists
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM LOGINDATA WHERE USERNAME = ? AND PASSWORD = ?");
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();

            if (!rs.next() &&
                    !username.isEmpty() && !password.isEmpty() &&
                    !name.isEmpty() && !email.isEmpty() && !address.isEmpty())
            {
                PreparedStatement insertQuery = connection.prepareStatement("INSERT INTO LOGINDATA " +
                        "(USERNAME, PASSWORD, NAME, EMAIL, ADDRESS, \"AccountType\") " +
                        "VALUES (?, ?, ?, ?, ?, ?)");
                insertQuery.setString(1, username);
                insertQuery.setString(2, password);
                insertQuery.setString(3, name);
                insertQuery.setString(4, email);
                insertQuery.setString(5, address);
                insertQuery.setString(6, accountType);

                insertQuery.executeUpdate();
                connection.close();
                return true;
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
        return false;
    }

    /**
     * Retrieves the username of the person.
     *
     * @return the username of the person.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the person.
     *
     * @param username the new username of the person.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password of the person.
     *
     * @return the password of the person.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the person.
     *
     * @param password the new password of the person.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the name of the person.
     *
     * @return the name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person.
     *
     * @param name the new name of the person.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the address of the person.
     *
     * @return the address of the person.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the person.
     *
     * @param address the new address of the person.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retrieves the email of the person.
     *
     * @return the email of the person.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the person.
     *
     * @param email the new email of the person.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
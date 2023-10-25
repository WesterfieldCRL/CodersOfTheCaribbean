package Person;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Person {
    private String username;
    private String password;
    private String name;
    private String address;
    private String email;

    public Person(String username, String password, String name, String address, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    /**
     * login
     * <p>
     * takes a username and password, and checks if they match
     * information in the database. Returns an Optional<Person>.
     * Example for use:<p>
     * Optional<Person> temp = login("username","password");
     * <p>
     * User temp.isPresent() to check if login was successful
     * <p>
     * to check for class, use getClass, ex:
     * temp.get().getClass() == TravelAgent.Class
     * <p>
     * Can then be assigned to variable, ex:
     * TravelAgent tav = (TravelAgent) temp.get();
     * <p>
     * Parameters:
     *   username - username input
     *   password - password input
     * <p>
     * Return value: Optional<Person>
     */
    public static Optional<Person> login(String username, String password)
    {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM \"loginData\" WHERE USERNAME = ? AND PASSWORD = ?");
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
                        yield Optional.of(guest);
                    }
                    case "TRAVELAGENT" -> {
                        TravelAgent tav = new TravelAgent(rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getString("NAME"),
                                rs.getString("ADDRESS"),
                                rs.getString("EMAIL"));
                        yield Optional.of(tav);
                    }
                    case "MANAGER" -> {
                        Manager manager = new Manager(rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getString("NAME"),
                                rs.getString("ADDRESS"),
                                rs.getString("EMAIL"));
                        yield Optional.of(manager);
                    }
                    case "ADMIN" -> {
                        Admin admin = new Admin(rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getString("NAME"),
                                rs.getString("ADDRESS"),
                                rs.getString("EMAIL"));
                        yield Optional.of(admin);
                    }
                    default -> {
                        System.out.println("An unexpected value was found while reading loginData");
                        yield Optional.empty();
                    }
                };
            }

        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * createAccount
     * <p>
     * takes the given information and adds a line in the appropriate file
     * corresponding to that information.
     * <p>
     * Will first check to see if the given information is valid, and not
     * a duplicate
     * <p>
     * If a duplicate, or contains commas, function will return false
     * <p>
     * Parameters:
     *   self-explanatory
     * <p>
     * Return value: boolean
     */
    protected boolean createGenericAccount(String accountType)
    {
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            //Check if account exists
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM \"loginData\" WHERE USERNAME = ? AND PASSWORD = ?");
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();

            if (!rs.next())
            {
                PreparedStatement insertQuery = connection.prepareStatement("INSERT INTO \"loginData\" " +
                        "(USERNAME, PASSWORD, NAME, EMAIL, ADDRESS, \"AccountType\") " +
                        "VALUES (?, ?, ?, ?, ?, ?)");
                insertQuery.setString(1, username);
                insertQuery.setString(2, password);
                insertQuery.setString(3, name);
                insertQuery.setString(4, email);
                insertQuery.setString(5, address);
                insertQuery.setString(6, accountType);

                insertQuery.executeUpdate();
                //connection.close();
                return true;
            }

        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
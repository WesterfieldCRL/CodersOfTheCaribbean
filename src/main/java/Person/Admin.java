package Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class Admin extends Person {
    public Admin(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public boolean createAccount()
    {
        return createGenericAccount("ADMIN");
    }

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


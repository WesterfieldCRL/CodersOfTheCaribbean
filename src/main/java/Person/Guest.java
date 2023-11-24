package Person;

import java.sql.*;
import java.util.ArrayList;
import java.time.*;
import java.util.List;

import Cruise.*;



//fix date changes to correspond
//commit and push

public class Guest extends Person {
    //TODO: decide if billing information is a seperate class WILL GET ON THAT SHIT
    private String creditCardNumber;
    private String creditCardExpirationDate;
    private ArrayList<Reservation> reservations;

    private String changedPassword;

    public Guest(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
        this.reservations = new ArrayList<>();
    }

    public static boolean usernameExists(String username) {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            PreparedStatement searchQuery = connection.prepareStatement(
                    "SELECT USERNAME FROM LOGINDATA WHERE USERNAME = ?");
            searchQuery.setString(1, username);

            ResultSet rs = searchQuery.executeQuery();
            return rs.next();
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
    }


    public boolean createAccount()
    {
        return createGenericAccount("GUEST");
    }

    public static void resetRequest(String username, String password)
    {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            //Check that username exists
            PreparedStatement searchQuery = connection.prepareStatement(
                    "SELECT USERNAME FROM LOGINDATA WHERE USERNAME = ?");

            searchQuery.setString(1, username);

            ResultSet rs = searchQuery.executeQuery();

            if (rs.next())
            {

                //Check if request has already been submitted exists
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM PASSWORDRESETS WHERE USERNAME = ?");
                statement.setString(1, username);

                ResultSet rs2 = statement.executeQuery();

                if (!rs2.next()) {

                    PreparedStatement insertQuery = connection.prepareStatement(
                            "INSERT INTO PASSWORDRESETS (USERNAME, NEWPASSWORD) VALUES (?, ?)");

                    insertQuery.setString(1, username);
                    insertQuery.setString(2, password);

                    insertQuery.executeUpdate();
                }
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
    }

    //!Updated to immediately update reset request DB
    public void resetPassword() {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            connection.setAutoCommit(false);

            PreparedStatement updateQuery = connection.prepareStatement(
                    "UPDATE LOGINDATA SET PASSWORD = ? WHERE USERNAME = ?");
            updateQuery.setString(1, this.getChangedPassword());
            updateQuery.setString(2, this.getUsername());
            updateQuery.executeUpdate();

            PreparedStatement deleteQuery = connection.prepareStatement(
                    "DELETE FROM PASSWORDRESETS WHERE USERNAME = ?");
            deleteQuery.setString(1, this.getUsername());
            deleteQuery.executeUpdate();

            connection.commit();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean writeReservation(LocalDate start, LocalDate end, Cruise cruise, Room room){

        Clock clock = Clock.systemDefaultZone();

        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement insertQuery = connection.prepareStatement("INSERT INTO RESERVATIONS " +
                    "(USERNAME, ROOMID, COST, STARTDATE, ENDDATE, DATERESERVED, CRUISE) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
            insertQuery.setString(1, this.getUsername());
            insertQuery.setInt(2, room.getID());
            insertQuery.setDouble(3, room.getTotalCost(start, end));
            insertQuery.setDate(4, Date.valueOf(start));
            insertQuery.setDate(5, Date.valueOf(end));
            insertQuery.setDate(6, Date.valueOf(LocalDate.now(clock)));
            insertQuery.setString(7, cruise.getName());

            insertQuery.executeUpdate();
            connection.close();
            return true;

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


    public boolean makeReservation(Room room, LocalDate start, LocalDate end, Cruise cruise){
        boolean b = writeReservation(start, end, cruise, room);

        getReservations();

        return b;
    }

    public boolean cancelReservation(int reservationId) {
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            PreparedStatement selectQuery = connection.prepareStatement("SELECT * FROM RESERVATIONS WHERE ID = ?");
            selectQuery.setInt(1, reservationId);
            ResultSet rs = selectQuery.executeQuery();

            if (rs.next()) {
                LocalDate reservedStart = rs.getDate("STARTDATE").toLocalDate();

                if (reservedStart.isBefore(LocalDate.now())) {
                    return false;
                }

                PreparedStatement deleteQuery = connection.prepareStatement("DELETE FROM RESERVATIONS WHERE ID = ?");
                deleteQuery.setInt(1, reservationId);
                deleteQuery.executeUpdate();

                this.reservations.removeIf(r -> r.id == reservationId);

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

    public boolean modifyReservation(int reservationId, Room room, LocalDate start, LocalDate end, Cruise cruise) {
        Clock clock = Clock.systemDefaultZone();

        try (Connection connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;")) {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            String updateQuery = "UPDATE RESERVATIONS SET ROOMID = ?, COST = ?, STARTDATE = ?, ENDDATE = ?, DATERESERVED = ?, CRUISE = ? WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(1, room.getID());
                preparedStatement.setDouble(2, room.getTotalCost(start, end));
                preparedStatement.setDate(3, Date.valueOf(start));
                preparedStatement.setDate(4, Date.valueOf(end));
                preparedStatement.setDate(5, Date.valueOf(LocalDate.now(clock)));
                preparedStatement.setString(6, cruise.getName());
                preparedStatement.setInt(7, reservationId);

                int affectedRows = preparedStatement.executeUpdate();
                return affectedRows > 0;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reservation> getReservations() {
        List<Reservation> reservations = new ArrayList<>();
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement selectQuery = connection.prepareStatement(
                    "SELECT * FROM RESERVATIONS WHERE USERNAME = ?");

            selectQuery.setString(1, this.getUsername());

            ResultSet rs = selectQuery.executeQuery();

            while (rs.next()) {
                String cruiseName = rs.getString("CRUISE");
                int roomID = rs.getInt("ROOMID");
                LocalDate startDate = rs.getDate("STARTDATE").toLocalDate();
                LocalDate endDate = rs.getDate("ENDDATE").toLocalDate();
                int id = rs.getInt("ID");

                Room room = Room.getRoom(cruiseName, roomID, connection);

                Reservation reservation = new Reservation(cruiseName, room, startDate, endDate, id);
                reservations.add(reservation);
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
        return reservations;
    }

    public String getChangedPassword() {
        return changedPassword;
    }

    public void setChangedPassword(String changedPassword) {
        this.changedPassword = changedPassword;
    }
    public class Reservation {
        public String cruiseName;
        public Room room;
        public  LocalDate startDate;
        public LocalDate endDate;
        public int id;

        // Constructor
        public Reservation(String cruiseName, Room room, LocalDate startDate, LocalDate endDate, int id) {
            this.cruiseName = cruiseName;
            this.room = room;
            this.startDate = startDate;
            this.endDate = endDate;
            this.id = id;
        }

        public Room getRoom() {
            return room;
        }

        public String getCruiseName() {
            return cruiseName;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public int getId() {
            return id;
        }
    }
}
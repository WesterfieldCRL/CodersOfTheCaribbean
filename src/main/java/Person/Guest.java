package Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import Cruise.Room;
import Cruise.Cruise;

public class Guest extends Person {
    //TODO: decide if billing information is a seperate class
    private String creditCardNumber;
    private String creditCardExpirationDate;
    private ArrayList<Reservation> reservations;

    private String changedPassword;

    public Guest(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
        this.reservations = new ArrayList<>();
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

    public void resetPassword()
    {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement updateQuery = connection.prepareStatement(
                    "UPDATE LOGINDATA SET PASSWORD = ? WHERE USERNAME = ?");

            updateQuery.setString(1, this.getChangedPassword());
            updateQuery.setString(2, this.getUsername());

            updateQuery.execute();


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

    public boolean writeReservation(Date start, Date end, Cruise cruise, Room room){

        java.sql.Date sqlStartDate = new java.sql.Date(start.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(end.getTime());

        Date currDate = new Date();
        java.sql.Date sqlCurrDate = new java.sql.Date(currDate.getTime());

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
            insertQuery.setDate(4, sqlStartDate);
            insertQuery.setDate(5, sqlEndDate);
            insertQuery.setDate(6, sqlCurrDate);
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


    public boolean makeReservation(Room room, Date start, Date end, Cruise cruise){
        //Reservation r = new Reservation(cruise.getName(), room, start, end);

        //this.reservations.add(r);

        boolean b = writeReservation(start, end, cruise, room);

        getReservations();

        return b;
    }

    public boolean cancelReservation(int reservationId){
        //TODO: proper cancellation penalties

        Reservation toCancel = null;
        Date currDate = new Date();

        for(Reservation r : this.reservations){
            if(r.id == reservationId){
                toCancel = r;
                break;
            }
        }

        //if has no reservations or could not find reservation
        if(toCancel == null){
            return false;
        }

        //if attempting to cancel after reservation start date
        if(toCancel.startDate.before(currDate)){
            return false;
        }

        //delete from guest's list of reservations
        this.reservations.remove(toCancel);

        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            PreparedStatement deleteQuery = connection.prepareStatement("DELETE FROM " +
                    "RESERVATIONS WHERE ID = " + reservationId);

                    deleteQuery.executeUpdate();
                    connection.close();

                    return true;
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

    protected void getReservations()
    {
        //reservations = null;
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement selectQuery = connection.prepareStatement(
                    "SELECT * FROM RESERVATIONS WHERE USERNAME = ?");

            selectQuery.setString(1, this.getUsername());

            ResultSet rs = selectQuery.executeQuery();

            while (rs.next())
            {
                String cruiseName = rs.getString("CRUISE");
                int roomID = rs.getInt("ROOMID");
                Date startDate = rs.getDate("STARTDATE");
                Date endDate = rs.getDate("ENDDATE");
                int id = rs.getInt("ID");
                Room room = Room.getRoom(cruiseName, roomID, connection);


                Reservation reservation = new Reservation(cruiseName, room, startDate, endDate, id);

                reservations.add(reservation);


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

    public String getChangedPassword() {
        return changedPassword;
    }

    public void setChangedPassword(String changedPassword) {
        this.changedPassword = changedPassword;
    }
    public class Reservation {
        public String cruiseName;
        public Room room;
        public  Date startDate;
        public Date endDate;
        public int id;

        // Constructor
        public Reservation(String cruiseName, Room room, Date startDate, Date endDate, int id) {
            this.cruiseName = cruiseName;
            this.room = room;
            this.startDate = startDate;
            this.endDate = endDate;
            this.id = id;
        }
    }
}
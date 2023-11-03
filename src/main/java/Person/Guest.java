package Person;

import java.sql.*;
import java.util.ArrayList;
import java.time.*;
import Cruise.*;



//fix date changes to correspond
//commit and push

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

    public boolean cancelReservation(int reservationId){
        //TODO: proper cancellation penalties

        Reservation toCancel = null;
        Clock clock = Clock.systemDefaultZone();

        //search for reservation to cancel
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
        if(toCancel.startDate.isBefore(LocalDate.now())){
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

    public boolean modifyReservation(int reservationId, Room room, LocalDate start, LocalDate end, Cruise cruise){
        boolean found = false;
        Clock clock = Clock.systemDefaultZone();

        //search for reservation to modify and modify it in guests reservation list
        for(Reservation r : this.reservations){
            if(r.id == reservationId){
                //if attempting to modify after reservation start date
                if(r.startDate.isBefore(LocalDate.now())){
                    return false;
                }

                found = true;

                r.cruiseName = cruise.getName();
                r.room = room;
                r.startDate = start;
                r.endDate = end;

                break;
            }
        }

        //if has no reservations or could not find reservation
        if(!found){
            return false;
        }

        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            String updateQuery = "UPDATE RESERVATIONS SET ROOMID = ?, " +
                    "COST = ?, " +
                    "STARTDATE = ?, " +
                    "ENDDATE = ?, " +
                    "DATERESERVED = ? " +
                    "CRUISE = ? " +
                    "WHERE ID = " + reservationId;

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(2, room.getID());
            preparedStatement.setDouble(3, room.getTotalCost(start, end));
            preparedStatement.setDate(4, Date.valueOf(start));
            preparedStatement.setDate(5, Date.valueOf(end));
            preparedStatement.setDate(6, Date.valueOf(LocalDate.now(clock)));
            preparedStatement.setString(7, cruise.getName());

            preparedStatement.executeUpdate();
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
                LocalDate startDate = rs.getDate("STARTDATE").toLocalDate();
                LocalDate endDate = rs.getDate("ENDDATE").toLocalDate();
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
    }
}
package Person;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

import Cruise.Room;
import Cruise.Cruise;
import Cruise.Reservation;

public class Guest extends Person {
    //TODO: decide if billing information is a seperate class
    private String creditCardNumber;
    private String creditCardExpirationDate;
    private ArrayList<Reservation> reservations;

    // Constructor
    public Guest(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
        this.reservations = new ArrayList<Reservation>();
    }

    public boolean createAccount()
    {
        return createGenericAccount("GUEST");
    }

    /*public void requestPasswordReset()
    {
        if (conflictChecker(this.getUsername(), "resetRequests.txt")) {
            try {
                // Set the second argument to 'true' to enable appending
                FileWriter fileWriter = new FileWriter("resetRequests.txt", true);

                // Write the data to the file
                fileWriter.write(this.getUsername() + "\n");

                // Close the file writer
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateLoginData()
    {
        this.updateLoginInfo("GUEST");
    }*/

    //Format: guestName, roomID, cost, cruiseName, currDate, startDate, endDate
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
            //connection.close();
            return true;

        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    public boolean makeReservation(Room room, Date start, Date end, Cruise cruise){
        Reservation r = new Reservation(this, cruise, room, start, end);

        this.reservations.add(r);

        return writeReservation(start, end, cruise, room);
    }
}
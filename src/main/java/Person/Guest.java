package Person;

import java.io.*;
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

    public void requestPasswordReset()
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
    }

    //Format: guestName, roomID, cost, cruiseName, currDate, startDate, endDate
    public boolean writeReservation(String filename, Guest guest, Date start, Date end, Cruise cruise, Room room){
        StringBuilder sb = new StringBuilder();
        String data;
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currDate = simpleDateFormat.format(new Date());
        String startDate = simpleDateFormat.format(start);
        String endDate = simpleDateFormat.format(end);

        sb.append(guest.getName());
        sb.append(",");
        sb.append(room.getID());
        sb.append(",");
        sb.append(room.getTotalCost(start,end));
        sb.append(",");
        sb.append(cruise.getName());
        sb.append(",");
        sb.append(currDate);
        sb.append(",");
        sb.append(startDate);
        sb.append(",");
        sb.append(endDate);
        sb.append("\n");

        data = sb.toString();

        return fileWriter("Reservations.txt", data);
    }


    public boolean makeReservation(Room room, Date start, Date end, Cruise cruise){
        Reservation r = new Reservation(this, cruise, room, start, end);

        this.reservations.add(r);

        return writeReservation("Reservations.txt", this, start, end, cruise, room);
    }
    public void cancelReservation(int indexOfReservation){

    }
}
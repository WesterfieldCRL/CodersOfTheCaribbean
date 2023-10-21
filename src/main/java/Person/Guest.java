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

    public boolean writeReservation(String filename, Guest guest, Date start, Date end, Cruise cruise, Room room){
        StringBuilder sb = new StringBuilder();
        String data;
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currDate = simpleDateFormat.format(new Date());
        String startDate = simpleDateFormat.format(start);
        String endDate = simpleDateFormat.format(end);


        sb.append("-----RESERVATION-----");
        sb.append('\n');
        sb.append("NAME: " + guest.getName());
        sb.append('\n');
        sb.append("ROOM #: " + room.getRoomNum());
        sb.append('\n');
        sb.append("COST: $" + room.getMaximumDailyRate());
        sb.append('\n');
        sb.append("CRUISE: " + cruise.getName());
        sb.append('\n');
        sb.append("DATE MADE: " + currDate);
        sb.append('\n');
        sb.append("START DATE: " + startDate);
        sb.append('\n');
        sb.append("END DATE: " + endDate);
        sb.append('\n');
        sb.append('\n');

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

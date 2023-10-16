import Person.*;

import java.io.*;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static int numRooms = 40;
    public static void main(String[] args) throws IOException {
        Cruise c = new Cruise("Burmuda Triange");

        c.readCruiseRooms("cruise1.csv");

        for (Room r : c.getRoomList()) {
            System.out.println(r.getID() + ": " + r.getMaximumDailyRate());
        }

    }
}
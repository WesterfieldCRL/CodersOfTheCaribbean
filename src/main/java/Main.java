import Person.*;

import java.io.*;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static int numRooms = 40;
    public static void main(String[] args) throws IOException {
       Optional<Person> temp = Person.login("john_doe24", "mynameisjeff");

       if (temp.isPresent() && temp.get().getClass() == TravelAgent.class)
       {
           System.out.println("success!");
           TravelAgent tav = (TravelAgent) temp.get();
           System.out.println(tav.getUsername());
           System.out.println(tav.getPassword());
           System.out.println(tav.getName());
           System.out.println(tav.getAddress());
           System.out.println(tav.getEmail());
       }

       if (!TravelAgent.createNewTravelAgent("thanos", "1234", "destroyer", "hell", "snap@aol.airbud"))
       {
           System.out.println("-----------------------------------------------");
       }

       Optional<Person> temp2 = Person.login("thanos", "1234");

       if (temp2.isEmpty())
       {
           System.out.println("oh no");
       }

        if (temp2.isPresent() && temp2.get().getClass() == TravelAgent.class)
        {
            System.out.println("success!");
            TravelAgent tav = (TravelAgent) temp2.get();
            System.out.println(tav.getUsername());
            System.out.println(tav.getPassword());
            System.out.println(tav.getName());
            System.out.println(tav.getAddress());
            System.out.println(tav.getEmail());
        }


    }
}
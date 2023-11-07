import Cruise.*;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class testMain {
    public static void main (String[] args)
    {
        Optional<Cruise> cruiseOptional = Cruise.getCruise("CRUISE1");

        if (cruiseOptional.isPresent())
        {
            Cruise cruise = cruiseOptional.get();

            LocalDate start = LocalDate.of(2023, 11, 13);
            LocalDate end = LocalDate.of(2023, 11, 6);

            ArrayList<Room> roomArrayList = cruise.getRoomsList(start, end);

            for (Room room : roomArrayList)
            {
                System.out.println(room.getID());
            }

        }

    }
}

import Cruise.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class testMain {
    public static void main(String[] args)
    {
        Optional<Cruise> optionalCruise = Cruise.getCruise("CRUISE1");

        if (optionalCruise.isPresent())
        {
            Cruise cruise = optionalCruise.get();

            cruise.createTravelPath();

            ArrayList<LocalDate> dates = cruise.getValidReservationDates();

            for (LocalDate date : dates)
            {
                System.out.println(date);
            }

            cruise.printTravelPath();

            cruise.saveTravelPath();

            cruise.updateTravelPath();

            System.out.println();

            cruise.printTravelPath();

        }

    }
}

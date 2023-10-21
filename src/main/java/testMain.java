import Cruise.*;
import Person.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public class testMain {
    public static void main(String[] args) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        Date startDate = simpleDateFormat.parse("10/21/2023");
        Date endDate = simpleDateFormat.parse("10/24/2023");

        Optional<Person> optionalGuest = Person.login("gguy", "guygug");

        if (optionalGuest.isPresent())
        {
            if(optionalGuest.get().getClass() == Guest.class) {
                Guest guest = (Guest) optionalGuest.get();
                Optional<Cruise> optionalCruise = Cruise.getCruise("cruise1");

                if (optionalCruise.isPresent())
                {
                    Cruise cruise = optionalCruise.get();

                    Optional<Room> optionalRoom = cruise.isRoomAvailable(Room.Quality.COMFORT, 1, Room.BedType.QUEEN, true, startDate, endDate);

                    if (optionalRoom.isPresent())
                    {
                        Room room = optionalRoom.get();

                        guest.makeReservation(room, startDate, endDate, cruise);

                    }
                    else
                    {
                        System.out.println("room is not present");
                    }

                }
                else
                {
                    System.out.println("Cruise is not present");
                }

            }
            else
            {
                System.out.println("Person is not guest");
            }
        }
        else
        {
            System.out.println("Login failed");
        }


    }

}

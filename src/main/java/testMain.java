import Cruise.*;

import java.util.Optional;

public class testMain {
    public static void main(String[] args)
    {
        Optional<Cruise> optonalCruise = Cruise.getCruise("CRUISE1");

        if (optonalCruise.isPresent())
        {
            Cruise cruise = optonalCruise.get();

            cruise.createTravelPath();
            cruise.printTravePath();

        }

    }
}

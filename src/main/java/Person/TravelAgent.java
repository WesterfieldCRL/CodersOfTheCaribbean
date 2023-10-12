package Person;

import java.io.*;
import java.util.Optional;

public class TravelAgent extends Person {
    public TravelAgent(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    protected static Optional<TravelAgent> searchTravelAgentList(String username, String password)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("travelAgentLogin.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password))
                {
                    TravelAgent tav = new TravelAgent(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    return Optional.of(tav);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * createNewTravelAgent
     *
     * takes the given information and adds a line in the appropriate file
     * corresponding to that information.
     *
     * Will first check to see if the given information is valid, and not
     * a duplicate
     *
     * If a duplicate, or contains commas, function will return false
     *
     * Parameters:
     *   self-explanatory
     *
     * Return value: boolean
     */
    public static boolean createNewTravelAgent(String username, String password, String name, String address, String email)
    {
        if (username.contains(",") || password.contains(",") || name.contains(",") || address.contains(",") || email.contains(","))
        {
            return false;
        }
        if (conflictChecker(username, password, "travelAgentLogin.txt")) {
            String data = username + "," + password + "," + name + "," + address + "," + email + "\n";

            return TravelAgent.fileWriter(data, "travelAgentLogin.txt");
        }
        return false;
    }

}


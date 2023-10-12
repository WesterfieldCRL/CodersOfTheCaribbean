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

    public static void createNewTravelAgent(String username, String password, String name, String address, String email)
    {
        if (conflictChecker(username, password, "travelAgentLogin.txt")) {
            String data = username + "," + password + "," + name + "," + address + "," + email + "\n";

            TravelAgent.fileWriter(data, "travelAgentLogin.txt");
        }
    }

}


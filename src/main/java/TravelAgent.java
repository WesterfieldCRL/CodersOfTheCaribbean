import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class TravelAgent extends Person {
    public TravelAgent(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public static Collection<TravelAgent> getTravelAgentList()
    {
        Collection<TravelAgent> outputList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("travelAgentLogin.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                outputList.add(new TravelAgent(parts[0], parts[1], "temp", "temp", "temp"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputList;
    }

}


import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Admin extends Person {
    public Admin(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public static Collection<Admin> getAdminList()
    {
        Collection<Admin> outputList = new ArrayList<Admin>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("adminLogin.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                outputList.add(new Admin(parts[0], parts[1], "temp", "temp", "temp"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputList;
    }

    public static void createNewTravelAgent(String username, String password)
    {
        String data = username + "," + password + "\n";

        try {
            // Set the second argument to 'true' to enable appending
            FileWriter fileWriter = new FileWriter("travelAgentLogin.txt", true);

            // Write the data to the file
            fileWriter.write(data);

            // Close the file writer
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createNewTravelAgent(String username, String password, Collection<Person> personCollection)
    {
        String data = username + "," + password + "\n";

        try {
            // Set the second argument to 'true' to enable appending
            FileWriter fileWriter = new FileWriter("travelAgentLogin.txt", true);

            // Write the data to the file
            fileWriter.write(data);

            // Close the file writer
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        TravelAgent tav = new TravelAgent(username, password, "temp", "temp", "temp");
        personCollection.add(tav);

    }

}


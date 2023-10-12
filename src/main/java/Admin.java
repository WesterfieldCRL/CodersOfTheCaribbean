import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public class Admin extends Person {
    public Admin(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public void createNewTravelAgent(String username, String password)
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

    public void resetGuest(Guest guest)
    {

    }

    public static Optional<Admin> searchAdminList(String username, String password)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("adminLogin.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password))
                {
                    Admin admin = new Admin(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    return Optional.of(admin);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}


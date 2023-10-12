package Person;

import java.io.*;
import java.util.Optional;

public class Admin extends Person {
    public Admin(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    protected static Optional<Admin> searchAdminList(String username, String password)
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

    public void createNewTravelAgent(String username, String password)
    {
        if (conflictChecker(username, password, "adminLogin.txt")) {

            String data = username + "," + password + "\n";

            Admin.fileWriter(data, "adminLogin.txt");
        }

    }

    public void resetGuest(Guest guest)
    {

    }

}


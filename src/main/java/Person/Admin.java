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

    /**
     * createNewAdmin
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
    public boolean createNewAdmin(String username, String password, String name, String address, String email)
    {
        if (username.contains(",") || password.contains(",") || name.contains(",") || address.contains(",") || email.contains(","))
        {
            return false;
        }
        if (conflictChecker(username, password, "adminLogin.txt")) {

            String data = username + "," + password + "\n";

            return Admin.fileWriter(data, "adminLogin.txt");
        }
        return false;

    }

}


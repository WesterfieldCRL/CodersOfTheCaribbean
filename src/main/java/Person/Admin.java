package Person;

import java.io.*;
import java.util.Optional;

public class Admin extends Person {
    public Admin(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    /**
     * createNewAdmin
     * <p>
     * takes the given information and adds a line in the appropriate file
     * corresponding to that information.
     * <p>
     * Will first check to see if the given information is valid, and not
     * a duplicate
     * <p>
     * If a duplicate, or contains commas, function will return false
     * <p>
     * Parameters:
     *   self-explanatory
     * <p>
     * Return value: boolean
     */
    public boolean createNewAdmin(String username, String password, String name, String address, String email)
    {
        if (username.contains(",") || password.contains(",") || name.contains(",") || address.contains(",") || email.contains(","))
        {
            return false;
        }
        if (conflictChecker(username, password)) {

            String data = username + "," + password + "ADMIN" + "," + "," + name + "," + address + "," + email + "\n";

            return Admin.fileWriter(data);
        }
        return false;

    }

}


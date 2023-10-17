package Person;

import java.io.*;
import java.util.Optional;

public class Manager extends Person{

    public Manager(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public void getExpenseSummary()
    {
        //TODO: figure out what to do with this
    }

    /**
     * createNewManager
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
    public static boolean createNewManager(String username, String password, String name, String address, String email)
    {
        if (username.contains(",") || password.contains(",") || name.contains(",") || address.contains(",") || email.contains(","))
        {
            return false;
        }
        if (conflictChecker(username, password)) {
            String data = username + "," + password + "MANAGER" + "," + "," + name + "," + address + "," + email + "\n";

            return Manager.fileWriter(data);
        }
        return false;
    }
}

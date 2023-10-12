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

    protected static Optional<Manager> searchManagerList(String username, String password)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("managerLogin.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password))
                {
                    Manager manager = new Manager(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    return Optional.of(manager);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * createNewManager
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
    public static boolean createNewManager(String username, String password, String name, String address, String email)
    {
        if (username.contains(",") || password.contains(",") || name.contains(",") || address.contains(",") || email.contains(","))
        {
            return false;
        }
        if (conflictChecker(username, password, "managerLogin.txt")) {
            String data = username + "," + password + "," + name + "," + address + "," + email + "\n";

            return Manager.fileWriter(data, "managerLogin.txt");
        }
        return false;
    }
}

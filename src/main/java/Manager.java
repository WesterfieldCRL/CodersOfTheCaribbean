import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public class Manager extends Person{

    public Manager(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public void getExpenseSummary()
    {
        //TODO: figure out what to do with this
    }

    public static Optional<Manager> searchManagerList(String username, String password)
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

    public static void createNewManager(String username, String password, String name, String address, String email)
    {
        String data = username + "," + password + "," + name + "," + address + "," + email + "\n";

        try {
            // Set the second argument to 'true' to enable appending
            FileWriter fileWriter = new FileWriter("managerLogin.txt", true);

            // Write the data to the file
            fileWriter.write(data);

            // Close the file writer
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

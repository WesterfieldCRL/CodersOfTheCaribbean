package Person;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Person {
    private String username;
    private String password;
    private String name;
    private String address;
    private String email;

    public Person(String username, String password, String name, String address, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    protected static boolean conflictChecker(String username, String password, String fileName)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password))
                {
                    return false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    protected static void fileWriter(String data, String fileName)
    {
        try {
            // Set the second argument to 'true' to enable appending
            FileWriter fileWriter = new FileWriter(fileName, true);

            //check for newline
            if (!fileEndsWithNewline(fileName)) {
                // If not, add a newline character
                fileWriter.write(System.lineSeparator());
            }

            // Write the data to the file
            fileWriter.write(data);

            // Close the file writer
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static boolean fileEndsWithNewline(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String lastLine = "";
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            // Check if the last line is empty or contains only whitespace
            return lastLine.trim().isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Return false on any error
        }
    }

    public static Optional<Person> login(String username, String password)
    {

        Optional<Guest> guest = Guest.searchGuestList(username, password);

        if (guest.isPresent())
        {
            return Optional.of(guest.get());
        }

        Optional<Admin> admin = Admin.searchAdminList(username, password);

        if (admin.isPresent())
        {
            return Optional.of(admin.get());
        }

        Optional<Manager> manager = Manager.searchManagerList(username, password);

        if (manager.isPresent())
        {
            return Optional.of(manager.get());
        }

        Optional<TravelAgent> tav = TravelAgent.searchTravelAgentList(username, password);
        if (tav.isPresent())
        {
            return Optional.of(tav.get());
        }

        return Optional.empty();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
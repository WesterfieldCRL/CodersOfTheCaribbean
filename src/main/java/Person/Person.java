package Person;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    protected boolean conflictChecker(String username, String password)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("loginData.txt"));

            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(",");
                if (parts[0].equals(username) /*&& parts[1].equals(password)*/)
                {
                    return false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    protected boolean fileWriter(String data)
    {
        try {
            // Set the second argument to 'true' to enable appending
            FileWriter fileWriter = new FileWriter("loginData.txt", true);

            //check for newline
            /*if (!fileEndsWithNewline("loginData.txt")) {
                // If not, add a newline character
                fileWriter.write(System.lineSeparator());
            }
            else
            {
                return false;
            }*/
            //Removed for the moment as it was adding an extra newline

            // Write the data to the file
            fileWriter.write(data);

            // Close the file writer
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    protected boolean fileEndsWithNewline(String fileName) {
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

    /**
     * login
     * <p>
     * takes a username and password, and checks if they match
     * information in the database. Returns an Optional<Person>.
     * Example for use:<p>
     * Optional<Person> temp = login("username","password");
     * <p>
     * User temp.isPresent() to check if login was successful
     * <p>
     * to check for class, use getClass, ex:
     * temp.get().getClass() == TravelAgent.Class
     * <p>
     * Can then be assigned to variable, ex:
     * TravelAgent tav = (TravelAgent) temp.get();
     * <p>
     * Parameters:
     *   username - username input
     *   password - password input
     * <p>
     * Return value: Optional<Person>
     */
    public static Optional<Person> login(String username, String password)
    {

        try {
            BufferedReader reader = new BufferedReader(new FileReader("loginData.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password))
                {
                    switch (parts[2])
                    {
                        case "GUEST" :
                            Guest guest = new Guest(parts[0], parts[1], parts[3], parts[4], parts[5]);
                            return Optional.of(guest);
                        case "TRAVELAGENT" :
                            TravelAgent tav = new TravelAgent(parts[0], parts[1], parts[3], parts[4], parts[5]);
                            return Optional.of(tav);
                        case "MANAGER" :
                            Manager manager = new Manager(parts[0], parts[1], parts[3], parts[4], parts[4]);
                            return Optional.of(manager);
                        case "ADMIN" :
                            Admin admin = new Admin(parts[0], parts[1], parts[3], parts[4], parts[5]);
                            return Optional.of(admin);
                        default:
                            System.out.println("An unexpected value was found while reading loginData");
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * createAccount
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
    protected boolean createGenericAccount(String accountType)
    {
        if (username.contains(",") || password.contains(",") || name.contains(",") || address.contains(",") || email.contains(","))
        {
            return false;
        }
        if (conflictChecker(username, password)) {
            String data = username + "," + password + "," + accountType + "," + name + "," + address + "," + email + "\n";

            return fileWriter(data);
        }
        return false;
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
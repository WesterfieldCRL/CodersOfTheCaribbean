package Person;

import java.io.*;
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

    protected boolean conflictChecker(String data, String filename)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(",");
                if (parts[0].equals(data))
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
        if (!checkData())
        {
            return false;
        }
        if (conflictChecker(username, "loginData.txt")) {
            String data = formatData(accountType);
            return fileWriter(data);
        }
        return false;
    }

    protected String formatData(String accountType)
    {
        String data = username + "," + password + "," + accountType + "," + name + "," + address + "," + email + "\n";
        return data;
    }

    protected boolean checkData()
    {
        return !username.contains(",") && !password.contains(",") && !name.contains(",") && !address.contains(",") && !email.contains(",")
                && !username.isEmpty() && !password.isEmpty() && !name.isEmpty() && !address.isEmpty() && !email.isEmpty();
    }

    protected void updateLoginInfo(String accountType)
    {
        String formatedData = "";
        ArrayList<String> data = readFile("loginData.txt");
        for (String line : data)
        {
            String[] parsedData = line.split(",");
            if (parsedData[0].equals(this.username) && parsedData[2].equals(accountType))
            {
                formatedData = formatData(accountType);
                data.remove(line);
                break;
            }
        }
        data.add(formatedData);

        overwriteFile("loginData.txt", data);

    }

    protected ArrayList<String> readFile(String filename)
    {
        ArrayList<String> data = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    protected void overwriteFile(String filename, ArrayList<String> data)
    {
        try {
            // Set the second argument to 'true' to enable appending
            FileWriter fileWriter = new FileWriter(filename);

            // Write the data to the file
            for (String tempData : data)
            {
                fileWriter.write(tempData + "\n");
            }

            // Close the file writer
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
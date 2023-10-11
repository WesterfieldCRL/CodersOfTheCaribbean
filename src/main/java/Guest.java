import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Guest extends Person {
    //TODO: decide if billing information is a seperate class
    private String creditCardNumber;
    private Date creditCardExpirationDate;

    // Constructor
    public Guest(String username, String password, String name, String address, String email,
                 String creditCardNumber, Date creditCardExpirationDate) {
        super(username, password, name, address, email);
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpirationDate = creditCardExpirationDate;
    }
    
    public static Collection<Guest> getGuestList() {
        Collection<Guest> outputList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("guestsLogin.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                outputList.add(new Guest(parts[0], parts[1], "temp", "temp", "temp", "temp", new Date()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputList;
    }

    //WARNING: File must end in a newline for this to work
    //TODO: check for newline at end of file
    public static void createNewGuest(String username, String password)
    {
        String data = username + "," + password + "\n";

        try {
            // Set the second argument to 'true' to enable appending
            FileWriter fileWriter = new FileWriter("guestsLogin.txt", true);

            // Write the data to the file
            fileWriter.write(data);

            // Close the file writer
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewGuest(String username, String password, Collection<Person> personCollection)
    {
        String data = username + "," + password + "\n";

        try {
            // Set the second argument to 'true' to enable appending
            FileWriter fileWriter = new FileWriter("guestsLogin.txt", true);

            // Write the data to the file
            fileWriter.write(data);

            // Close the file writer
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Guest guest = new Guest(username, password, "temp", "temp", "temp", "temp", new Date());
        personCollection.add(guest);

    }

}

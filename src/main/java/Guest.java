import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

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

    public static Optional<Guest> searchGuestList(String username, String password)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("guestsLogin.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password))
                {
                    Guest guest = new Guest(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], new Date());
                    return Optional.of(guest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    //WARNING: File must end in a newline for this to work
    //TODO: check for newline at end of file
    //TODO: check for duplicates
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


}

package Person;

import java.io.*;
import java.util.Date;
import java.util.Optional;

public class Guest extends Person {
    //TODO: decide if billing information is a seperate class
    private String creditCardNumber;
    private String creditCardExpirationDate;

    // Constructor
    public Guest(String username, String password, String name, String address, String email,
                 String creditCardNumber, String creditCardExpirationDate) {
        super(username, password, name, address, email);
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpirationDate = creditCardExpirationDate;
    }

    protected static Optional<Guest> searchGuestList(String username, String password)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("guestsLogin.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password))
                {
                    Guest guest = new Guest(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                    return Optional.of(guest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * createNewGuest
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
    public static boolean createNewGuest(String username, String password, String name, String address, String email,
                                         String creditCardNumber, String creditCardExpirationDate)
    {

        if (username.contains(",") || password.contains(",") || name.contains(",") || address.contains(",") ||
                email.contains(",") || creditCardNumber.contains(",") || creditCardExpirationDate.contains(","))
        {
            return false;
        }

        if (conflictChecker(username, password, "guestsLogin.txt")) {
            String data = username + "," + password + "," + name + "," + address + "," + email
                    + creditCardNumber + creditCardExpirationDate + "\n";

            return Guest.fileWriter(data, "guestsLogin.txt");
        }
        return false;
    }


}

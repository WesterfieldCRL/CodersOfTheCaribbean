import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    
    public static Collection<Guest> getGuestList()
    {
        Collection<Guest> outputList = new ArrayList<Guest>();
        try {
            // Load the text file from resources, assumes file contains valid data
            InputStream resourceStream = Person.class.getResourceAsStream("/guestsLogin.txt");
            if (resourceStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    outputList.add(new Guest(parts[0], parts[1], "temp", "temp", "temp", "temp", new Date()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputList;
    }
}

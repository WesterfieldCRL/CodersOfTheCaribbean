package Person;

import java.io.FileWriter;
import java.io.IOException;

public class Guest extends Person {
    //TODO: decide if billing information is a separate class
    private String creditCardNumber;
    private String creditCardExpirationDate;

    // Constructor
    public Guest(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public boolean createAccount()
    {
        return createGenericAccount("GUEST");
    }

    public void requestPasswordReset()
    {
        try {
            // Set the second argument to 'true' to enable appending
            FileWriter fileWriter = new FileWriter("resetRequests", true);

            // Write the data to the file
            fileWriter.write(this.getUsername() + "\n");

            // Close the file writer
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

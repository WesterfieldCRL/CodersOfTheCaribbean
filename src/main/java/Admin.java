import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

public class Admin extends Person {
    public Admin(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public static Collection<Admin> getAdminList()
    {
        Collection<Admin> outputList = new ArrayList<Admin>();
        try {
            // Load the text file from resources, assumes file contains valid data
            InputStream resourceStream = Person.class.getResourceAsStream("/guestsLogin.txt");
            if (resourceStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    outputList.add(new Admin(parts[0], parts[1], "temp", "temp", "temp"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputList;
    }
}


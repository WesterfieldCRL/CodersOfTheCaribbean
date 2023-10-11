import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Person {
    private String username;
    private String password;
    private String name;
    private String address;
    private String email;

    // Constructor
    public Person(String username, String password, String name, String address, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public boolean login(String username, String password) {

        /*try {
            // Load the text file from resources, assumes file contains valid data
            InputStream resourceStream = getClass().getResourceAsStream("/guestsLogin.txt");
            if (resourceStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(username) && parts[1].equals(password))
                    {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return this.username.equals(username) && this.password.equals(password);
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

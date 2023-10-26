package Person;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class Admin extends Person {
    public Admin(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public boolean createAccount()
    {
        return createGenericAccount("ADMIN");
    }

    //Note: if there are no names in the file, this function will return an empty collection
    /*public Collection<Guest> getResetRequests()
    {
        ArrayList<Guest> guests = new ArrayList<Guest>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("resetRequests.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                Optional<Guest> guest = getGuest(line);
                guest.ifPresent(guests::add);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return guests;
    }

    private Optional<Guest> getGuest(String username)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("loginData.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[2].equals("GUEST"))
                {
                    Guest guest = new Guest(parts[0], parts[1], parts[3], parts[4], parts[5]);
                    return Optional.of(guest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /*public void resetGuestPassword(String username, String password, Guest guest)
    {
        guest.setPassword(password);
        guest.updateLoginData();
        ArrayList<String> usernames = readFile("resetRequests.txt");
        usernames.remove(username);
        overwriteFile("resetRequests.txt",usernames);
    }*/

}


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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



    public Optional<Person> login(String username, String password)
    {

        Optional<Guest> guest = Guest.searchGuestList(username, password);

        if (guest.isPresent())
        {
            return Optional.of(guest.get());
        }

        Optional<Admin> admin = Admin.searchAdminList(username, password);

        if (admin.isPresent())
        {
            return Optional.of(admin.get());
        }

        Optional<Manager> manager = Manager.searchManagerList(username, password);

        if (manager.isPresent())
        {
            return Optional.of(manager.get());
        }

        Optional<TravelAgent> tav = TravelAgent.searchTravelAgentList(username, password);
        if (tav.isPresent())
        {
            return Optional.of(tav.get());
        }

        return Optional.empty();
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
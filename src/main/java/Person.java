import java.util.*;

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
        return this.username.equals(username) && this.password.equals(password);
    }

    //use .isPresent() to check if not null, and .get() to get the value when not null
    public static Optional<Person> getPerson(Collection<Person> list, String username, String password)
    {
        for (Person person : list)
        {
            if (person.login(username, password))
            {
                return Optional.of(person);
            }
        }
        return Optional.empty();
    }

    public static Collection<Person> populateList()
    {
        Collection<Person> outputList = new ArrayList<Person>();

        outputList.addAll(Guest.getGuestList());
        outputList.addAll(Admin.getAdminList());
        outputList.addAll(Manager.getManagerList());
        outputList.addAll(TravelAgent.getTravelAgentList());

        return outputList;
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
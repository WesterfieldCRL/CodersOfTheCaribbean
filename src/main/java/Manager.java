import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Manager extends Person{

    public Manager(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public void getExpenseSummary()
    {
        //TODO: figure out what to do with this
    }

    public static Collection<Manager> getManagerList()
    {
        Collection<Manager> outputList = new ArrayList<Manager>();
        try {
            // Load the text file from resources, assumes file contains valid data
            InputStream resourceStream = Person.class.getResourceAsStream("/managerLogin.txt");
            if (resourceStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    outputList.add(new Manager(parts[0], parts[1], "temp", "temp", "temp"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputList;
    }
}

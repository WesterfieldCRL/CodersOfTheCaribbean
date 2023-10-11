import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

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
        Collection<Manager> outputList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("managerLogin.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                outputList.add(new Manager(parts[0], parts[1], "temp", "temp", "temp"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputList;
    }
}

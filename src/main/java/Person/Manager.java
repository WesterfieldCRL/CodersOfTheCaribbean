package Person;

public class Manager extends Person{

    public Manager(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public void getExpenseSummary()
    {
        //TODO: figure out what to do with this
    }

    public boolean createAccount()
    {
        return createGenericAccount("MANAGER");
    }
}

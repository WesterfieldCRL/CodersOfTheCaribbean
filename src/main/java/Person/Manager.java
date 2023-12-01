package Person;

public class Manager extends Person{

    public Manager(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }



    public boolean createAccount()
    {
        return createGenericAccount("MANAGER");
    }
}

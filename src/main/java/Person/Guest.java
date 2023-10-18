package Person;

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


}

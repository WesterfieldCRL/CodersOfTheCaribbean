package Person;

import java.io.*;
import java.util.Optional;

public class Admin extends Person {
    public Admin(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    public boolean createAccount()
    {
        return createGenericAccount("ADMIN");
    }

}


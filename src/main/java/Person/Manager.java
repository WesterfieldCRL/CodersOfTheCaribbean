package Person;

/**
 * The {@code Manager} class represents a manager in the cruise booking system.
 *
 * <p>This class extends the {@code Person} class and provides additional functionality specific to managers,
 * such as account creation.</p>
 *
 * <p>Key features of the {@code Manager} class include:</p>
 * <ul>
 *   <li>Account creation for managers using the {@code createAccount} method.</li>
 * </ul>
 */
public class Manager extends Person{

    /**
     * Constructs a {@code Manager} object with the specified username, password, name, address, and email.
     *
     * @param username the username of the manager
     * @param password the password of the manager
     * @param name the name of the manager
     * @param address the address of the manager
     * @param email the email of the manager
     */
    public Manager(String username, String password, String name, String address, String email) {
        super(username, password, name, address, email);
    }

    /**
     * Creates an manager account in the database.
     *
     * @return {@code true} if the account was created successfully, {@code false} otherwise
     */
    public boolean createAccount()
    {
        return createGenericAccount("MANAGER");
    }
}

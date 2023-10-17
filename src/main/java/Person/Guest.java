package Person;

public class Guest extends Person {
    //TODO: decide if billing information is a seperate class
    private String creditCardNumber;
    private String creditCardExpirationDate;

    // Constructor
    public Guest(String username, String password, String name, String address, String email,
                 String creditCardNumber, String creditCardExpirationDate) {
        super(username, password, name, address, email);
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpirationDate = creditCardExpirationDate;
    }

    /**
     * createNewGuest
     * <p>
     * takes the given information and adds a line in the appropriate file
     * corresponding to that information.
     * <p>
     * Will first check to see if the given information is valid, and not
     * a duplicate
     * <p>
     * If a duplicate, or contains commas, function will return false
     * <p>
     * Parameters:
     *   self-explanatory
     * <p>
     * Return value: boolean
     */
    public static boolean createNewGuest(String username, String password, String name, String address, String email,
                                         String creditCardNumber, String creditCardExpirationDate)
    {

        if (username.contains(",") || password.contains(",") || name.contains(",") || address.contains(",") ||
                email.contains(",") || creditCardNumber.contains(",") || creditCardExpirationDate.contains(","))
        {
            return false;
        }

        if (conflictChecker(username, password)) {
            String data = username + "," + password + "," + "GUEST" + "," + name + "," + address + "," + email
                    + "," + creditCardNumber + "," +creditCardExpirationDate + "\n";

            return Guest.fileWriter(data);
        }
        return false;
    }


}

package Billing;


import java.time.YearMonth;

/**
 * Represents billing information including credit card details.
 */
public class BillingInformation {
    /**
     * Default constructor for BillingInformation.
     */
    public BillingInformation(){

    }

    private String number;


    private YearMonth expirationDate;
    private Boolean isCorporateGuest;
    /**
     * Constructs a BillingInformation object with specified parameters.
     *
     * @param number           The credit card number.
     * @param expirationDate   The expiration date of the credit card.
     * @param isCorporateGuest Indicates whether the guest is a corporate guest.
     */
    public BillingInformation(String number, YearMonth expirationDate, Boolean isCorporateGuest){
        this.number=number;
        this.expirationDate=expirationDate;
        this.isCorporateGuest = isCorporateGuest;

    }
    /**
     * Gets the credit card number.
     *
     * @return The credit card number.
     */
    public String getCreditCardNumber() {
        return number;
    }
    /**
     * Sets the credit card number if it is a valid string.
     *
     * @param number The credit card number to set.
     */

    public void setCreditCardNumber(String number) {
        if (isValidString(number)) {
            this.number = number;
        }
    }
    /**
     * Gets whether the guest is a corporate guest.
     *
     * @return True if the guest is a corporate guest, false otherwise.
     */
    public Boolean getIsCorporateGuest(){
        return isCorporateGuest;
    }
    /**
     * Sets whether the guest is a corporate guest.
     *
     * @param isCorporateGuest True if the guest is a corporate guest, false otherwise.
     */
    public void setIsCorporateGuest(Boolean isCorporateGuest){
        this.isCorporateGuest = isCorporateGuest;
    }
    /**
     * Gets the credit card expiration date.
     *
     * @return The credit card expiration date.
     */
    public YearMonth getCreditCardExpirationDate() {
        return expirationDate;
    }
    /**
     * Sets the credit card expiration date.
     *
     * @param expirationDate The credit card expiration date to set.
     */
    public void setCreditCardExpirationDate(YearMonth expirationDate) {
        this.expirationDate = expirationDate;
    }
    /**
     * Checks whether the input string is a valid credit card number.
     *
     * @param input The input string to validate.
     * @return True if the input is a valid credit card number, false otherwise.
     */
    public static boolean isValidString(String input) {

        String regex = "\\d+";
        return input.matches(regex) && input.length() == 16;
    }
}

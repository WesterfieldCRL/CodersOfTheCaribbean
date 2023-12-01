package Billing;


import java.util.Date;

public class BillingInformation {

    private String number;


    private Date expirationDate;
    public BillingInformation(String number, Date expirationDate){
        this.number=number;
        this.expirationDate=expirationDate;
    }

    public String getCreditCardNumber() {
        return number;
    }

    public void setCreditCardNumber(String number) {
        if (isValidString(number)) {
            this.number = number;
        }
    }

    public Date getCreditCardExpirationDate() {
        return expirationDate;
    }

    public void setCreditCardExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    private static boolean isValidString(String input) {

        String regex = "\\d+";
        return input.matches(regex) && input.length() == 16;
    }
}

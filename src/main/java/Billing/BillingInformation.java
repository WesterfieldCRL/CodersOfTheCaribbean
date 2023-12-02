package Billing;


import java.time.YearMonth;
import java.util.Date;

public class BillingInformation {
    public BillingInformation(){

    }

    private String number;


    private YearMonth expirationDate;
    private Boolean isCorporateGuest;
    public BillingInformation(String number, YearMonth expirationDate, Boolean isCorporateGuest){
        this.number=number;
        this.expirationDate=expirationDate;
        this.isCorporateGuest = isCorporateGuest;

    }

    public String getCreditCardNumber() {
        return number;
    }

    public void setCreditCardNumber(String number) {
        if (isValidString(number)) {
            this.number = number;
        }
    }
    public Boolean getIsCorporateGuest(){
        return isCorporateGuest;
    }
    public void setIsCorporateGuest(Boolean isCorporateGuest){
        this.isCorporateGuest = isCorporateGuest;
    }

    public YearMonth getCreditCardExpirationDate() {
        return expirationDate;
    }

    public void setCreditCardExpirationDate(YearMonth expirationDate) {
        this.expirationDate = expirationDate;
    }

    private static boolean isValidString(String input) {

        String regex = "\\d+";
        return input.matches(regex) && input.length() == 16;
    }
}

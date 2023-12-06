import org.junit.jupiter.api.Test;
import java.time.YearMonth;
import static org.junit.jupiter.api.Assertions.*;
import Billing.*;
/**
 * Unit tests for the {@link BillingInformation} class.
 */
public class BillingInformationTest {

    /**
     * Tests the {@link BillingInformation#getCreditCardNumber()} method.
     */
    @Test
    void getCreditCardNumber() {
        BillingInformation billingInformation = new BillingInformation();
        assertNull(billingInformation.getCreditCardNumber());

        billingInformation.setCreditCardNumber("1234567890123456");
        assertEquals("1234567890123456", billingInformation.getCreditCardNumber());
    }
    /**
     * Tests the {@link BillingInformation#setIsCorporateGuest(Boolean)} and
     * {@link BillingInformation#getIsCorporateGuest()} methods.
     */
    @Test
    void setIsCorporateGuest() {
        BillingInformation billingInformation = new BillingInformation();
        assertNull(billingInformation.getIsCorporateGuest());

        billingInformation.setIsCorporateGuest(true);
        assertTrue(billingInformation.getIsCorporateGuest());
    }
    /**
     * Tests the {@link BillingInformation#getCreditCardExpirationDate()} and
     * {@link BillingInformation#setCreditCardExpirationDate(YearMonth)} methods.
     */
    @Test
    void getCreditCardExpirationDate() {
        BillingInformation billingInformation = new BillingInformation();
        assertNull(billingInformation.getCreditCardExpirationDate());

        YearMonth expirationDate = YearMonth.of(2023, 12);
        billingInformation.setCreditCardExpirationDate(expirationDate);
        assertEquals(expirationDate, billingInformation.getCreditCardExpirationDate());
    }
    /**
     * Tests the {@link BillingInformation#isValidString(String)} method.
     */
    @Test
    void isValidString() {
        assertTrue(BillingInformation.isValidString("1234567890123456"));
        assertFalse(BillingInformation.isValidString("invalid"));
        assertFalse(BillingInformation.isValidString("12345678901234567"));
    }
}

package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketValidatorTest {

    private final TicketValidator validator = new TicketValidator();

    @Test
    void shouldRejectNullAccountId() {
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateAccount(null)
        );

        assertEquals(
                "Enter an account ID",
                exception.getMessage()
        );
    }
}

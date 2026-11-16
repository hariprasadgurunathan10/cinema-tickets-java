package uk.gov.dwp.uc.pairtest.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.NULL_ACCOUNT_ID;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.INVALID_ACCOUNT_ID;

public class TicketValidatorTest {

    private final TicketValidator validator = new TicketValidator();

    @Test
    void shouldRejectNullAccountId() {
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateAccount(null)
        );

        assertEquals(
                NULL_ACCOUNT_ID,
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectZeroAccountId() {
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateAccount(0L)
        );

        assertEquals(
                INVALID_ACCOUNT_ID,
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNegativeAccountId() {
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateAccount(-1L)
        );

        assertEquals(
                INVALID_ACCOUNT_ID,
                exception.getMessage()
        );
    }

    @Test
    void shouldAcceptPositiveAccountId() {
        assertDoesNotThrow(() -> validator.validateAccount(1L));
    }
}

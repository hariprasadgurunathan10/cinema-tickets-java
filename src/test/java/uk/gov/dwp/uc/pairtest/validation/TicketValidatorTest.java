package uk.gov.dwp.uc.pairtest.validation;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.*;

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

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    void shouldRejectZeroAndNegativeAccountIds(long accountId) {
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateAccount(accountId)
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

    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectNullAndEmptyTicketTypeRequests(TicketTypeRequest[] requests) {
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateRequests(requests)
        );

        assertEquals(
                NULL_OR_EMPTY_TICKET_REQUESTS,
                exception.getMessage()
        );
    }

    private static Stream<Arguments> invalidTicketRequests() {
        return Stream.of(
                arguments(null, NULL_TICKET_REQUEST),
                arguments(new TicketTypeRequest(null, 1), NULL_TICKET_TYPE),
                arguments(new TicketTypeRequest(Type.ADULT, 0), INVALID_NO_OF_TICKETS),
                arguments(new TicketTypeRequest(Type.ADULT, -1), INVALID_NO_OF_TICKETS)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidTicketRequests")
    void shouldRejectInvalidTicketRequests(
            TicketTypeRequest request,
            String expectedErrorMessage) {

        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateRequests(
                        new TicketTypeRequest[]{request}
                )
        );

        assertEquals(
                expectedErrorMessage,
                exception.getMessage()
        );
    }

    @Test
    void shouldAcceptSingleValidTicketRequest() {
        assertDoesNotThrow(
                () -> validator.validateRequests(
                        new TicketTypeRequest(Type.ADULT, 1)
                )
        );
    }

    @Test
    void shouldAcceptMultipleValidTicketRequests() {
        assertDoesNotThrow(
                () -> validator.validateRequests(
                        new TicketTypeRequest(Type.ADULT, 2),
                        new TicketTypeRequest(Type.CHILD, 3),
                        new TicketTypeRequest(Type.INFANT, 1)
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "17, 6, 5",
            "26, 0, 0"
    })
    void shouldRejectTicketCountExceedingMaxTicketLimit(
            int adults,
            int children,
            int infants) {
        TicketTypeCounts counts = new TicketTypeCounts(adults, children, infants);

        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validatePurchase(counts)
        );

        assertEquals(
                TICKET_LIMIT_EXCEEDED,
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @CsvSource({
            "10, 0, 0",
            "5, 5, 2",
            "20, 4, 1",
            "25, 0, 0"
    })
    void shouldAcceptTicketCountsWithinAllowedLimit(
            int adults,
            int children,
            int infants) {
        TicketTypeCounts counts = new TicketTypeCounts(adults, children, infants);

        assertDoesNotThrow(
                () -> validator.validatePurchase(counts)
        );
    }
}

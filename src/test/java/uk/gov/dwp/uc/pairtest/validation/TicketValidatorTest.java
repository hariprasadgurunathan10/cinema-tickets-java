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

    /**
     * Verifies that an account ID is required.
     * A null account ID must be rejected with the expected validation error message.
     */
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

    /**
     * Verifies that account IDs must be greater than zero.
     * Zero and negative account IDs must be rejected with the expected validation error message.
     */
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

    /**
     * Verifies that a positive account ID is accepted.
     * The valid account ID of 1 must pass validation without throwing an exception.
     */
    @Test
    void shouldAcceptPositiveAccountId() {
        assertDoesNotThrow(() -> validator.validateAccount(1L));
    }

    /**
     * Verifies that at least one ticket request is required.
     * Null or empty ticket request arrays must be rejected with the expected validation error message.
     */
    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectNullAndEmptyTicketTypeRequests(TicketTypeRequest[] ticketTypeRequests) {
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateTicketRequests(ticketTypeRequests)
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
                arguments(new TicketTypeRequest(Type.ADULT, -1), INVALID_NO_OF_TICKETS),
                arguments(new TicketTypeRequest(Type.ADULT, Integer.MIN_VALUE), INVALID_NO_OF_TICKETS),
                arguments(new TicketTypeRequest(Type.ADULT, 26), TICKET_LIMIT_EXCEEDED),
                arguments(new TicketTypeRequest(Type.CHILD, Integer.MAX_VALUE), TICKET_LIMIT_EXCEEDED)
        );
    }

    /**
     * Verifies that ticket requests must have a valid ticket type and a ticket count between 1 and 25.
     * Null requests, missing ticket types, and counts outside this range must be rejected
     * with the expected validation error message.
     */
    @ParameterizedTest
    @MethodSource("invalidTicketRequests")
    void shouldRejectInvalidTicketRequests(
            TicketTypeRequest ticketTypeRequest,
            String expectedErrorMessage) {

        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateTicketRequests(
                        new TicketTypeRequest[]{ticketTypeRequest}
                )
        );

        assertEquals(
                expectedErrorMessage,
                exception.getMessage()
        );
    }

    /**
     * Verifies that a single ticket request with a valid type and quantity is accepted.
     * A request for one adult ticket must pass validation without throwing an exception.
     */
    @Test
    void shouldAcceptSingleValidTicketRequest() {
        assertDoesNotThrow(
                () -> validator.validateTicketRequests(
                        new TicketTypeRequest[]{new TicketTypeRequest(Type.ADULT, 1)}
                )
        );
    }

    /**
     * Verifies that multiple ticket requests with valid types and quantities are accepted.
     * A combination of adult, child, and infant tickets must pass validation
     * without throwing an exception.
     */
    @Test
    void shouldAcceptMultipleValidTicketRequests() {
        assertDoesNotThrow(
                () -> validator.validateTicketRequests(
                        new TicketTypeRequest[]{new TicketTypeRequest(Type.ADULT, 2),
                        new TicketTypeRequest(Type.CHILD, 3),
                        new TicketTypeRequest(Type.INFANT, 1)}
                )
        );
    }

    /**
     * Verifies that a purchase cannot exceed the maximum limit of 25 tickets.
     * Purchases exceeding this limit must be rejected with the expected validation error message.
     */
    @ParameterizedTest
    @CsvSource({
            "17, 6, 5",
            "26, 0, 0"
    })
    void shouldRejectTicketCountExceedingMaxTicketLimit(
            int adultTicketCount,
            int childTicketCount,
            int infantTicketCount) {
        TicketTypeCounts ticketTypeCounts = new TicketTypeCounts(adultTicketCount, childTicketCount, infantTicketCount);

        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateTicketCounts(ticketTypeCounts)
        );

        assertEquals(
                TICKET_LIMIT_EXCEEDED,
                exception.getMessage()
        );
    }

    /**
     * Verifies that valid ticket combinations within the maximum ticket limit of 25
     * are accepted without throwing an exception.
     */
    @ParameterizedTest
    @CsvSource({
            "10, 0, 0",
            "5, 5, 2",
            "20, 4, 1",
            "25, 0, 0"
    })
    void shouldAcceptTicketCountsWithinAllowedLimit(
            int adultTicketCount,
            int childTicketCount,
            int infantTicketCount) {
        TicketTypeCounts ticketTypeCounts = new TicketTypeCounts(adultTicketCount, childTicketCount, infantTicketCount);

        assertDoesNotThrow(
                () -> validator.validateTicketCounts(ticketTypeCounts)
        );
    }

    /**
     * Verifies the requirement for adult accompaniment.
     * Purchases containing child or infant tickets without an adult ticket must be rejected.
     */
    @ParameterizedTest
    @CsvSource({
            "0, 2, 0",
            "0, 0, 1",
            "0, 3, 2"
    })
    void shouldRejectChildOrInfantTicketsWithoutAdult(
            int adultTicketCount,
            int childTicketCount,
            int infantTicketCount) {

        TicketTypeCounts ticketTypeCounts = new TicketTypeCounts(adultTicketCount, childTicketCount, infantTicketCount);

        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateTicketCounts(ticketTypeCounts)
        );

        assertEquals(
                ADULT_TICKET_REQUIRED,
                exception.getMessage()
        );
    }

    /**
     * Verifies the requirement for one adult per infant.
     * Purchases containing more infant tickets than adult tickets must be rejected.
     */
    @ParameterizedTest
    @CsvSource({
            "1, 0, 2",
            "2, 5, 4"
    })
    void shouldRejectInfantCountExceedingAdultCount(
            int adultTicketCount,
            int childTicketCount,
            int infantTicketCount) {

        TicketTypeCounts ticketTypeCounts = new TicketTypeCounts(adultTicketCount, childTicketCount, infantTicketCount);

        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> validator.validateTicketCounts(ticketTypeCounts)
        );

        assertEquals(
                INFANT_COUNT_EXCEEDS_ADULT_COUNT,
                exception.getMessage()
        );
    }

    /**
     * Verifies the requirement for one adult per infant.
     * Purchases with infant ticket counts equal to or below adult ticket counts
     * must be accepted.
     */
    @ParameterizedTest
    @CsvSource({
            "2, 0, 2",
            "5, 3, 2",
            "10, 5, 0"
    })
    void shouldAcceptInfantCountNotExceedingAdultCount(
            int adultTicketCount,
            int childTicketCount,
            int infantTicketCount) {

        TicketTypeCounts ticketTypeCounts = new TicketTypeCounts(adultTicketCount, childTicketCount, infantTicketCount);

        assertDoesNotThrow(
                () -> validator.validateTicketCounts(ticketTypeCounts)
        );
    }

}

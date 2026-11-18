package uk.gov.dwp.uc.pairtest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.*;

public class TicketServiceImplTest {

    private final TicketPaymentService paymentService =
            mock(TicketPaymentService.class);

    private final SeatReservationService seatReservationService =
            mock(SeatReservationService.class);

    private final TicketServiceImpl ticketService =
            new TicketServiceImpl(paymentService, seatReservationService);

    private static Stream<Arguments> validTicketPurchases() {
        return Stream.of(
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 2)
                        },
                        50, 2
                ),
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 2),
                                new TicketTypeRequest(Type.CHILD, 3),
                                new TicketTypeRequest(Type.INFANT, 1)
                        },
                        95, 5
                ),
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 1),
                                new TicketTypeRequest(Type.INFANT, 1)
                        },
                        25, 1
                ),
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 10),
                                new TicketTypeRequest(Type.CHILD, 10),
                                new TicketTypeRequest(Type.INFANT, 5)
                        },
                        400, 20
                ),
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 25)
                        },
                        625, 25
                ),
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 1),
                                new TicketTypeRequest(Type.ADULT, 2),
                                new TicketTypeRequest(Type.CHILD, 1)
                        },
                        90, 4
                )
        );
    }

    /**
     * Verifies that valid purchases invoke payment and seat reservation services.
     * The payment service should receive the calculated amount, while the seat reservation
     * service should receive the required number of seats.
     */
    @ParameterizedTest
    @MethodSource("validTicketPurchases")
    void shouldProcessValidTicketPurchases(
            TicketTypeRequest[] ticketTypeRequests,
            int expectedAmountToPay,
            int expectedSeatsToReserve) {

        long accountId = 123L;

        ticketService.purchaseTickets(accountId, ticketTypeRequests);

        verify(paymentService).makePayment(accountId, expectedAmountToPay);
        verify(seatReservationService).reserveSeat(accountId, expectedSeatsToReserve);
    }


    private static Stream<Arguments> invalidTicketPurchases() {
        return Stream.of(
                arguments(
                        null,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 1)
                        },
                        NULL_ACCOUNT_ID
                ),
                arguments(
                        0L,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 1)
                        },
                        INVALID_ACCOUNT_ID
                ),
                arguments(
                        -1L,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 1)
                        },
                        INVALID_ACCOUNT_ID
                ),
                arguments(
                        123L,
                        (TicketTypeRequest[]) null,
                        NULL_OR_EMPTY_TICKET_REQUESTS
                ),
                arguments(
                        123L,
                        new TicketTypeRequest[0],
                        NULL_OR_EMPTY_TICKET_REQUESTS
                ),
                arguments(
                        123L,
                        new TicketTypeRequest[]{null},
                        NULL_TICKET_REQUEST
                ),
                arguments(
                        123L,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(null, 1)
                        },
                        NULL_TICKET_TYPE
                ),
                arguments(
                        123L,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 0)
                        },
                        INVALID_NO_OF_TICKETS
                ),
                arguments(
                        123L,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, -1)
                        },
                        INVALID_NO_OF_TICKETS
                ),
                arguments(
                        123L,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, Integer.MIN_VALUE)
                        },
                        INVALID_NO_OF_TICKETS
                ),
                arguments(
                        123L,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 26)
                        },
                        TICKET_LIMIT_EXCEEDED
                ),
                arguments(
                        123L,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 20),
                                new TicketTypeRequest(Type.CHILD, Integer.MAX_VALUE)
                        },
                        TICKET_LIMIT_EXCEEDED
                ),
                arguments(
                        123L,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.CHILD, 2)
                        },
                        ADULT_TICKET_REQUIRED
                ),
                arguments(
                        123L,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.INFANT, 1)
                        },
                        ADULT_TICKET_REQUIRED
                ),
                arguments(
                        123L,
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 1),
                                new TicketTypeRequest(Type.INFANT, 2)
                        },
                        INFANT_COUNT_EXCEEDS_ADULT_COUNT
                )
        );
    }

    /**
     * Verifies that invalid ticket purchases are rejected during validation with the expected error message.
     * Payment and seat reservation services must not be called for rejected purchases.
     */
    @ParameterizedTest
    @MethodSource("invalidTicketPurchases")
    void shouldRejectInvalidPurchasesWithoutCallingExternalServices(
            Long accountId,
            TicketTypeRequest[] ticketTypeRequests,
            String expectedErrorMessage) {

        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> ticketService.purchaseTickets(accountId, ticketTypeRequests)
        );

        assertEquals(expectedErrorMessage, exception.getMessage());

        verifyNoInteractions(paymentService, seatReservationService);
    }

}

package uk.gov.dwp.uc.pairtest;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

public class TicketServiceImplTest {

    private final TicketPaymentService paymentService =
            mock(TicketPaymentService.class);

    private final SeatReservationService seatReservationService =
            mock(SeatReservationService.class);

    private final TicketServiceImpl ticketService =
            new TicketServiceImpl(paymentService, seatReservationService);

    @Test
    void shouldProcessValidTicketPurchase() {
        long accountId = 123L;

        TicketTypeRequest request =
                new TicketTypeRequest(Type.ADULT, 2);

        ticketService.purchaseTickets(accountId, request);

        verify(paymentService).makePayment(accountId, 50);
        verify(seatReservationService).reserveSeat(accountId, 2);
    }

    private static Stream<Arguments> validTicketPurchases() {
        return Stream.of(
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

    @ParameterizedTest
    @MethodSource("validTicketPurchases")
    void shouldProcessValidTicketPurchases(
            TicketTypeRequest[] requests,
            int expectedAmount,
            int expectedSeats) {

        long accountId = 123L;

        ticketService.purchaseTickets(accountId, requests);

        verify(paymentService).makePayment(accountId, expectedAmount);
        verify(seatReservationService).reserveSeat(accountId, expectedSeats);
    }

}

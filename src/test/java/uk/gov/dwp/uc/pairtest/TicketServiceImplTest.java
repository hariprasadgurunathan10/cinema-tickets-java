package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
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
}

package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.PurchaseTotals;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.TicketValidator;

/**
 * Implements {@link TicketService} by coordinating ticket validation,
 * calculating purchase totals, processing payment, and reserving seats.
 */
public class TicketServiceImpl implements TicketService {

    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;
    private final TicketValidator ticketValidator;
    private final TicketTypeAggregator ticketTypeAggregator;
    private final TicketPurchaseCalculator ticketPurchaseCalculator;

    /**
     * @param paymentService         the service used to process payments
     * @param seatReservationService the service used to reserve seats
     */
    public TicketServiceImpl(
            TicketPaymentService paymentService,
            SeatReservationService seatReservationService) {

        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
        this.ticketValidator = new TicketValidator();
        this.ticketTypeAggregator = new TicketTypeAggregator();
        this.ticketPurchaseCalculator = new TicketPurchaseCalculator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void purchaseTickets(
            Long accountId,
            TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException {
        ticketValidator.validateAccount(accountId);
        ticketValidator.validateTicketRequests(ticketTypeRequests);

        TicketTypeCounts ticketTypeCounts = ticketTypeAggregator.aggregateTicketCountsByType(ticketTypeRequests);
        ticketValidator.validateTicketCounts(ticketTypeCounts);

        PurchaseTotals purchaseTotals = ticketPurchaseCalculator.calculatePurchaseTotals(ticketTypeCounts);

        paymentService.makePayment(accountId, purchaseTotals.amountToPay());
        seatReservationService.reserveSeat(accountId, purchaseTotals.seatsToReserve());
    }

}

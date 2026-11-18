package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.PurchaseTotals;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.TicketValidator;

public class TicketServiceImpl implements TicketService {

    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;
    private final TicketValidator ticketValidator;
    private final TicketTypeAggregator ticketTypeAggregator;
    private final TicketPurchaseCalculator ticketPurchaseCalculator;

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
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(
            Long accountId,
            TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException {
        ticketValidator.validateAccount(accountId);
        ticketValidator.validateRequests(ticketTypeRequests);

        TicketTypeCounts ticketTypeCounts = ticketTypeAggregator.aggregate(ticketTypeRequests);
        ticketValidator.validatePurchase(ticketTypeCounts);

        PurchaseTotals purchaseTotals = ticketPurchaseCalculator.calculate(ticketTypeCounts);

        paymentService.makePayment(accountId, purchaseTotals.amountToPay());
        seatReservationService.reserveSeat(accountId, purchaseTotals.seatsToReserve());
    }

}

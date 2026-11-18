package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.PurchaseTotals;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.ADULT_TICKET_PRICE;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.CHILD_TICKET_PRICE;

/**
 * Calculates the total payment amount and number of seats required
 * for a ticket purchase.
 */
public class TicketPurchaseCalculator {

    /**
     * Calculates purchase totals from validated ticket counts.
     * Infant tickets are free and do not require seats.
     *
     * @param ticketTypeCounts the validated ticket counts for each type
     * @return the total amount to pay and the number of seats to reserve
     */
    public PurchaseTotals calculatePurchaseTotals(TicketTypeCounts ticketTypeCounts) {
        int adultTicketCount = ticketTypeCounts.adults();
        int childTicketCount = ticketTypeCounts.children();

        int totalAmountToPay =
                (adultTicketCount * ADULT_TICKET_PRICE)
                        + (childTicketCount * CHILD_TICKET_PRICE);

        int totalSeatsToReserve =
                adultTicketCount + childTicketCount;

        return new PurchaseTotals(
                totalAmountToPay,
                totalSeatsToReserve
        );
    }
}

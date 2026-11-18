package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.PurchaseTotals;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.ADULT_TICKET_PRICE;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.CHILD_TICKET_PRICE;

public class TicketPurchaseCalculator {
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

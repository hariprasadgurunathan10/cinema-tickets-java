package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.PurchaseTotals;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.ADULT_PRICE;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.CHILD_PRICE;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.INFANT_PRICE;

public class TicketPurchaseCalculator {
    public PurchaseTotals calculate(TicketTypeCounts counts) {
        int adultTicketCount = counts.adults();
        int childrenTicketCount = counts.children();

        int totalAmountToPay =
                (adultTicketCount * ADULT_PRICE)
                        + (childrenTicketCount * CHILD_PRICE);

        int totalSeatsToReserve =
                adultTicketCount + childrenTicketCount;

        return new PurchaseTotals(
                totalAmountToPay,
                totalSeatsToReserve
        );
    }
}

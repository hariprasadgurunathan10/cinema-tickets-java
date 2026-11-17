package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.PurchaseTotals;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;

public class TicketPurchaseCalculator {
    public PurchaseTotals calculate(TicketTypeCounts counts) {
        return new PurchaseTotals(0, 0);
    }
}

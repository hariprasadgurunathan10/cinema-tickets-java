package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.PurchaseTotals;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketPurchaseCalculatorTest {
    private final TicketPurchaseCalculator calculator = new TicketPurchaseCalculator();

    @Test
    void shouldCalculateAdultTicketPriceAndSeats() {
        TicketTypeCounts counts = new TicketTypeCounts(2, 0, 0);

        PurchaseTotals totals = calculator.calculate(counts);

        assertEquals(50, totals.amountToPay());
        assertEquals(2, totals.seatsToReserve());
    }
}

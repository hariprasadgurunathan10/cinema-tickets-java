package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.gov.dwp.uc.pairtest.domain.PurchaseTotals;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketPurchaseCalculatorTest {
    private final TicketPurchaseCalculator calculator = new TicketPurchaseCalculator();


    @ParameterizedTest
    @CsvSource({
            "2, 0, 0, 50, 2",
            "2, 2, 0, 80, 4",
            "1, 0, 1, 25, 1",
            "2, 3, 1, 95, 5"
    })
    void shouldCalculatePurchaseTotals(
            int adults,
            int children,
            int infants,
            int expectedAmount,
            int expectedSeats) {

        TicketTypeCounts counts = new TicketTypeCounts(adults, children, infants);

        PurchaseTotals totals = calculator.calculate(counts);

        assertEquals(expectedAmount, totals.amountToPay());
        assertEquals(expectedSeats, totals.seatsToReserve());
    }
}

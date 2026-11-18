package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.gov.dwp.uc.pairtest.domain.PurchaseTotals;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketPurchaseCalculatorTest {
    private final TicketPurchaseCalculator calculator = new TicketPurchaseCalculator();


    /**
     * Verifies total ticket price and number of seats calculations for different ticket combinations.
     * Infant tickets should not contribute to the total price or require reserved seats.
     */
    @ParameterizedTest
    @CsvSource({
            "2, 0, 0, 50, 2",
            "2, 2, 0, 80, 4",
            "1, 0, 1, 25, 1",
            "2, 3, 1, 95, 5"
    })
    void shouldCalculatePurchaseTotals(
            int adultTicketCount,
            int childTicketCount,
            int infantTicketCount,
            int expectedAmountToPay,
            int expectedSeatsToReserve) {

        TicketTypeCounts ticketTypeCounts = new TicketTypeCounts(adultTicketCount, childTicketCount, infantTicketCount);

        PurchaseTotals purchaseTotals = calculator.calculatePurchaseTotals(ticketTypeCounts);

        assertEquals(expectedAmountToPay, purchaseTotals.amountToPay());
        assertEquals(expectedSeatsToReserve, purchaseTotals.seatsToReserve());
    }
}

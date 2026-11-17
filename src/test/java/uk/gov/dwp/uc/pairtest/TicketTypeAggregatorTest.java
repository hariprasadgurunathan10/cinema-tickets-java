package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public class TicketTypeAggregatorTest {
    private final TicketTypeAggregator aggregator = new TicketTypeAggregator();

    @Test
    void shouldAggregateAdultTicketCounts() {
        TicketTypeCounts counts = aggregator.aggregate(
                new TicketTypeRequest(Type.ADULT, 2)
        );

        assertEquals(2, counts.getAdults());
        assertEquals(0, counts.getChildren());
        assertEquals(0, counts.getInfants());
    }
}

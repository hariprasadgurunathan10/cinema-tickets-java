package uk.gov.dwp.uc.pairtest;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

public class TicketTypeAggregatorTest {
    private final TicketTypeAggregator aggregator = new TicketTypeAggregator();

    private static Stream<Arguments> ticketRequests() {
        return Stream.of(
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 2)
                        },
                        2, 0, 0
                ),
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.CHILD, 3)
                        },
                        0, 3, 0
                ),
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.INFANT, 2)
                        },
                        0, 0, 2
                ),
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 2),
                                new TicketTypeRequest(Type.CHILD, 3),
                                new TicketTypeRequest(Type.INFANT, 1)
                        },
                        2, 3, 1
                ),
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.ADULT, 5),
                                new TicketTypeRequest(Type.ADULT, 3),
                                new TicketTypeRequest(Type.CHILD, 4),
                                new TicketTypeRequest(Type.CHILD, 1),
                                new TicketTypeRequest(Type.INFANT, 5),
                                new TicketTypeRequest(Type.INFANT, 2)
                        },
                        8, 5, 7
                ),
                arguments(
                        new TicketTypeRequest[]{
                                new TicketTypeRequest(Type.INFANT, 1),
                                new TicketTypeRequest(Type.CHILD, 3),
                                new TicketTypeRequest(Type.ADULT, 2)
                        },
                        2, 3, 1
                )
        );
    }

    @ParameterizedTest
    @MethodSource("ticketRequests")
    void shouldAggregateTicketCounts(
            TicketTypeRequest[] requests,
            int expectedAdults,
            int expectedChildren,
            int expectedInfants) {

        TicketTypeCounts counts = aggregator.aggregate(requests);

        assertEquals(expectedAdults, counts.adults());
        assertEquals(expectedChildren, counts.children());
        assertEquals(expectedInfants, counts.infants());
    }
}

package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

/**
 * Aggregates ticket counts from multiple requests into separate totals
 * for adult, child, and infant tickets.
 */
public class TicketTypeAggregator {

    /**
     * Sums the total number of tickets requested for each ticket type.
     *
     * @param ticketTypeRequests the ticket requests to aggregate
     * @return the total count of adult, child, and infant tickets
     */
    public TicketTypeCounts aggregateTicketCountsByType(TicketTypeRequest... ticketTypeRequests){
        int adultTicketCount = 0;
        int childTicketCount = 0;
        int infantTicketCount = 0;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
            switch (ticketTypeRequest.getTicketType()) {
                case ADULT -> adultTicketCount += ticketTypeRequest.getNoOfTickets();
                case CHILD -> childTicketCount += ticketTypeRequest.getNoOfTickets();
                case INFANT -> infantTicketCount += ticketTypeRequest.getNoOfTickets();
            }
        }

        return new TicketTypeCounts(adultTicketCount, childTicketCount, infantTicketCount);
    }
}

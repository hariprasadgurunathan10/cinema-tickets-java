package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public class TicketTypeAggregator {
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

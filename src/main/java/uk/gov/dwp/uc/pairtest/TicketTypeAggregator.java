package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public class TicketTypeAggregator {
    public TicketTypeCounts aggregate(TicketTypeRequest... requests){
        int adults = 0;
        int children = 0;
        int infants = 0;

        for (TicketTypeRequest request : requests) {
            switch (request.getTicketType()) {
                case ADULT -> adults += request.getNoOfTickets();
                case CHILD -> children += request.getNoOfTickets();
                case INFANT -> infants += request.getNoOfTickets();
            }
        }

        return new TicketTypeCounts(adults, children, infants);
    }
}

package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public class TicketTypeAggregator {
    public TicketTypeCounts aggregate(TicketTypeRequest... requests){
        return new TicketTypeCounts(0, 0, 0);
    }
}

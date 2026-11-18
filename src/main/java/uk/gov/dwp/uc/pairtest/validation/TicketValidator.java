package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.*;

public class TicketValidator {

    public void validateAccount(Long accountId){
        if(accountId == null){
            throw new InvalidPurchaseException(NULL_ACCOUNT_ID);
        }

        if(accountId <= 0){
            throw new InvalidPurchaseException(INVALID_ACCOUNT_ID);
        }
    }

    public void validateTicketRequests(TicketTypeRequest... ticketTypeRequests){
        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException(NULL_OR_EMPTY_TICKET_REQUESTS);
        }

        for(TicketTypeRequest ticketTypeRequest : ticketTypeRequests){
            if(ticketTypeRequest == null){
                throw new InvalidPurchaseException(NULL_TICKET_REQUEST);
            }
            if(ticketTypeRequest.getTicketType() == null){
                throw new InvalidPurchaseException(NULL_TICKET_TYPE);
            }

            int noOfTickets = ticketTypeRequest.getNoOfTickets();
            if(noOfTickets <= 0){
                throw new InvalidPurchaseException(INVALID_NO_OF_TICKETS);
            }
            validateTicketLimit(noOfTickets);
        }
    }

    public void validateTicketCounts(TicketTypeCounts ticketTypeCounts) {
        int adultTicketCount = ticketTypeCounts.adults();
        int childTicketCount = ticketTypeCounts.children();
        int infantTicketCount = ticketTypeCounts.infants();

        validateTicketLimit(adultTicketCount + childTicketCount + infantTicketCount);

        if (adultTicketCount == 0
                && (childTicketCount > 0 || infantTicketCount > 0)) {
            throw new InvalidPurchaseException(ADULT_TICKET_REQUIRED);
        }

        if (infantTicketCount > adultTicketCount) {
            throw new InvalidPurchaseException(INFANT_COUNT_EXCEEDS_ADULT_COUNT);
        }
    }

    private void validateTicketLimit(int ticketCount) {
        if (ticketCount > MAX_TICKET_LIMIT) {
            throw new InvalidPurchaseException(TICKET_LIMIT_EXCEEDED);
        }
    }
}

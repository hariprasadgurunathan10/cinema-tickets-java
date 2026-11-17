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

    public void validateRequests(TicketTypeRequest... requests){
        if (requests == null || requests.length == 0) {
            throw new InvalidPurchaseException(NULL_OR_EMPTY_TICKET_REQUESTS);
        }

        for(TicketTypeRequest request : requests){
            if(request == null){
                throw new InvalidPurchaseException(NULL_TICKET_REQUEST);
            }
            if(request.getTicketType() == null){
                throw new InvalidPurchaseException(NULL_TICKET_TYPE);
            }
            if(request.getNoOfTickets() <= 0){
                throw new InvalidPurchaseException(INVALID_NO_OF_TICKETS);
            }
        }
    }

    public void validatePurchase(TicketTypeCounts counts) {
        int adultTicketCount = counts.adults();
        int childrenTicketCount = counts.children();
        int infantTicketCount = counts.infants();
        int totalTicketCount = adultTicketCount + childrenTicketCount + infantTicketCount;

        if(totalTicketCount > MAX_TICKET_LIMIT) {
            throw new InvalidPurchaseException(TICKET_LIMIT_EXCEEDED);
        }

        if (adultTicketCount == 0
                && (childrenTicketCount > 0 || infantTicketCount > 0)) {
            throw new InvalidPurchaseException(ADULT_TICKET_REQUIRED);
        }

        if (infantTicketCount > adultTicketCount) {
            throw new InvalidPurchaseException(INFANT_COUNT_EXCEEDS_ADULT_COUNT);
        }
    }
}

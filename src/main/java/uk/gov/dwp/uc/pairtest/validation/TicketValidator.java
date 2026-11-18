package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.*;

/**
 * Validates account IDs, ticket requests and aggregated ticket counts.
 */
public class TicketValidator {

    /**
     * Validates that the account ID is present and is greater than zero.
     * 
     * @param accountId the account ID to validate
     * @throws InvalidPurchaseException if the ID is null or not greater than zero
     */
    public void validateAccount(Long accountId){
        if(accountId == null){
            throw new InvalidPurchaseException(NULL_ACCOUNT_ID);
        }

        if(accountId <= 0){
            throw new InvalidPurchaseException(INVALID_ACCOUNT_ID);
        }
    }

    /**
     * Validates that ticket requests are present and contain a valid ticket type
     * and a positive ticket count within the maximum ticket limit.
     *
     * @param ticketTypeRequests the ticket requests to validate
     * @throws InvalidPurchaseException if the ticket requests are null or empty,
     *                                  or any ticket request violates a validation rule
     */
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

    /**
     * Validates that ticket counts are not null, the total does not exceed the ticket limit, 
     * that child or infant tickets are accompanied by an adult, and that infants do not outnumber adults.
     *
     * @param ticketTypeCounts the aggregated ticket counts to validate
     * @throws InvalidPurchaseException if any purchase validation rule is violated
     */
    public void validateTicketCounts(TicketTypeCounts ticketTypeCounts) {
        if (ticketTypeCounts == null) {
            throw new InvalidPurchaseException(NULL_TICKET_COUNTS);
        }

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

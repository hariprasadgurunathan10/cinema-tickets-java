package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

/**
 * Defines the service for purchasing cinema tickets.
 */
public interface TicketService {

    /**
     * Validates a ticket purchase, calculates the total cost and required seats,
     * then processes payment and reserves the seats.
     * 
     * @param accountId          the account making the purchase; must be greater than zero
     * @param ticketTypeRequests one or more requests describing ticket type and quantity
     * @throws InvalidPurchaseException if the account or ticket requests violates the validation or purchase rules
     */
    void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException;

}

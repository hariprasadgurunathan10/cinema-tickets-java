package uk.gov.dwp.uc.pairtest.domain;

/**
 * Represents an immutable request for a specified number of tickets of a single type.
 */
public final class TicketTypeRequest {

    private final int noOfTickets;
    private final Type type;

    /**
     * Creates a new ticket type request with the specified type and quantity.
     * @param type        the ticket type
     * @param noOfTickets the number of tickets requested
     */
    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    /**
     * @return the number of tickets requested
     */
    public int getNoOfTickets() {
        return noOfTickets;
    }

    /**
     * @return the requested ticket type
     */
    public Type getTicketType() {
        return type;
    }

    /**
     * Supported ticket types.
     */
    public enum Type {
        ADULT, CHILD , INFANT
    }

}

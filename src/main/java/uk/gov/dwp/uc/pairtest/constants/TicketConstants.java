package uk.gov.dwp.uc.pairtest.constants;

public final class TicketConstants {

    private TicketConstants(){
        // Prevents instantiation
    }

    // Error messages
    public static final String NULL_ACCOUNT_ID = "Enter an account ID";
    public static final String INVALID_ACCOUNT_ID = "Account ID must be greater than 0";
    public static final String NULL_OR_EMPTY_TICKET_REQUESTS = "At least one ticket request is required";
    public static final String NULL_TICKET_REQUEST = "Enter a ticket type and quantity for each request";
    public static final String NULL_TICKET_TYPE = "Enter a ticket type";
    public static final String INVALID_NO_OF_TICKETS = "Number of tickets must be greater than 0";
    public static final String TICKET_LIMIT_EXCEEDED = "You cannot purchase more than 25 tickets at a time";
    public static final String ADULT_TICKET_REQUIRED =
            "At least one adult ticket is required when purchasing child or infant tickets";
    public static final String INFANT_COUNT_EXCEEDS_ADULT_COUNT =
            "Each infant must be accompanied by a separate adult";

    // Ticket purchase limits
    public static final int MAX_TICKET_LIMIT = 25;

    // Ticket prices based on ticket type
    public static final int ADULT_PRICE = 25;
    public static final int CHILD_PRICE = 15;
    public static final int INFANT_PRICE = 0;
}

package uk.gov.dwp.uc.pairtest.constants;

public final class TicketConstants {

    private TicketConstants(){
        // Prevents instantiation
    }

    // Error messages
    public static final String NULL_ACCOUNT_ID = "Enter an account ID";
    public static final String INVALID_ACCOUNT_ID = "Account ID must be greater than 0";
    public static final String NO_TICKET_TYPE_REQUEST = "At least one ticket request is required";;
}

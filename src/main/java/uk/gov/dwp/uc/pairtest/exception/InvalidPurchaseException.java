package uk.gov.dwp.uc.pairtest.exception;

/**
 * Thrown when a ticket purchase request violates a business or validation rule.
 */
public class InvalidPurchaseException extends RuntimeException {

    /**
     * Constructs a new InvalidPurchaseException with the specified error message.
     * 
     * @param errorMessage description of why the purchase is invalid
     */
    public InvalidPurchaseException(String errorMessage) {
        super(errorMessage);
    }
}

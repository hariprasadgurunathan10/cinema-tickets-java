package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import static uk.gov.dwp.uc.pairtest.constants.TicketConstants.NULL_ACCOUNT_ID;

public class TicketValidator {

    public void validateAccount(Long accountId){
        if(accountId == null){
            throw new InvalidPurchaseException(NULL_ACCOUNT_ID);
        }
    }
}

package uk.gov.dwp.uc.pairtest.domain;

/**
 * Represents the calculated payment amount and total number of seats required for a ticket purchase.
 *
 * @param amountToPay    total amount to charge in GBP
 * @param seatsToReserve total number of seats to reserve
 */
public record PurchaseTotals(
        int amountToPay,
        int seatsToReserve
) {}

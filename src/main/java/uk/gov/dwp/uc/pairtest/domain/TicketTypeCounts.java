package uk.gov.dwp.uc.pairtest.domain;

/**
 * Represents the aggregated number of tickets for each ticket type.
 *
 * @param adults   number of adult tickets
 * @param children number of child tickets
 * @param infants  number of infant tickets
 */
public record TicketTypeCounts(
        int adults,
        int children,
        int infants
) {}

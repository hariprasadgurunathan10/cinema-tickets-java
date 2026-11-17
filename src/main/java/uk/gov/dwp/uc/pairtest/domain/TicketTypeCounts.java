package uk.gov.dwp.uc.pairtest.domain;

public final class TicketTypeCounts {
    private final int adults;
    private final int children;
    private final int infants;

    public TicketTypeCounts(int adults, int children, int infants) {
        this.adults = adults;
        this.children = children;
        this.infants = infants;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public int getInfants() {
        return infants;
    }
}

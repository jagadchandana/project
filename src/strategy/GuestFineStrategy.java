package strategy;

import model.MembershipType;

public class GuestFineStrategy implements FineCalculationStrategy {
    
    @Override
    public double calculateFine(long overdueDays) {
        return overdueDays * 100.0;
    }

    @Override
    public MembershipType getMembershipType() {
        return MembershipType.GUEST;
    }
}

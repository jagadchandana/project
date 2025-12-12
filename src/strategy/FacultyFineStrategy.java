package strategy;

import model.MembershipType;

public class FacultyFineStrategy implements FineCalculationStrategy {
    
    @Override
    public double calculateFine(long overdueDays) {
        return overdueDays * 20.0;
    }

    @Override
    public MembershipType getMembershipType() {
        return MembershipType.FACULTY;
    }
}

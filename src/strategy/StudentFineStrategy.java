package strategy;

import model.MembershipType;

public class StudentFineStrategy implements FineCalculationStrategy {
    
    @Override
    public double calculateFine(long overdueDays) {
        return overdueDays * 50.0;
    }

    @Override
    public MembershipType getMembershipType() {
        return MembershipType.STUDENT;
    }
}

package strategy;

import model.MembershipType;

public class FineCalculator {
    private FineCalculationStrategy strategy;

    public FineCalculator(MembershipType membershipType) {
        setStrategy(membershipType);
    }

    public void setStrategy(MembershipType membershipType) {
        switch (membershipType) {
            case STUDENT:
                this.strategy = new StudentFineStrategy();
                break;
            case FACULTY:
                this.strategy = new FacultyFineStrategy();
                break;
            case GUEST:
                this.strategy = new GuestFineStrategy();
                break;
        }
    }

    public double calculateFine(long overdueDays) {
        return strategy.calculateFine(overdueDays);
    }
}

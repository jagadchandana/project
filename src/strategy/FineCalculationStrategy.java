package strategy;

import model.MembershipType;

public interface FineCalculationStrategy {
    double calculateFine(long overdueDays);
    MembershipType getMembershipType();
}

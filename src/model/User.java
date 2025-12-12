package model;

import observer.Observer;
import java.util.ArrayList;
import java.util.List;

public class User implements Observer {
    private String userId;
    private String name;
    private String email;
    private String contactNumber;
    private MembershipType membershipType;
    private List<BorrowRecord> borrowedBooksHistory;
    private List<String> notifications;

    public User(String userId, String name, String email, String contactNumber, MembershipType membershipType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.membershipType = membershipType;
        this.borrowedBooksHistory = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public List<BorrowRecord> getBorrowedBooksHistory() {
        return borrowedBooksHistory;
    }

    public void addBorrowRecord(BorrowRecord record) {
        this.borrowedBooksHistory.add(record);
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public int getBorrowingLimit() {
        switch (membershipType) {
            case STUDENT:
                return 3;
            case FACULTY:
                return 5;
            case GUEST:
                return 1;
            default:
                return 0;
        }
    }

    public int getBorrowingPeriodDays() {
        switch (membershipType) {
            case STUDENT:
                return 14;
            case FACULTY:
                return 30;
            case GUEST:
                return 7;
            default:
                return 0;
        }
    }

    public long getCurrentActiveBorrowCount() {
        return borrowedBooksHistory.stream()
                .filter(record -> record.getReturnDate() == null)
                .count();
    }

    @Override
    public void update(String message) {
        notifications.add(message);
    }

    @Override
    public String toString() {
        return userId + " - " + name + " (" + membershipType + ")";
    }
}

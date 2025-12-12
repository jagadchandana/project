package manager;

import model.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReportGenerator {
    private LibraryManager libraryManager;

    public ReportGenerator(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
    }

    public Map<String, Integer> getMostBorrowedBooks() {
        Map<String, Integer> bookBorrowCount = new HashMap<>();
        
        for (Book book : libraryManager.getAllBooks()) {
            int count = book.getBorrowedHistory().size();
            bookBorrowCount.put(book.getTitle(), count);
        }
        
        return bookBorrowCount.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }

    public Map<String, Long> getActiveBorrowers() {
        Map<String, Long> userBorrowCount = new HashMap<>();
        
        for (User user : libraryManager.getAllUsers()) {
            long count = user.getCurrentActiveBorrowCount();
            if (count > 0) {
                userBorrowCount.put(user.getName(), count);
            }
        }
        
        return userBorrowCount.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }

    public List<Map<String, String>> getOverdueBooksReport() {
        List<Map<String, String>> overdueList = new ArrayList<>();
        List<BorrowRecord> overdueRecords = libraryManager.getOverdueRecords();
        
        for (BorrowRecord record : overdueRecords) {
            User user = libraryManager.getUser(record.getUserId());
            Book book = libraryManager.getBook(record.getBookId());
            
            if (user != null && book != null) {
                Map<String, String> entry = new HashMap<>();
                entry.put("User", user.getName());
                entry.put("Book", book.getTitle());
                entry.put("Due Date", record.getDueDate().toString());
                entry.put("Overdue Days", String.valueOf(record.getOverdueDays()));
                overdueList.add(entry);
            }
        }
        
        return overdueList;
    }

    public Map<String, Integer> getBooksByCategory() {
        Map<String, Integer> categoryCount = new HashMap<>();
        
        for (Book book : libraryManager.getAllBooks()) {
            String category = book.getCategory();
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
        }
        
        return categoryCount;
    }

    public Map<String, Long> getUsersByMembershipType() {
        Map<String, Long> membershipCount = new HashMap<>();
        
        membershipCount.put("STUDENT", libraryManager.getAllUsers().stream()
            .filter(u -> u.getMembershipType() == MembershipType.STUDENT)
            .count());
        membershipCount.put("FACULTY", libraryManager.getAllUsers().stream()
            .filter(u -> u.getMembershipType() == MembershipType.FACULTY)
            .count());
        membershipCount.put("GUEST", libraryManager.getAllUsers().stream()
            .filter(u -> u.getMembershipType() == MembershipType.GUEST)
            .count());
        
        return membershipCount;
    }

    public double getTotalFinesCollected() {
        return libraryManager.getAllBorrowRecords().stream()
            .mapToDouble(BorrowRecord::getFine)
            .sum();
    }

    public Map<String, Integer> getAvailabilityStatusCount() {
        Map<String, Integer> statusCount = new HashMap<>();
        statusCount.put("Available", 0);
        statusCount.put("Borrowed", 0);
        statusCount.put("Reserved", 0);
        
        for (Book book : libraryManager.getAllBooks()) {
            String status = book.getAvailabilityStatus();
            statusCount.put(status, statusCount.get(status) + 1);
        }
        
        return statusCount;
    }
}

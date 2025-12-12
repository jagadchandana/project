package manager;

import model.*;
import observer.NotificationManager;
import strategy.FineCalculator;
import state.AvailableState;
import state.BorrowedState;
import state.ReservedState;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibraryManager {
    private Map<String, Book> books;
    private Map<String, User> users;
    private List<BorrowRecord> borrowRecords;
    private List<Reservation> reservations;
    private NotificationManager notificationManager;
    private int recordIdCounter;
    private int reservationIdCounter;

    public LibraryManager() {
        this.books = new HashMap<>();
        this.users = new HashMap<>();
        this.borrowRecords = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.notificationManager = new NotificationManager();
        this.recordIdCounter = 1;
        this.reservationIdCounter = 1;
    }

    public void addBook(Book book) {
        books.put(book.getBookId(), book);
    }

    public void removeBook(String bookId) {
        books.remove(bookId);
    }

    public void updateBook(Book book) {
        books.put(book.getBookId(), book);
    }

    public Book getBook(String bookId) {
        return books.get(bookId);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
        notificationManager.attach(user);
    }

    public void removeUser(String userId) {
        User user = users.remove(userId);
        if (user != null) {
            notificationManager.detach(user);
        }
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean borrowBook(User user, Book book) {
        if (!book.getAvailabilityStatus().equals("Available")) {
            return false;
        }

        if (user.getCurrentActiveBorrowCount() >= user.getBorrowingLimit()) {
            return false;
        }

        book.getState().borrow(book);
        
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(user.getBorrowingPeriodDays());
        
        String recordId = "BR" + String.format("%04d", recordIdCounter++);
        BorrowRecord record = new BorrowRecord(recordId, user.getUserId(), book.getBookId(), borrowDate, dueDate);
        
        borrowRecords.add(record);
        user.addBorrowRecord(record);
        book.addToBorrowedHistory(user.getUserId());
        
        notificationManager.notifySpecificObserver(user, 
            "Book borrowed: " + book.getTitle() + ". Due date: " + dueDate);
        
        return true;
    }

    public boolean returnBook(User user, Book book) {
        BorrowRecord record = findActiveBorrowRecord(user.getUserId(), book.getBookId());
        if (record == null) {
            return false;
        }

        record.setReturnDate(LocalDate.now());

        if (record.isOverdue()) {
            long overdueDays = record.getOverdueDays();
            FineCalculator fineCalculator = new FineCalculator(user.getMembershipType());
            double fine = fineCalculator.calculateFine(overdueDays);
            record.setFine(fine);
            
            notificationManager.notifySpecificObserver(user, 
                "Book returned late. Fine: LKR " + fine);
        } else {
            notificationManager.notifySpecificObserver(user, 
                "Book returned: " + book.getTitle());
        }

        Reservation reservation = findReservationForBook(book.getBookId());
        if (reservation != null) {
            book.getState().returnBook(book);
            book.setState(new ReservedState());
            
            User reservedUser = users.get(reservation.getUserId());
            if (reservedUser != null) {
                notificationManager.notifySpecificObserver(reservedUser, 
                    "Reserved book available: " + book.getTitle());
                reservation.setNotified(true);
            }
        } else {
            book.getState().returnBook(book);
        }

        return true;
    }

    public boolean reserveBook(User user, Book book) {
        if (book.getAvailabilityStatus().equals("Available")) {
            return false;
        }

        if (hasActiveReservation(user.getUserId(), book.getBookId())) {
            return false;
        }

        String reservationId = "RS" + String.format("%04d", reservationIdCounter++);
        Reservation reservation = new Reservation(reservationId, user.getUserId(), book.getBookId(), LocalDate.now());
        reservations.add(reservation);

        if (book.getAvailabilityStatus().equals("Borrowed")) {
            book.getState().reserve(book);
        }

        notificationManager.notifySpecificObserver(user, 
            "Book reserved: " + book.getTitle());

        return true;
    }

    public boolean cancelReservation(User user, Book book) {
        Reservation reservation = findReservation(user.getUserId(), book.getBookId());
        if (reservation == null) {
            return false;
        }

        reservations.remove(reservation);

        if (book.getAvailabilityStatus().equals("Reserved") && 
            !hasOtherReservations(book.getBookId())) {
            book.setState(new BorrowedState());
        }

        notificationManager.notifySpecificObserver(user, 
            "Reservation cancelled: " + book.getTitle());

        return true;
    }

    private BorrowRecord findActiveBorrowRecord(String userId, String bookId) {
        return borrowRecords.stream()
            .filter(r -> r.getUserId().equals(userId) && 
                        r.getBookId().equals(bookId) && 
                        r.getReturnDate() == null)
            .findFirst()
            .orElse(null);
    }

    private Reservation findReservationForBook(String bookId) {
        return reservations.stream()
            .filter(r -> r.getBookId().equals(bookId) && !r.isNotified())
            .findFirst()
            .orElse(null);
    }

    private Reservation findReservation(String userId, String bookId) {
        return reservations.stream()
            .filter(r -> r.getUserId().equals(userId) && r.getBookId().equals(bookId))
            .findFirst()
            .orElse(null);
    }

    private boolean hasActiveReservation(String userId, String bookId) {
        return reservations.stream()
            .anyMatch(r -> r.getUserId().equals(userId) && r.getBookId().equals(bookId));
    }

    private boolean hasOtherReservations(String bookId) {
        return reservations.stream()
            .anyMatch(r -> r.getBookId().equals(bookId));
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return new ArrayList<>(borrowRecords);
    }

    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecords.stream()
            .filter(BorrowRecord::isOverdue)
            .collect(Collectors.toList());
    }

    public void checkAndNotifyOverdue() {
        List<BorrowRecord> overdueRecords = getOverdueRecords();
        for (BorrowRecord record : overdueRecords) {
            User user = users.get(record.getUserId());
            Book book = books.get(record.getBookId());
            if (user != null && book != null) {
                notificationManager.notifySpecificObserver(user, 
                    "OVERDUE: " + book.getTitle() + " - " + record.getOverdueDays() + " days overdue");
            }
        }
    }

    public void checkAndNotifyDueSoon() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<BorrowRecord> dueSoonRecords = borrowRecords.stream()
            .filter(r -> r.getReturnDate() == null && r.getDueDate().equals(tomorrow))
            .collect(Collectors.toList());

        for (BorrowRecord record : dueSoonRecords) {
            User user = users.get(record.getUserId());
            Book book = books.get(record.getBookId());
            if (user != null && book != null) {
                notificationManager.notifySpecificObserver(user, 
                    "DUE TOMORROW: " + book.getTitle());
            }
        }
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }
}

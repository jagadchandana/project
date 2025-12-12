package gui;

import manager.LibraryManager;
import model.*;
import builder.BookBuilder;
import java.time.LocalDate;

public class SampleDataInitializer {
    
    public static void initializeSampleData(LibraryManager libraryManager) {
        initializeBooks(libraryManager);
        initializeUsers(libraryManager);
        initializeBorrowRecords(libraryManager);
    }

    private static void initializeBooks(LibraryManager libraryManager) {
        BookBuilder builder1 = new BookBuilder("B001", "Clean Code", "Robert C. Martin");
        libraryManager.addBook(builder1.setCategory("Programming")
                                       .setIsbn("978-0132350884")
                                       .setEdition("1st Edition")
                                       .build());

        BookBuilder builder2 = new BookBuilder("B002", "Design Patterns", "Gang of Four");
        libraryManager.addBook(builder2.setCategory("Software Engineering")
                                       .setIsbn("978-0201633610")
                                       .setEdition("1st Edition")
                                       .build());

        BookBuilder builder3 = new BookBuilder("B003", "The Pragmatic Programmer", "Andrew Hunt");
        libraryManager.addBook(builder3.setCategory("Programming")
                                       .setIsbn("978-0135957059")
                                       .setEdition("2nd Edition")
                                       .build());

        BookBuilder builder4 = new BookBuilder("B004", "Introduction to Algorithms", "Thomas H. Cormen");
        libraryManager.addBook(builder4.setCategory("Computer Science")
                                       .setIsbn("978-0262033848")
                                       .setEdition("3rd Edition")
                                       .build());

        BookBuilder builder5 = new BookBuilder("B005", "Java: The Complete Reference", "Herbert Schildt");
        libraryManager.addBook(builder5.setCategory("Programming")
                                       .setIsbn("978-1260440232")
                                       .setEdition("11th Edition")
                                       .build());

        BookBuilder builder6 = new BookBuilder("B006", "Effective Java", "Joshua Bloch");
        libraryManager.addBook(builder6.setCategory("Programming")
                                       .setIsbn("978-0134685991")
                                       .setEdition("3rd Edition")
                                       .build());

        BookBuilder builder7 = new BookBuilder("B007", "Database System Concepts", "Abraham Silberschatz");
        libraryManager.addBook(builder7.setCategory("Database")
                                       .setIsbn("978-0078022159")
                                       .setEdition("7th Edition")
                                       .build());

        BookBuilder builder8 = new BookBuilder("B008", "Computer Networks", "Andrew S. Tanenbaum");
        libraryManager.addBook(builder8.setCategory("Networking")
                                       .setIsbn("978-0132126953")
                                       .setEdition("5th Edition")
                                       .build());

        BookBuilder builder9 = new BookBuilder("B009", "Operating System Concepts", "Abraham Silberschatz");
        libraryManager.addBook(builder9.setCategory("Operating Systems")
                                       .setIsbn("978-1118063330")
                                       .setEdition("9th Edition")
                                       .build());

        BookBuilder builder10 = new BookBuilder("B010", "Artificial Intelligence: A Modern Approach", "Stuart Russell");
        libraryManager.addBook(builder10.setCategory("AI")
                                        .setIsbn("978-0136042594")
                                        .setEdition("3rd Edition")
                                        .build());
    }

    private static void initializeUsers(LibraryManager libraryManager) {
        User user1 = new User("U001", "John Smith", "john.smith@university.edu", "0771234567", MembershipType.STUDENT);
        libraryManager.addUser(user1);

        User user2 = new User("U002", "Sarah Johnson", "sarah.j@university.edu", "0772345678", MembershipType.FACULTY);
        libraryManager.addUser(user2);

        User user3 = new User("U003", "Michael Brown", "michael.b@university.edu", "0773456789", MembershipType.STUDENT);
        libraryManager.addUser(user3);

        User user4 = new User("U004", "Emily Davis", "emily.d@university.edu", "0774567890", MembershipType.FACULTY);
        libraryManager.addUser(user4);

        User user5 = new User("U005", "David Wilson", "david.w@guest.com", "0775678901", MembershipType.GUEST);
        libraryManager.addUser(user5);

        User user6 = new User("U006", "Lisa Anderson", "lisa.a@university.edu", "0776789012", MembershipType.STUDENT);
        libraryManager.addUser(user6);

        User user7 = new User("U007", "Robert Taylor", "robert.t@university.edu", "0777890123", MembershipType.FACULTY);
        libraryManager.addUser(user7);

        User user8 = new User("U008", "Jennifer Martinez", "jennifer.m@university.edu", "0778901234", MembershipType.STUDENT);
        libraryManager.addUser(user8);
    }

    private static void initializeBorrowRecords(LibraryManager libraryManager) {
        User user1 = libraryManager.getUser("U001");
        Book book1 = libraryManager.getBook("B001");
        if (user1 != null && book1 != null) {
            libraryManager.borrowBook(user1, book1);
        }

        User user2 = libraryManager.getUser("U002");
        Book book2 = libraryManager.getBook("B002");
        if (user2 != null && book2 != null) {
            libraryManager.borrowBook(user2, book2);
        }

        User user3 = libraryManager.getUser("U003");
        Book book3 = libraryManager.getBook("B003");
        if (user3 != null && book3 != null) {
            libraryManager.borrowBook(user3, book3);
        }
    }
}

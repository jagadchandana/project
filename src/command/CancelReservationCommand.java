package command;

import manager.LibraryManager;
import model.User;
import model.Book;

public class CancelReservationCommand implements Command {
    private LibraryManager libraryManager;
    private User user;
    private Book book;
    private boolean executed;

    public CancelReservationCommand(LibraryManager libraryManager, User user, Book book) {
        this.libraryManager = libraryManager;
        this.user = user;
        this.book = book;
        this.executed = false;
    }

    @Override
    public void execute() {
        if (!executed) {
            libraryManager.cancelReservation(user, book);
            executed = true;
        }
    }

    @Override
    public void undo() {
        if (executed) {
            libraryManager.reserveBook(user, book);
            executed = false;
        }
    }

    @Override
    public String getCommandDescription() {
        return "Cancel Reservation: " + book.getTitle() + " by " + user.getName();
    }
}

package command;

import manager.LibraryManager;
import model.User;
import model.Book;

public class ReserveCommand implements Command {
    private LibraryManager libraryManager;
    private User user;
    private Book book;
    private boolean executed;

    public ReserveCommand(LibraryManager libraryManager, User user, Book book) {
        this.libraryManager = libraryManager;
        this.user = user;
        this.book = book;
        this.executed = false;
    }

    @Override
    public void execute() {
        if (!executed) {
            libraryManager.reserveBook(user, book);
            executed = true;
        }
    }

    @Override
    public void undo() {
        if (executed) {
            libraryManager.cancelReservation(user, book);
            executed = false;
        }
    }

    @Override
    public String getCommandDescription() {
        return "Reserve: " + book.getTitle() + " by " + user.getName();
    }
}

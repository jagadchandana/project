package command;

import manager.LibraryManager;
import model.User;
import model.Book;

public class ReturnCommand implements Command {
    private LibraryManager libraryManager;
    private User user;
    private Book book;
    private boolean executed;

    public ReturnCommand(LibraryManager libraryManager, User user, Book book) {
        this.libraryManager = libraryManager;
        this.user = user;
        this.book = book;
        this.executed = false;
    }

    @Override
    public void execute() {
        if (!executed) {
            libraryManager.returnBook(user, book);
            executed = true;
        }
    }

    @Override
    public void undo() {
        if (executed) {
            libraryManager.borrowBook(user, book);
            executed = false;
        }
    }

    @Override
    public String getCommandDescription() {
        return "Return: " + book.getTitle() + " by " + user.getName();
    }
}

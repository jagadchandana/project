package state;

import model.Book;

public class BorrowedState implements BookState {
    
    @Override
    public void borrow(Book book) {
    }

    @Override
    public void returnBook(Book book) {
        book.setState(new AvailableState());
    }

    @Override
    public void reserve(Book book) {
        book.setState(new ReservedState());
    }

    @Override
    public String getStateName() {
        return "Borrowed";
    }
}

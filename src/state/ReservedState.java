package state;

import model.Book;

public class ReservedState implements BookState {
    
    @Override
    public void borrow(Book book) {
        book.setState(new BorrowedState());
    }

    @Override
    public void returnBook(Book book) {
        book.setState(new AvailableState());
    }

    @Override
    public void reserve(Book book) {
    }

    @Override
    public String getStateName() {
        return "Reserved";
    }
}

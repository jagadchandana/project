package state;

import model.Book;

public interface BookState {
    void borrow(Book book);
    void returnBook(Book book);
    void reserve(Book book);
    String getStateName();
}

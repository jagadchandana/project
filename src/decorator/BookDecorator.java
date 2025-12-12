package decorator;

import model.Book;

public abstract class BookDecorator extends Book {
    protected Book decoratedBook;

    public BookDecorator(Book book) {
        super(book.getBookId(), book.getTitle(), book.getAuthor(), book.getCategory(), book.getIsbn());
        this.decoratedBook = book;
        this.setState(book.getState());
    }

    @Override
    public abstract String toString();
}

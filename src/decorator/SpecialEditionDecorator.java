package decorator;

import model.Book;

public class SpecialEditionDecorator extends BookDecorator {

    public SpecialEditionDecorator(Book book) {
        super(book);
    }

    @Override
    public String toString() {
        return "[SPECIAL EDITION] " + decoratedBook.toString();
    }

    public String getSpecialEditionLabel() {
        return "Special Edition";
    }
}

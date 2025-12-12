package decorator;

import model.Book;

public class FeaturedBookDecorator extends BookDecorator {

    public FeaturedBookDecorator(Book book) {
        super(book);
    }

    @Override
    public String toString() {
        return "[FEATURED] " + decoratedBook.toString();
    }

    public String getFeatureLabel() {
        return "Featured";
    }
}

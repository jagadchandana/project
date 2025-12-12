package decorator;

import model.Book;

public class RecommendedBookDecorator extends BookDecorator {

    public RecommendedBookDecorator(Book book) {
        super(book);
    }

    @Override
    public String toString() {
        return "[RECOMMENDED] " + decoratedBook.toString();
    }

    public String getRecommendationLabel() {
        return "Recommended";
    }
}

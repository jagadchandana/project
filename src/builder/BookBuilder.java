package builder;

import model.Book;
import java.util.ArrayList;
import java.util.List;

public class BookBuilder {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private String isbn;
    private String reviews;
    private List<String> tags;
    private String edition;

    public BookBuilder(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.tags = new ArrayList<>();
    }

    public BookBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public BookBuilder setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public BookBuilder setReviews(String reviews) {
        this.reviews = reviews;
        return this;
    }

    public BookBuilder addTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    public BookBuilder setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public BookBuilder setEdition(String edition) {
        this.edition = edition;
        return this;
    }

    public Book build() {
        Book book = new Book(bookId, title, author, category, isbn);
        book.setReviews(reviews);
        book.setTags(tags);
        book.setEdition(edition);
        return book;
    }
}

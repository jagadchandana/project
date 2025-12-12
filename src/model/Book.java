package model;

import state.BookState;
import state.AvailableState;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private String isbn;
    private BookState state;
    private List<String> borrowedHistory;
    private String reviews;
    private List<String> tags;
    private String edition;

    public Book(String bookId, String title, String author, String category, String isbn) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
        this.state = new AvailableState();
        this.borrowedHistory = new ArrayList<>();
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BookState getState() {
        return state;
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public List<String> getBorrowedHistory() {
        return borrowedHistory;
    }

    public void addToBorrowedHistory(String userId) {
        this.borrowedHistory.add(userId);
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getAvailabilityStatus() {
        return state.getStateName();
    }

    @Override
    public String toString() {
        return bookId + " - " + title + " by " + author + " [" + getAvailabilityStatus() + "]";
    }
}

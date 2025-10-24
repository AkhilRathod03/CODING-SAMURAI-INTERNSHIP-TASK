package com.librarysystem;

import java.io.Serializable;

/**
 * Represents a book in the library system.
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String author;
    private boolean isBorrowed;
    private String borrowedBy;

    /**
     * Constructs a new Book instance.
     * @param id The unique ID of the book.
     * @param title The title of the book.
     * @param author The author of the book.
     */
    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
        this.borrowedBy = null;
    }

    /**
     * Returns the ID of the book.
     * @return The book ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the book.
     * @param id The new book ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the title of the book.
     * @return The book title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     * @param title The new book title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the author of the book.
     * @return The book author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     * @param author The new book author.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Checks if the book is currently borrowed.
     * @return True if borrowed, false otherwise.
     */
    public boolean isBorrowed() {
        return isBorrowed;
    }

    /**
     * Sets the borrowed status of the book.
     * @param borrowed True if the book is to be marked as borrowed, false otherwise.
     */
    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    /**
     * Returns the name of the person who borrowed the book.
     * @return The borrower's name, or null if not borrowed.
     */
    public String getBorrowedBy() {
        return borrowedBy;
    }

    /**
     * Sets the name of the person who borrowed the book.
     * @param borrowedBy The borrower's name.
     */
    public void setBorrowedBy(String borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    /**
     * Returns a string representation of the Book object.
     * @return A string containing book details.
     */
    @Override
    public String toString() {
        return "ID: " + id + ", Title: " + title + ", Author: " + author + ", Borrowed: " + (isBorrowed ? "Yes" : "No") + (borrowedBy != null ? " by " + borrowedBy : "");
    }
}

package com.librarysystem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a record of a book being borrowed or returned.
 */
public class BorrowingRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String recordId;
    private String bookId;
    private String borrowerName;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;

    /**
     * Constructs a new BorrowingRecord for a borrowed book.
     * @param recordId A unique ID for this record.
     * @param bookId The ID of the book being borrowed.
     * @param borrowerName The name of the borrower.
     * @param borrowDate The date and time the book was borrowed.
     */
    public BorrowingRecord(String recordId, String bookId, String borrowerName, LocalDateTime borrowDate) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.borrowerName = borrowerName;
        this.borrowDate = borrowDate;
        this.returnDate = null; // Initially null, set when returned
    }

    /**
     * Returns the unique ID of this record.
     * @return The record ID.
     */
    public String getRecordId() {
        return recordId;
    }

    /**
     * Returns the ID of the book associated with this record.
     * @return The book ID.
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * Returns the name of the borrower.
     * @return The borrower's name.
     */
    public String getBorrowerName() {
        return borrowerName;
    }

    /**
     * Returns the date and time the book was borrowed.
     * @return The borrow date.
     */
    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    /**
     * Returns the date and time the book was returned.
     * @return The return date, or null if not yet returned.
     */
    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    /**
     * Sets the return date for this record.
     * @param returnDate The date and time the book was returned.
     */
    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Returns a string representation of the BorrowingRecord object.
     * @return A string containing record details.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Record ID: " + recordId +
               ", Book ID: " + bookId +
               ", Borrower: " + borrowerName +
               ", Borrow Date: " + borrowDate.format(formatter) +
               ", Return Date: " + (returnDate != null ? returnDate.format(formatter) : "N/A");
    }
}

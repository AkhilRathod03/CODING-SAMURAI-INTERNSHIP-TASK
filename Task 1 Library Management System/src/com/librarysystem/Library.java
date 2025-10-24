package com.librarysystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Manages the collection of books and borrowing records in the library, including adding, removing, updating, and searching.
 * Handles borrowing and returning books, and interacts with FileHandler for data persistence.
 */
public class Library {
    private List<Book> books;
    private List<BorrowingRecord> borrowingRecords;

    /**
     * Constructs a new Library instance and loads existing book and borrowing record data from files.
     */
    public Library() {
        System.out.println("Library: Initializing and loading data...");
        this.books = FileHandler.loadBooks();
        this.borrowingRecords = FileHandler.loadBorrowingRecords();
        System.out.println("Library: Loaded " + books.size() + " books and " + borrowingRecords.size() + " records.");
    }

    /**
     * Adds a new book to the library.
     * @param book The Book object to add.
     * @return True if the book was added successfully, false if a book with the same ID already exists.
     */
    public boolean addBook(Book book) {
        System.out.println("Library: Attempting to add book with ID: " + book.getId());
        if (getBookById(book.getId()) == null) {
            this.books.add(book);
            FileHandler.saveBooks(books);
            System.out.println("Library: Book " + book.getId() + " added and saved.");
            return true;
        }
        System.out.println("Library: Book " + book.getId() + " already exists.");
        return false;
    }

    /**
     * Removes a book from the library by its ID.
     * @param id The ID of the book to remove.
     * @return True if the book was removed successfully, false otherwise.
     */
    public boolean removeBook(String id) {
        System.out.println("Library: Attempting to remove book with ID: " + id);
        boolean removed = this.books.removeIf(book -> book.getId().equals(id));
        if (removed) {
            // Also remove any active borrowing records associated with this book
            int initialRecordCount = borrowingRecords.size();
            borrowingRecords.removeIf(record -> record.getBookId().equals(id) && record.getReturnDate() == null);
            if (borrowingRecords.size() < initialRecordCount) {
                System.out.println("Library: Removed " + (initialRecordCount - borrowingRecords.size()) + " active borrowing records for book " + id);
            }
            FileHandler.saveBooks(books);
            FileHandler.saveBorrowingRecords(borrowingRecords);
            System.out.println("Library: Book " + id + " removed and data saved.");
        }
        System.out.println("Library: Book " + id + " removal status: " + removed);
        return removed;
    }

    /**
     * Updates an existing book's title and author.
     * @param id The ID of the book to update.
     * @param newTitle The new title for the book (can be null or empty to keep existing).
     * @param newAuthor The new author for the book (can be null or empty to keep existing).
     * @return True if the book was updated successfully, false if the book was not found.
     */
    public boolean updateBook(String id, String newTitle, String newAuthor) {
        System.out.println("Library: Attempting to update book with ID: " + id);
        Book book = getBookById(id);
        if (book != null) {
            if (newTitle != null && !newTitle.trim().isEmpty()) {
                book.setTitle(newTitle);
            }
            if (newAuthor != null && !newAuthor.trim().isEmpty()) {
                book.setAuthor(newAuthor);
            }
            FileHandler.saveBooks(books);
            System.out.println("Library: Book " + id + " updated and saved.");
            return true;
        }
        System.out.println("Library: Book " + id + " not found for update.");
        return false;
    }

    /**
     * Retrieves a book by its ID.
     * @param id The ID of the book to retrieve.
     * @return The Book object if found, null otherwise.
     */
    public Book getBookById(String id) {
        return this.books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Searches for books by title or author.
     * @param query The search query string.
     * @return A list of books matching the query.
     */
    public List<Book> searchBooks(String query) {
        String lowerCaseQuery = query.toLowerCase();
        return this.books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                                 book.getAuthor().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
    }

    /**
     * Borrows a book, marking it as unavailable and assigning a borrower.
     * Creates a new borrowing record.
     * @param id The ID of the book to borrow.
     * @param borrower The name of the person borrowing the book.
     * @return True if the book was successfully borrowed, false otherwise (e.g., book not found or already borrowed).
     */
    public boolean borrowBook(String id, String borrower) {
        System.out.println("Library: Attempting to borrow book " + id + " by " + borrower);
        Book book = getBookById(id);
        if (book != null && !book.isBorrowed()) {
            book.setBorrowed(true);
            book.setBorrowedBy(borrower);

            String recordId = UUID.randomUUID().toString();
            BorrowingRecord record = new BorrowingRecord(recordId, id, borrower, LocalDateTime.now());
            borrowingRecords.add(record);

            FileHandler.saveBooks(books);
            FileHandler.saveBorrowingRecords(borrowingRecords);
            System.out.println("Library: Book " + id + " borrowed and records saved.");
            return true;
        }
        System.out.println("Library: Failed to borrow book " + id + ". Book not found or already borrowed.");
        return false;
    }

    /**
     * Returns a borrowed book, marking it as available.
     * Updates the corresponding borrowing record.
     * @param id The ID of the book to return.
     * @return True if the book was successfully returned, false otherwise (e.g., book not found or not borrowed).
     */
    public boolean returnBook(String id) {
        System.out.println("Library: Attempting to return book with ID: " + id);
        Book book = getBookById(id);
        if (book != null && book.isBorrowed()) {
            book.setBorrowed(false);
            book.setBorrowedBy(null);

            // Find the active borrowing record and set return date
            borrowingRecords.stream()
                    .filter(record -> record.getBookId().equals(id) && record.getReturnDate() == null)
                    .findFirst()
                    .ifPresent(record -> {
                        record.setReturnDate(LocalDateTime.now());
                        System.out.println("Library: Borrowing record for " + id + " updated with return date.");
                    });

            FileHandler.saveBooks(books);
            FileHandler.saveBorrowingRecords(borrowingRecords);
            System.out.println("Library: Book " + id + " returned and records saved.");
            return true;
        }
        System.out.println("Library: Failed to return book " + id + ". Book not found or not borrowed.");
        return false;
    }

    /**
     * Returns a list of all books in the library.
     * @return A new ArrayList containing all books.
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Returns a list of books that are currently available (not borrowed).
     * @return A list of available books.
     */
    public List<Book> getAvailableBooks() {
        return books.stream()
                .filter(book -> !book.isBorrowed())
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of books that are currently borrowed.
     * @return A list of borrowed books.
     */
    public List<Book> getBorrowedBooks() {
        return books.stream()
                .filter(Book::isBorrowed)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all borrowing records.
     * @return A new ArrayList containing all borrowing records.
     */
    public List<BorrowingRecord> getBorrowingHistory() {
        return new ArrayList<>(borrowingRecords);
    }

    /**
     * Saves the current state of the library (all books and borrowing records) to their respective data files.
     */
    public void saveData() {
        System.out.println("Library: Saving all data...");
        FileHandler.saveBooks(books);
        FileHandler.saveBorrowingRecords(borrowingRecords);
        System.out.println("Library: All data saved.");
    }
}

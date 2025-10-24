package com.librarysystem;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading and writing Book and BorrowingRecord data to files for persistence.
 */
public class FileHandler {

    private static final String BOOKS_FILE_NAME = "library_data.txt";
    private static final String RECORDS_FILE_NAME = "borrowing_records.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Loads a list of books from the data file.
     * @return A list of Book objects loaded from the file.
     */
    public static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        System.out.println("Attempting to load books from " + BOOKS_FILE_NAME);
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t"); // Using tab as delimiter
                if (parts.length == 5) {
                    String id = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    boolean isBorrowed = Boolean.parseBoolean(parts[3]);
                    String borrowedBy = parts[4].equals("null") ? null : parts[4];

                    Book book = new Book(id, title, author);
                    book.setBorrowed(isBorrowed);
                    book.setBorrowedBy(borrowedBy);
                    books.add(book);
                }
            }
            System.out.println("Successfully loaded " + books.size() + " books.");
        } catch (FileNotFoundException e) {
            System.out.println("Books data file not found. Starting with an empty library.");
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    /**
     * Saves a list of books to the data file.
     * @param books The list of Book objects to save.
     */
    public static void saveBooks(List<Book> books) {
        System.out.println("Attempting to save " + books.size() + " books to " + BOOKS_FILE_NAME);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE_NAME))) {
            for (Book book : books) {
                writer.write(book.getId() + "\t" +
                             book.getTitle() + "\t" +
                             book.getAuthor() + "\t" +
                             book.isBorrowed() + "\t" +
                             (book.getBorrowedBy() != null ? book.getBorrowedBy() : "null"));
                writer.newLine();
            }
            System.out.println("Successfully saved " + books.size() + " books.");
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    /**
     * Loads a list of borrowing records from the data file.
     * @return A list of BorrowingRecord objects loaded from the file.
     */
    public static List<BorrowingRecord> loadBorrowingRecords() {
        List<BorrowingRecord> records = new ArrayList<>();
        System.out.println("Attempting to load borrowing records from " + RECORDS_FILE_NAME);
        try (BufferedReader reader = new BufferedReader(new FileReader(RECORDS_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 5) {
                    String recordId = parts[0];
                    String bookId = parts[1];
                    String borrowerName = parts[2];
                    LocalDateTime borrowDate = LocalDateTime.parse(parts[3], FORMATTER);
                    LocalDateTime returnDate = parts[4].equals("null") ? null : LocalDateTime.parse(parts[4], FORMATTER);

                    BorrowingRecord record = new BorrowingRecord(recordId, bookId, borrowerName, borrowDate);
                    record.setReturnDate(returnDate);
                    records.add(record);
                }
            }
            System.out.println("Successfully loaded " + records.size() + " borrowing records.");
        } catch (FileNotFoundException e) {
            System.out.println("Borrowing records data file not found. Starting with no records.");
        } catch (IOException e) {
            System.err.println("Error loading borrowing records: " + e.getMessage());
        }
        return records;
    }

    /**
     * Saves a list of borrowing records to the data file.
     * @param records The list of BorrowingRecord objects to save.
     */
    public static void saveBorrowingRecords(List<BorrowingRecord> records) {
        System.out.println("Attempting to save " + records.size() + " borrowing records to " + RECORDS_FILE_NAME);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECORDS_FILE_NAME))) {
            for (BorrowingRecord record : records) {
                writer.write(record.getRecordId() + "\t" +
                             record.getBookId() + "\t" +
                             record.getBorrowerName() + "\t" +
                             record.getBorrowDate().format(FORMATTER) + "\t" +
                             (record.getReturnDate() != null ? record.getReturnDate().format(FORMATTER) : "null"));
                writer.newLine();
            }
            System.out.println("Successfully saved " + records.size() + " borrowing records.");
        } catch (IOException e) {
            System.err.println("Error saving borrowing records: " + e.getMessage());
        }
    }
}

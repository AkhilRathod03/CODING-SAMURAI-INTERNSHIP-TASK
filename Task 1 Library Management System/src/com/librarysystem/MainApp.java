package com.librarysystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

/**
 * Main application class for the Library Management System with a Swing GUI.
 */
public class MainApp extends JFrame {

    private Library library;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // UI Components for Add Book
    private JTextField addIdField, addTitleField, addAuthorField;

    // UI Components for Manage Books
    private JTextField manageIdField, updateTitleField, updateAuthorField;

    // UI Components for Borrow/Return
    private JTextField brIdField, brBorrowerField;

    // UI Components for View Books
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTextField searchField;

    // UI Components for Borrowing History
    private JTable historyTable;
    private DefaultTableModel historyTableModel;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor for MainApp. Initializes the library and the GUI.
     */
    public MainApp() {
        library = new Library();
        setTitle("Library Management System");
        setSize(1200, 800); // Increased size
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Handle close manually
        setLocationRelativeTo(null);

        // Set a global default font
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 16);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 16));
        UIManager.put("OptionPane.messageFont", defaultFont);
        UIManager.put("OptionPane.buttonFont", defaultFont);
        
        // Add window listener to save data on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        JPanel dashboardPanel = createDashboardPanel();
        JPanel addBookPanel = createAddBookPanel();
        JPanel manageBooksPanel = createManageBooksPanel();
        JPanel borrowReturnPanel = createBorrowReturnPanel();
        JPanel viewBooksPanel = createViewBooksPanel();
        JPanel borrowingHistoryPanel = createBorrowingHistoryPanel();

        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(addBookPanel, "AddBook");
        mainPanel.add(manageBooksPanel, "ManageBooks");
        mainPanel.add(borrowReturnPanel, "BorrowReturn");
        mainPanel.add(viewBooksPanel, "ViewBooks");
        mainPanel.add(borrowingHistoryPanel, "BorrowingHistory");

        add(mainPanel);
        cardLayout.show(mainPanel, "Dashboard"); // Show dashboard initially
    }

    /**
     * Creates the main dashboard panel with navigation buttons.
     * @return A JPanel representing the dashboard.
     */
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241)); // Light gray background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Increased Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel("Welcome to Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Larger font
        titleLabel.setForeground(new Color(44, 62, 80)); // Dark blue text
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JButton addBookBtn = createNavButton("Add New Book", "AddBook");
        panel.add(addBookBtn, gbc);

        gbc.gridx++;
        JButton viewBooksBtn = createNavButton("View All Books", "ViewBooks");
        panel.add(viewBooksBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JButton manageBooksBtn = createNavButton("Manage Books", "ManageBooks");
        panel.add(manageBooksBtn, gbc);

        gbc.gridx++;
        JButton borrowReturnBtn = createNavButton("Borrow / Return Book", "BorrowReturn");
        panel.add(borrowReturnBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JButton historyBtn = createNavButton("Borrowing History", "BorrowingHistory");
        panel.add(historyBtn, gbc);

        gbc.gridx++;
        JButton exitBtn = new JButton("Exit Application");
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Larger font
        exitBtn.setBackground(new Color(192, 57, 43)); // Red color
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25)); // Increased padding
        exitBtn.addActionListener(e -> {
            library.saveData();
            JOptionPane.showMessageDialog(MainApp.this, "Library data saved. Exiting application.");
            System.exit(0);
        });
        panel.add(exitBtn, gbc);

        return panel;
    }

    /**
     * Helper method to create a navigation button.
     * @param text The text to display on the button.
     * @param panelName The name of the panel to switch to.
     * @return A styled JButton.
     */
    private JButton createNavButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18)); // Larger font
        button.setBackground(new Color(52, 152, 219)); // Blue color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25)); // Increased padding
        button.addActionListener(e -> {
            cardLayout.show(mainPanel, panelName);
            // Refresh data when navigating to view/history panels
            if (panelName.equals("ViewBooks")) {
                displayBooks(library.getAllBooks());
            } else if (panelName.equals("BorrowingHistory")) {
                displayBorrowingHistory(library.getBorrowingHistory());
            }
        });
        return button;
    }

    /**
     * Creates the panel for adding new books.
     * @return A JPanel for adding books.
     */
    private JPanel createAddBookPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Increased padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Add New Book");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Larger font
        title.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Book ID:"), gbc);
        gbc.gridx = 1;
        addIdField = new JTextField(20);
        panel.add(addIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        addTitleField = new JTextField(20);
        panel.add(addTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        addAuthorField = new JTextField(20);
        panel.add(addAuthorField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton addButton = new JButton("Add Book");
        addButton.setBackground(new Color(46, 204, 113)); // Green
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> addBook());
        panel.add(addButton, gbc);

        gbc.gridy++;
        JButton backButton = createNavButton("Back to Dashboard", "Dashboard");
        panel.add(backButton, gbc);

        return panel;
    }

    /**
     * Handles the action of adding a book.
     */
    private void addBook() {
        String id = addIdField.getText().trim();
        String title = addTitleField.getText().trim();
        String author = addAuthorField.getText().trim();

        if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book newBook = new Book(id, title, author);
        if (library.addBook(newBook)) {
            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            addIdField.setText("");
            addTitleField.setText("");
            addAuthorField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Book with ID " + id + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates the panel for managing (updating/removing) books.
     * @return A JPanel for managing books.
     */
    private JPanel createManageBooksPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Increased padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Manage Books");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Larger font
        title.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Book ID:"), gbc);
        gbc.gridx = 1;
        manageIdField = new JTextField(20);
        panel.add(manageIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("New Title (optional):"), gbc);
        gbc.gridx = 1;
        updateTitleField = new JTextField(20);
        panel.add(updateTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("New Author (optional):"), gbc);
        gbc.gridx = 1;
        updateAuthorField = new JTextField(20);
        panel.add(updateAuthorField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton updateButton = new JButton("Update Book");
        updateButton.setBackground(new Color(52, 152, 219)); // Blue
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> updateBook());
        panel.add(updateButton, gbc);

        gbc.gridy++;
        JButton removeButton = new JButton("Remove Book");
        removeButton.setBackground(new Color(231, 76, 60)); // Red
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(e -> removeBook());
        panel.add(removeButton, gbc);

        gbc.gridy++;
        JButton backButton = createNavButton("Back to Dashboard", "Dashboard");
        panel.add(backButton, gbc);

        return panel;
    }

    /**
     * Handles the action of updating a book.
     */
    private void updateBook() {
        String id = manageIdField.getText().trim();
        String newTitle = updateTitleField.getText().trim();
        String newAuthor = updateAuthorField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Book ID to update.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newTitle.isEmpty() && newAuthor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a new title or author to update.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (library.updateBook(id, newTitle, newAuthor)) {
            JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            manageIdField.setText("");
            updateTitleField.setText("");
            updateAuthorField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Book with ID " + id + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the action of removing a book.
     */
    private void removeBook() {
        String id = manageIdField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Book ID to remove.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (library.removeBook(id)) {
            JOptionPane.showMessageDialog(this, "Book removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            manageIdField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Book with ID " + id + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates the panel for borrowing and returning books.
     * @return A JPanel for borrow/return operations.
     */
    private JPanel createBorrowReturnPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Increased padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Borrow / Return Book");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Larger font
        title.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Book ID:"), gbc);
        gbc.gridx = 1;
        brIdField = new JTextField(20);
        panel.add(brIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Borrower Name:"), gbc);
        gbc.gridx = 1;
        brBorrowerField = new JTextField(20);
        panel.add(brBorrowerField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton borrowButton = new JButton("Borrow Book");
        borrowButton.setBackground(new Color(243, 156, 18)); // Orange
        borrowButton.setForeground(Color.WHITE);
        borrowButton.setFocusPainted(false);
        borrowButton.addActionListener(e -> borrowBook());
        panel.add(borrowButton, gbc);

        gbc.gridy++;
        JButton returnButton = new JButton("Return Book");
        returnButton.setBackground(new Color(52, 73, 94)); // Darker Blue
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        returnButton.addActionListener(e -> returnBook());
        panel.add(returnButton, gbc);

        gbc.gridy++;
        JButton backButton = createNavButton("Back to Dashboard", "Dashboard");
        panel.add(backButton, gbc);

        return panel;
    }

    /**
     * Handles the action of borrowing a book.
     */
    private void borrowBook() {
        String id = brIdField.getText().trim();
        String borrower = brBorrowerField.getText().trim();

        if (id.isEmpty() || borrower.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Book ID and Borrower Name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (library.borrowBook(id, borrower)) {
            JOptionPane.showMessageDialog(this, "Book ID " + id + " borrowed by " + borrower + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            brIdField.setText("");
            brBorrowerField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Could not borrow book. Check ID or if it's already borrowed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the action of returning a book.
     */
    private void returnBook() {
        String id = brIdField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Book ID to return.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (library.returnBook(id)) {
            JOptionPane.showMessageDialog(this, "Book ID " + id + " returned!", "Success", JOptionPane.INFORMATION_MESSAGE);
            brIdField.setText("");
            brBorrowerField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Could not return book. Check ID or if it's not borrowed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates the panel for viewing and searching books.
     * @return A JPanel for viewing books.
     */
    private JPanel createViewBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("View All Books");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Larger font
        title.setForeground(new Color(44, 62, 80));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.NORTH);

        // Search and Filter Panel
        JPanel searchFilterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15)); // Increased spacing
        searchFilterPanel.setBackground(new Color(236, 240, 241));
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Larger font
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(52, 152, 219));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> searchBooks());

        JButton showAllButton = new JButton("Show All");
        showAllButton.setBackground(new Color(149, 165, 166)); // Gray
        showAllButton.setForeground(Color.WHITE);
        showAllButton.setFocusPainted(false);
        showAllButton.addActionListener(e -> displayBooks(library.getAllBooks()));

        JButton showAvailableButton = new JButton("Show Available");
        showAvailableButton.setBackground(new Color(46, 204, 113)); // Green
        showAvailableButton.setForeground(Color.WHITE);
        showAvailableButton.setFocusPainted(false);
        showAvailableButton.addActionListener(e -> displayBooks(library.getAvailableBooks()));

        JButton showBorrowedButton = new JButton("Show Borrowed");
        showBorrowedButton.setBackground(new Color(243, 156, 18)); // Orange
        showBorrowedButton.setForeground(Color.WHITE);
        showBorrowedButton.setFocusPainted(false);
        showBorrowedButton.addActionListener(e -> displayBooks(library.getBorrowedBooks()));

        searchFilterPanel.add(searchField);
        searchFilterPanel.add(searchButton);
        searchFilterPanel.add(showAllButton);
        searchFilterPanel.add(showAvailableButton);
        searchFilterPanel.add(showBorrowedButton);
        panel.add(searchFilterPanel, BorderLayout.NORTH);

        // Table for displaying books
        String[] columnNames = {"ID", "Title", "Author", "Borrowed", "Borrowed By"};
        bookTableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(bookTableModel);
        bookTable.setRowHeight(30); // Increased row height
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16)); // Larger header font
        bookTable.getTableHeader().setBackground(new Color(44, 62, 80));
        bookTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(new Color(236, 240, 241));
        JButton backButton = createNavButton("Back to Dashboard", "Dashboard");
        southPanel.add(backButton);
        panel.add(southPanel, BorderLayout.SOUTH);

        displayBooks(library.getAllBooks()); // Initial display
        return panel;
    }

    /**
     * Populates the book table with the given list of books.
     * @param books The list of Book objects to display.
     */
    private void displayBooks(List<Book> books) {
        bookTableModel.setRowCount(0); // Clear existing data
        for (Book book : books) {
            Vector<Object> row = new Vector<>();
            row.add(book.getId());
            row.add(book.getTitle());
            row.add(book.getAuthor());
            row.add(book.isBorrowed() ? "Yes" : "No");
            row.add(book.getBorrowedBy() != null ? book.getBorrowedBy() : "-");
            bookTableModel.addRow(row);
        }
    }

    /**
     * Handles the action of searching for books.
     */
    private void searchBooks() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            displayBooks(library.getAllBooks());
        } else {
            displayBooks(library.searchBooks(query));
        }
    }

    /**
     * Creates the panel for viewing borrowing history.
     * @return A JPanel for borrowing history.
     */
    private JPanel createBorrowingHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Borrowing History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Larger font
        title.setForeground(new Color(44, 62, 80));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.NORTH);

        // Table for displaying borrowing history
        String[] columnNames = {"Record ID", "Book ID", "Borrower", "Borrow Date", "Return Date"};
        historyTableModel = new DefaultTableModel(columnNames, 0);
        historyTable = new JTable(historyTableModel);
        historyTable.setRowHeight(30); // Increased row height
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16)); // Larger header font
        historyTable.getTableHeader().setBackground(new Color(44, 62, 80));
        historyTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(new Color(236, 240, 241));
        JButton refreshButton = new JButton("Refresh History");
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> displayBorrowingHistory(library.getBorrowingHistory()));
        southPanel.add(refreshButton);

        JButton backButton = createNavButton("Back to Dashboard", "Dashboard");
        southPanel.add(backButton);
        panel.add(southPanel, BorderLayout.SOUTH);

        displayBorrowingHistory(library.getBorrowingHistory()); // Initial display
        return panel;
    }

    /**
     * Populates the borrowing history table with the given list of records.
     * @param records The list of BorrowingRecord objects to display.
     */
    private void displayBorrowingHistory(List<BorrowingRecord> records) {
        historyTableModel.setRowCount(0); // Clear existing data
        for (BorrowingRecord record : records) {
            Vector<Object> row = new Vector<>();
            row.add(record.getRecordId());
            row.add(record.getBookId());
            row.add(record.getBorrowerName());
            row.add(record.getBorrowDate().format(DATE_FORMATTER));
            row.add(record.getReturnDate() != null ? record.getReturnDate().format(DATE_FORMATTER) : "-");
            historyTableModel.addRow(row);
        }
    }

    /**
     * Main method to launch the application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Ensure GUI updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainApp mainApp = new MainApp();
            mainApp.setVisible(true);
        });
    }

    /**
     * Handles the application shutdown gracefully.
     */
    private void shutdown() {
        library.saveData();
        JOptionPane.showMessageDialog(this, "Library data saved. Exiting application.");
        System.exit(0);
    }
}

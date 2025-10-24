# Library Management System

A simple Java Swing application to manage a library's book inventory and borrowing records.

## Features

- Add, update, and remove books.
- Borrow and return books.
- View all books, available books, and borrowed books.
- Search for books by title or author.
- View borrowing history.
- Data is persisted to local files (`library_data.txt` and `borrowing_records.txt`).

## How to Run

1. **Compile the source code:**
   ```sh
   javac -d out src\com\librarysystem\*.java
   ```

2. **Run the application:**
   ```sh
   java -cp out com.librarysystem.MainApp
   ```

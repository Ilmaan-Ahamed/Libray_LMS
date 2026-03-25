package Libray_LMS.service;

import Libray_LMS.model.Book;
import Libray_LMS.model.User;
import Libray_LMS.data.Bookinventory;
import Libray_LMS.Dao.BookDAO;
import Libray_LMS.Dao.UserDAO;
import Libray_LMS.Dao.IssuedBookDAO;
import Libray_LMS.ui.ConsoleUI;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import java.util.List;

public class LibraryService {
    private Bookinventory inventory;
    private Queue<Book> borrowRequests;
    private Stack<Book> returnStack;
    private User currentUser;
    private BookDAO bookDAO;
    private UserDAO userDAO;
    private IssuedBookDAO issuedBookDAO;
    private int currentUserId = -1; // Track user ID from DB

    // Constructor to initialize the Library Service
    public LibraryService() {
        inventory = new Bookinventory();
        borrowRequests = new LinkedList<>();
        returnStack = new Stack<>();
        bookDAO = new BookDAO();
        userDAO = new UserDAO();
        issuedBookDAO = new IssuedBookDAO();
    }

    // Method to set the current user
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Register or find user in DB and set current user
    public void loginUser(String name) {
        currentUser = new User(name);

        // Check if user already exists in DB
        int userId = userDAO.findUserByName(name);
        if (userId == -1) {
            // New user — register in DB
            userId = userDAO.addUser(name);
        } else {
            ConsoleUI.success("Welcome back, " + name + "! (ID: " + userId + ")");
        }
        currentUserId = userId;
    }

    // Getter for current user
    public User getCurrentUser() {
        return currentUser;
    }

    // Getter for current user ID
    public int getCurrentUserId() {
        return currentUserId;
    }

    // Method to add a book (in-memory BST + database)
    public void addBook(String title, String author, String isbn) {
        Book book = new Book(title, author, isbn);
        inventory.insert(book);
        bookDAO.addBook(book); // Save to database
    }

    // Method to remove a book by ISBN (in-memory BST + database)
    public void removeBook(String isbn) {
        inventory.delete(isbn);
        bookDAO.deleteBookByIsbn(isbn); // Delete from database
    }

    // Method to display all books in the library sorted by ISBN
    public void displayBooks() {
        ConsoleUI.printSectionHeader("📚  Library Books (Sorted By ISBN)");
        inventory.displayInOrder();
        ConsoleUI.printDivider();
    }

    // Search books by title (partial match)
    public void searchByTitle(String keyword) {
        List<Book> results = inventory.searchByTitle(keyword);
        if (results.isEmpty()) {
            ConsoleUI.warning("No books found matching title: \"" + keyword + "\"");
        } else {
            ConsoleUI.printSectionHeader("Search Results ─ Title: \"" + keyword + "\"");
            for (Book book : results) {
                String status = book.isAvailable()
                        ? ConsoleUI.BRIGHT_GREEN + " [Available]" + ConsoleUI.RESET
                        : ConsoleUI.BRIGHT_RED + " [Borrowed]" + ConsoleUI.RESET;
                System.out.println(ConsoleUI.WHITE + "    • " + book + status);
            }
            ConsoleUI.info(results.size() + " book(s) found.");
        }
    }

    // Search books by author (partial match)
    public void searchByAuthor(String keyword) {
        List<Book> results = inventory.searchByAuthor(keyword);
        if (results.isEmpty()) {
            ConsoleUI.warning("No books found matching author: \"" + keyword + "\"");
        } else {
            ConsoleUI.printSectionHeader("Search Results ─ Author: \"" + keyword + "\"");
            for (Book book : results) {
                String status = book.isAvailable()
                        ? ConsoleUI.BRIGHT_GREEN + " [Available]" + ConsoleUI.RESET
                        : ConsoleUI.BRIGHT_RED + " [Borrowed]" + ConsoleUI.RESET;
                System.out.println(ConsoleUI.WHITE + "    • " + book + status);
            }
            ConsoleUI.info(results.size() + " book(s) found.");
        }
    }

    // Show in-memory library statistics
    public void showLocalStats() {
        int[] counts = inventory.countBooks();
        ConsoleUI.printStatsBox("IN-MEMORY BOOK STATISTICS", new String[][] {
                { "📚", "Total Books", String.valueOf(counts[0]) },
                { "✅", "Available Books", String.valueOf(counts[1]) },
                { "📖", "Borrowed Books", String.valueOf(counts[2]) }
        });
    }

    // Show database statistics
    public void showDBStats() {
        issuedBookDAO.showStats();
    }

    // View all currently issued books from DB
    public void viewIssuedBooks() {
        issuedBookDAO.viewIssuedBooks();
    }

    // View issue history for current user from DB
    public void viewUserIssueHistory() {
        if (currentUserId > 0) {
            issuedBookDAO.viewUserHistory(currentUserId);
        } else {
            ConsoleUI.warning("No user logged in.");
        }
    }

    // Admin — update book title by ID
    public void updateBookTitle(int bookId, String newTitle) {
        bookDAO.updateBook(bookId, newTitle);
    }

    // Admin — view books directly from database
    public void viewBooksFromDB() {
        bookDAO.viewBooks();
    }

    // Admin — view all registered users
    public void viewUsers() {
        userDAO.viewUsers();
    }

    // Method to request borrowing a book by ISBN
    public void requestBorrow(String isbn) {
        Book book = inventory.search(isbn);
        if (book != null && book.isAvailable()) {
            borrowRequests.add(book);
            book.setAvailable(false);
            ConsoleUI.info("Borrow request queued for: " + book.getTitle());
        } else if (book == null) {
            ConsoleUI.error("Book not found with ISBN: " + isbn);
        } else {
            ConsoleUI.warning("Book is already borrowed: " + book.getTitle());
        }
    }

    // Method to Process Borrow Requests
    public void processBorrow() {
        if (!borrowRequests.isEmpty()) {
            Book book = borrowRequests.poll();
            if (currentUser != null) {
                currentUser.addtoHistory(book);
            }
            bookDAO.updateAvailability(book.getIsbn(), false); // Sync to DB

            // Record issue in issued_books table
            if (currentUserId > 0) {
                issuedBookDAO.issueBook(currentUserId, book.getIsbn());
            }

            ConsoleUI.success("Book Issued: " + book.getTitle());
        } else {
            ConsoleUI.info("No pending borrow requests.");
        }
    }

    // Method to return a book by ISBN
    public void returnBook(String isbn) {
        Book book = inventory.search(isbn);
        if (book != null && !book.isAvailable()) {
            returnStack.push(book);
            book.setAvailable(true);
            bookDAO.updateAvailability(isbn, true); // Sync to DB

            // Record return in issued_books table
            if (currentUserId > 0) {
                issuedBookDAO.returnBook(currentUserId, isbn);
            }

            if (currentUser != null) {
                currentUser.removeFromHistory(isbn);
            }
            ConsoleUI.success("Book returned: " + book.getTitle());
        } else if (book == null) {
            ConsoleUI.error("Book not found with ISBN: " + isbn);
        } else {
            ConsoleUI.warning("This book is not currently borrowed.");
        }
    }

    // Method to process returns from the return stack
    public void processReturn() {
        if (!returnStack.isEmpty()) {
            Book book = returnStack.pop();
            ConsoleUI.success("Processed return for: " + book.getTitle());
        } else {
            ConsoleUI.info("No books to process in return stack.");
        }
    }
}

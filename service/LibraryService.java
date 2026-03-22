package Libray_LMS.service;

import Libray_LMS.model.Book;
import Libray_LMS.model.User;
import Libray_LMS.data.Bookinventory;
import Libray_LMS.Dao.BookDAO;
import Libray_LMS.Dao.UserDAO;
import Libray_LMS.Dao.IssuedBookDAO;

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

    // ✅ NEW: Register or find user in DB and set current user
    public void loginUser(String name) {
        currentUser = new User(name);

        // Check if user already exists in DB
        int userId = userDAO.findUserByName(name);
        if (userId == -1) {
            // New user — register in DB
            userId = userDAO.addUser(name);
        } else {
            System.out.println("✅ Welcome back, " + name + "! (ID: " + userId + ")");
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
        System.out.println("Library Books (Sorted By ISBN):");
        inventory.displayInOrder();
    }

    // ✅ NEW: Search books by title (partial match)
    public void searchByTitle(String keyword) {
        List<Book> results = inventory.searchByTitle(keyword);
        if (results.isEmpty()) {
            System.out.println("No books found matching title: \"" + keyword + "\"");
        } else {
            System.out.println("--- Search Results for Title: \"" + keyword + "\" ---");
            for (Book book : results) {
                System.out.println("  " + book + (book.isAvailable() ? " [Available]" : " [Borrowed]"));
            }
            System.out.println("--- " + results.size() + " book(s) found ---");
        }
    }

    // ✅ NEW: Search books by author (partial match)
    public void searchByAuthor(String keyword) {
        List<Book> results = inventory.searchByAuthor(keyword);
        if (results.isEmpty()) {
            System.out.println("No books found matching author: \"" + keyword + "\"");
        } else {
            System.out.println("--- Search Results for Author: \"" + keyword + "\" ---");
            for (Book book : results) {
                System.out.println("  " + book + (book.isAvailable() ? " [Available]" : " [Borrowed]"));
            }
            System.out.println("--- " + results.size() + " book(s) found ---");
        }
    }

    // ✅ NEW: Show in-memory library statistics
    public void showLocalStats() {
        int[] counts = inventory.countBooks();
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║     📊 IN-MEMORY BOOK STATISTICS     ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf("║  📚 Total Books       : %-12d ║%n", counts[0]);
        System.out.printf("║  ✅ Available Books    : %-12d ║%n", counts[1]);
        System.out.printf("║  📖 Borrowed Books    : %-12d ║%n", counts[2]);
        System.out.println("╚══════════════════════════════════════╝");
    }

    // ✅ NEW: Show database statistics
    public void showDBStats() {
        issuedBookDAO.showStats();
    }

    // ✅ NEW: View all currently issued books from DB
    public void viewIssuedBooks() {
        issuedBookDAO.viewIssuedBooks();
    }

    // ✅ NEW: View issue history for current user from DB
    public void viewUserIssueHistory() {
        if (currentUserId > 0) {
            issuedBookDAO.viewUserHistory(currentUserId);
        } else {
            System.out.println("⚠️ No user logged in.");
        }
    }

    // ✅ NEW: Admin — update book title by ID
    public void updateBookTitle(int bookId, String newTitle) {
        bookDAO.updateBook(bookId, newTitle);
    }

    // ✅ NEW: Admin — view books directly from database
    public void viewBooksFromDB() {
        bookDAO.viewBooks();
    }

    // ✅ NEW: Admin — view all registered users
    public void viewUsers() {
        userDAO.viewUsers();
    }

    // Method to request borrowing a book by ISBN
    public void requestBorrow(String isbn) {
        Book book = inventory.search(isbn);
        if (book != null && book.isAvailable()) {
            borrowRequests.add(book);
            book.setAvailable(false);
            System.out.println("Borrow request added to queue for : " + book.getTitle());
        } else if (book == null) {
            System.out.println("❌ Book not found with ISBN: " + isbn);
        } else {
            System.out.println("⚠️ Book is already borrowed: " + book.getTitle());
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

            // ✅ NEW: Record issue in issued_books table
            if (currentUserId > 0) {
                issuedBookDAO.issueBook(currentUserId, book.getIsbn());
            }

            System.out.println("Book Issued: " + book.getTitle());
        } else {
            System.out.println("No Pending borrow requests.");
        }
    }

    // Method to return a book by ISBN
    public void returnBook(String isbn) {
        Book book = inventory.search(isbn);
        if (book != null && !book.isAvailable()) {
            returnStack.push(book);
            book.setAvailable(true);
            bookDAO.updateAvailability(isbn, true); // Sync to DB

            // ✅ NEW: Record return in issued_books table
            if (currentUserId > 0) {
                issuedBookDAO.returnBook(currentUserId, isbn);
            }

            if (currentUser != null) {
                currentUser.removeFromHistory(isbn);
            }
            System.out.println("Book returned: " + book.getTitle());
        } else if (book == null) {
            System.out.println("❌ Book not found with ISBN: " + isbn);
        } else {
            System.out.println("⚠️ This book is not currently borrowed.");
        }
    }

    // Method to process returns from the return stack
    public void processReturn() {
        if (!returnStack.isEmpty()) {
            Book book = returnStack.pop();
            System.out.println("Processed return for: " + book.getTitle());
        } else {
            System.out.println("No Books to process in return stack");
        }
    }
}

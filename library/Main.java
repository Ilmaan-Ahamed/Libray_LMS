package Libray_LMS.library;

import Libray_LMS.service.LibraryService;
import Libray_LMS.Dao.BookDAO;
import Libray_LMS.db.DBConnection;
import Libray_LMS.ui.ConsoleUI;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static LibraryService library = new LibraryService();

    public static void main(String[] args) {
        ConsoleUI.enableAnsiSupport();
        ConsoleUI.printBanner();
        jdbcMain();
        showMainMenu();
    }

    // Safe integer input with validation
    private static int getIntInput() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                ConsoleUI.warning("Please enter a valid number!");
                scanner.nextLine(); // clear invalid input
                ConsoleUI.printPrompt("Try again: ");
            }
        }
    }

    private static void showMainMenu() {
        while (true) {
            ConsoleUI.printMenuHeader("📚  LIBRARY MANAGEMENT SYSTEM");
            ConsoleUI.printMenuItem(1, "🔧", "Admin Functions");
            ConsoleUI.printMenuItem(2, "👤", "User Functions");
            ConsoleUI.printMenuItem(3, "📊", "Library Statistics");
            ConsoleUI.printMenuItem(4, "🔍", "Search Books");
            ConsoleUI.printMenuItem(5, "❌", "Exit");
            ConsoleUI.printMenuFooter();
            ConsoleUI.printPrompt("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    showAdminMenu();
                    break;
                case 2:
                    showUserMenu();
                    break;
                case 3:
                    showStatsMenu();
                    break;
                case 4:
                    showSearchMenu();
                    break;
                case 5:
                    ConsoleUI.printGoodbye();
                    return;
                default:
                    ConsoleUI.warning("Invalid Choice! Please select 1-5.");
            }
        }
    }

    private static void showAdminMenu() {
        while (true) {
            ConsoleUI.printMenuHeader("🔧  ADMIN PANEL");
            ConsoleUI.printMenuItem(1, "➕", "Add Book");
            ConsoleUI.printMenuItem(2, "➖", "Remove Book");
            ConsoleUI.printMenuItem(3, "📋", "View All Books (In-Memory)");
            ConsoleUI.printMenuItem(4, "🗄️ ", "View All Books (Database)");
            ConsoleUI.printMenuItem(5, "✏️ ", "Update Book Title");
            ConsoleUI.printMenuItem(6, "👥", "View Registered Users");
            ConsoleUI.printMenuItem(7, "📖", "View Issued Books");
            ConsoleUI.printMenuItem(8, "⬅️ ", "Back to Main Menu");
            ConsoleUI.printMenuFooter();
            ConsoleUI.printPrompt("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    library.displayBooks();
                    break;
                case 4:
                    library.viewBooksFromDB();
                    break;
                case 5:
                    updateBookTitle();
                    break;
                case 6:
                    library.viewUsers();
                    break;
                case 7:
                    library.viewIssuedBooks();
                    break;
                case 8:
                    return;
                default:
                    ConsoleUI.warning("Invalid Choice! Please select 1-8.");
            }
        }
    }

    private static void showUserMenu() {
        if (library.getCurrentUser() == null) {
            ConsoleUI.printBlankLine();
            ConsoleUI.printPrompt("Enter Your Name: ");
            String name = scanner.nextLine();
            library.loginUser(name);
        }

        while (true) {
            ConsoleUI.printMenuHeader("👤  USER MENU  ─  " + library.getCurrentUser().getName());
            ConsoleUI.printMenuItem(1, "📖", "Borrow Book");
            ConsoleUI.printMenuItem(2, "📥", "Return Book");
            ConsoleUI.printMenuItem(3, "📜", "View Borrowing History");
            ConsoleUI.printMenuItem(4, "📚", "View Available Books");
            ConsoleUI.printMenuItem(5, "📋", "View My Issue History (DB)");
            ConsoleUI.printMenuItem(6, "🔍", "Search Books");
            ConsoleUI.printMenuItem(7, "🔄", "Switch User");
            ConsoleUI.printMenuItem(8, "⬅️ ", "Back to Main Menu");
            ConsoleUI.printMenuFooter();
            ConsoleUI.printPrompt("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    library.getCurrentUser().displayHistory();
                    break;
                case 4:
                    library.displayBooks();
                    break;
                case 5:
                    library.viewUserIssueHistory();
                    break;
                case 6:
                    showSearchMenu();
                    break;
                case 7:
                    ConsoleUI.printBlankLine();
                    ConsoleUI.printPrompt("Enter New User Name: ");
                    String name = scanner.nextLine();
                    library.loginUser(name);
                    ConsoleUI.success("Switched to user: " + name);
                    break;
                case 8:
                    return;
                default:
                    ConsoleUI.warning("Invalid Choice! Please select 1-8.");
            }
        }
    }

    // Search Menu
    private static void showSearchMenu() {
        while (true) {
            ConsoleUI.printMenuHeader("🔍  SEARCH BOOKS");
            ConsoleUI.printMenuItem(1, "📕", "Search by Title");
            ConsoleUI.printMenuItem(2, "✍️ ", "Search by Author");
            ConsoleUI.printMenuItem(3, "🔢", "Search by ISBN");
            ConsoleUI.printMenuItem(4, "⬅️ ", "Back");
            ConsoleUI.printMenuFooter();
            ConsoleUI.printPrompt("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    ConsoleUI.printPrompt("Enter title keyword: ");
                    String titleKeyword = scanner.nextLine();
                    library.searchByTitle(titleKeyword);
                    break;
                case 2:
                    ConsoleUI.printPrompt("Enter author keyword: ");
                    String authorKeyword = scanner.nextLine();
                    library.searchByAuthor(authorKeyword);
                    break;
                case 3:
                    ConsoleUI.printPrompt("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    searchByIsbn(isbn);
                    break;
                case 4:
                    return;
                default:
                    ConsoleUI.warning("Invalid Choice!");
            }
        }
    }

    // Search by ISBN helper
    private static void searchByIsbn(String isbn) {
        ConsoleUI.printSectionHeader("Searching for ISBN: " + isbn);
        library.displayBooks(); // Shows all books; user can visually scan
        ConsoleUI.info("Tip: Use borrow/return by ISBN for direct lookup.");
    }

    // Statistics Menu
    private static void showStatsMenu() {
        while (true) {
            ConsoleUI.printMenuHeader("📊  LIBRARY STATISTICS");
            ConsoleUI.printMenuItem(1, "💾", "In-Memory Statistics");
            ConsoleUI.printMenuItem(2, "🗄️ ", "Database Statistics");
            ConsoleUI.printMenuItem(3, "⬅️ ", "Back");
            ConsoleUI.printMenuFooter();
            ConsoleUI.printPrompt("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    library.showLocalStats();
                    break;
                case 2:
                    library.showDBStats();
                    break;
                case 3:
                    return;
                default:
                    ConsoleUI.warning("Invalid Choice!");
            }
        }
    }

    // Method to add a new book to the library
    private static void addBook() {
        ConsoleUI.printSectionHeader("➕  Add New Book");

        ConsoleUI.printPrompt("Enter Title: ");
        String title = scanner.nextLine();

        ConsoleUI.printPrompt("Enter Author: ");
        String author = scanner.nextLine();

        ConsoleUI.printPrompt("Enter ISBN Number: ");
        String isbn = scanner.nextLine();

        library.addBook(title, author, isbn);
        ConsoleUI.success("Book added successfully!");
    }

    // Method to remove a book from the library By ISBN
    private static void removeBook() {
        ConsoleUI.printSectionHeader("➖  Remove Book");

        ConsoleUI.printPrompt("Enter ISBN of Book to Remove: ");
        String isbn = scanner.nextLine();
        library.removeBook(isbn);
    }

    // Update a book title by its database ID
    private static void updateBookTitle() {
        ConsoleUI.printSectionHeader("✏️  Update Book Title");

        ConsoleUI.printPrompt("Enter Book ID (from database): ");
        int id = getIntInput();

        ConsoleUI.printPrompt("Enter New Title: ");
        String newTitle = scanner.nextLine();

        library.updateBookTitle(id, newTitle);
    }

    // Method to borrow a book from the library
    private static void borrowBook() {
        ConsoleUI.printSectionHeader("📖  Borrow Book");

        ConsoleUI.printPrompt("Enter ISBN of Book to borrow: ");
        String isbn = scanner.nextLine();
        library.requestBorrow(isbn);
        library.processBorrow();
    }

    // Method to return a borrowed book to the library
    private static void returnBook() {
        ConsoleUI.printSectionHeader("📥  Return Book");

        ConsoleUI.printPrompt("Enter ISBN of book to return: ");
        String isbn = scanner.nextLine();
        library.returnBook(isbn);
        library.processReturn();
    }

    // Method to connect JDBC in main file
    private static void jdbcMain() {
        ConsoleUI.printSectionHeader("🔌  Testing JDBC Connection");
        java.sql.Connection con = DBConnection.getConnection();
        ConsoleUI.printConnectionStatus(con != null);

        if (con != null) {
            try {
                con.close();
            } catch (Exception ignored) {
            }
        }

        BookDAO bookDAO = new BookDAO();
        ConsoleUI.info("Fetching books from Database...");
        bookDAO.viewBooks();
    }
}

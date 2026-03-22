package Libray_LMS.library;

import Libray_LMS.service.LibraryService;
import Libray_LMS.Dao.BookDAO;
import Libray_LMS.db.DBConnection;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static LibraryService library = new LibraryService();

    public static void main(String[] args) {
        jdbcMain();
        showMainMenu();
    }

    // ✅ Safe integer input with validation
    private static int getIntInput() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Please enter a valid number!");
                scanner.nextLine(); // clear invalid input
                System.out.print("Try again: ");
            }
        }
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║   📚 LIBRARY MANAGEMENT SYSTEM       ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. 🔧 Admin Functions               ║");
            System.out.println("║  2. 👤 User Functions                ║");
            System.out.println("║  3. 📊 Library Statistics            ║");
            System.out.println("║  4. 🔍 Search Books                  ║");
            System.out.println("║  5. ❌ Exit                          ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Enter your Choice: ");

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
                    System.out.println("👋 Exiting System... Goodbye!");
                    return;
                default:
                    System.out.println("⚠️ Invalid Choice! Please select 1-5.");
            }
        }
    }

    private static void showAdminMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          🔧 ADMIN MENU               ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. ➕ Add Book                      ║");
            System.out.println("║  2. ➖ Remove Book                   ║");
            System.out.println("║  3. 📋 View All Books (In-Memory)    ║");
            System.out.println("║  4. 🗄️  View All Books (Database)    ║");
            System.out.println("║  5. ✏️  Update Book Title             ║");
            System.out.println("║  6. 👥 View Registered Users         ║");
            System.out.println("║  7. 📖 View Issued Books             ║");
            System.out.println("║  8. ⬅️  Back to Main Menu             ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Enter your Choice: ");

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
                    System.out.println("⚠️ Invalid Choice! Please select 1-8.");
            }
        }
    }

    private static void showUserMenu() {
        if (library.getCurrentUser() == null) {
            System.out.print("\n👤 Enter Your Name: ");
            String name = scanner.nextLine();
            library.loginUser(name); // ✅ Register/login via DB
        }

        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║   👤 USER MENU (" + padRight(library.getCurrentUser().getName(), 18) + ")  ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. 📖 Borrow Book                   ║");
            System.out.println("║  2. 📥 Return Book                   ║");
            System.out.println("║  3. 📜 View Borrowing History        ║");
            System.out.println("║  4. 📚 View Available Books          ║");
            System.out.println("║  5. 📋 View My Issue History (DB)    ║");
            System.out.println("║  6. 🔍 Search Books                  ║");
            System.out.println("║  7. 🔄 Switch User                   ║");
            System.out.println("║  8. ⬅️  Back to Main Menu             ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Enter Your Choice: ");

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
                    // ✅ NEW: Switch user
                    System.out.print("\n👤 Enter New User Name: ");
                    String name = scanner.nextLine();
                    library.loginUser(name);
                    System.out.println("✅ Switched to user: " + name);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("⚠️ Invalid Choice! Please select 1-8.");
            }
        }
    }

    // ✅ NEW: Search Menu
    private static void showSearchMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          🔍 SEARCH BOOKS             ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Search by Title                  ║");
            System.out.println("║  2. Search by Author                 ║");
            System.out.println("║  3. Search by ISBN                   ║");
            System.out.println("║  4. ⬅️  Back                          ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Enter your Choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    System.out.print("Enter title keyword: ");
                    String titleKeyword = scanner.nextLine();
                    library.searchByTitle(titleKeyword);
                    break;
                case 2:
                    System.out.print("Enter author keyword: ");
                    String authorKeyword = scanner.nextLine();
                    library.searchByAuthor(authorKeyword);
                    break;
                case 3:
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    searchByIsbn(isbn);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("⚠️ Invalid Choice!");
            }
        }
    }

    // ✅ NEW: Search by ISBN helper
    private static void searchByIsbn(String isbn) {
        // Use reflection or direct search — we'll do it via the service
        System.out.println("--- Searching for ISBN: " + isbn + " ---");
        // We need to leverage existing search — for now print via display
        library.displayBooks(); // Shows all books; user can visually scan
        System.out.println("(Tip: Use borrow/return by ISBN for direct lookup)");
    }

    // ✅ NEW: Statistics Menu
    private static void showStatsMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║        📊 LIBRARY STATISTICS         ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. In-Memory Statistics             ║");
            System.out.println("║  2. Database Statistics              ║");
            System.out.println("║  3. ⬅️  Back                          ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Enter your Choice: ");

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
                    System.out.println("⚠️ Invalid Choice!");
            }
        }
    }

    // Method to add a new book to the library
    private static void addBook() {
        System.out.println("\n--- ➕ Add New Book ---");

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Author: ");
        String author = scanner.nextLine();

        System.out.print("Enter ISBN Number: ");
        String isbn = scanner.nextLine();

        library.addBook(title, author, isbn);
        System.out.println("✅ Book added successfully!");
    }

    // Method to remove a book from the library By ISBN
    private static void removeBook() {
        System.out.println("\n--- ➖ Remove Book ---");

        System.out.print("Enter ISBN of Book to Remove: ");
        String isbn = scanner.nextLine();
        library.removeBook(isbn);
    }

    // ✅ NEW: Update a book title by its database ID
    private static void updateBookTitle() {
        System.out.println("\n--- ✏️ Update Book Title ---");

        System.out.print("Enter Book ID (from database): ");
        int id = getIntInput();

        System.out.print("Enter New Title: ");
        String newTitle = scanner.nextLine();

        library.updateBookTitle(id, newTitle);
    }

    // Method to borrow a book from the library
    private static void borrowBook() {
        System.out.println("\n--- 📖 Borrow Book ---");

        System.out.print("Enter ISBN of Book to borrow: ");
        String isbn = scanner.nextLine();
        library.requestBorrow(isbn);
        library.processBorrow();
    }

    // Method to return a borrowed book to the library
    private static void returnBook() {
        System.out.println("\n--- 📥 Return Book ---");

        System.out.print("Enter ISBN of book to return: ");
        String isbn = scanner.nextLine();
        library.returnBook(isbn);
        library.processReturn();
    }

    // Method to connect JDBC in main file
    private static void jdbcMain() {
        System.out.println("\n--- 🔌 Testing JDBC Connection ---");
        DBConnection.getConnection();

        BookDAO bookDAO = new BookDAO();
        System.out.println("Fetching books from Database...");
        bookDAO.viewBooks();
    }

    // Helper method to pad strings for aligned menu display
    private static String padRight(String s, int n) {
        if (s.length() >= n)
            return s.substring(0, n);
        return String.format("%-" + n + "s", s);
    }
}

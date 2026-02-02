package Libray_LMS.library;

import Libray_LMS.model.User;               // Represnts a user of the libaray
import Libray_LMS.service.LibraryService;   // Service class to manage libarary operations

import java.util.Scanner;       // Scanner for user input

public class Main {
    private static Scanner scanner = new Scanner(System.in);            // Scanner for reading user input
    private static LibraryService library = new LibraryService();       // Library service instance to manage books & User
    private static User currentUser = null;                             // Current user of the library

    public static void main(String[] args) {
        showMainMenu();
    }


    private static void showMainMenu(){
        while (true) {
            System.out.println("\n==== Library Managment System ===");
            System.out.println("1. Admin Funtions");
            System.out.println("2. User Functions");
            System.out.println("3. Exit");
            System.out.println("Enter your Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();      // consume newline

            // Handel user choise for the main menu
            switch (choice) {
                case 1:
                    showAdminMenu();
                    break;
                case 2:
                    showUserMenu();
                    break;
                case 3:
                    System.out.println("Exiting System...");
                    return;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    private static void showAdminMenu(){
        while (true) {
            System.out.println("\n==== Admin Menu ====");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Books");
            System.out.println("3. View All Books");
            System.out.println("4. Back to Main Menu");
            System.out.println("-- Enter your Choice --");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consuem new line

            // Handle useer choice for admin menu
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
                    return;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // Method to display user menu and Handle user actions 
    private static void showUserMenu(){
        if (currentUser == null) {
            System.out.println("\nEnter Your Name: ");
            
            String name = scanner.nextLine();
            currentUser = new User(name);
            library.setCurrentUser(currentUser);
        }

        // Display user menu options and Handle user actions
        while (true) {
            System.out.println("\n=== User Menu (" + currentUser.getName() + ") ===");
            System.out.println("1. Borrow Book");
            System.out.println("2. Retrun Book");
            System.out.println("3. View Borrowing History");
            System.out.println("4. View Available Books");
            System.out.println("5. Back to Main Menu");
            System.out.println("Enter Your CHoice");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Handle user choice for user menu
            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    currentUser.displayHistory();
                    break;
                case 4:
                    library.displayBooks();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid Choice!!!");

            }
        }
    }

    

    // Method to add a new book to the library
    private static void addBook(){
        System.out.println("\n--- Add New Book  ---");
        
        System.out.println("Enter Title: ");
        String title = scanner.nextLine();

        System.out.println("Enter Author: ");
        String author = scanner.nextLine();

        System.out.println("Enter ISBN Number: ");
        String isbn = scanner.nextLine();

        library.addBook(title, author, isbn);
        System.out.println("Book added Successfully");
    }

    // Method to remove a book from the library By ISBN
    private static void removeBook(){
        System.out.println("\n--- Remove Book ---");
        
        System.out.println("Enter ISBN of Book to Remove: ");
        String isbn = scanner.nextLine();
        library.removeBook(isbn);
        System.out.println("Book Removed Successfully!");
    }

    // Method to borrow a book from the library
    private static void borrowBook(){
        System.out.println("\n--- Borrow Book ---");

        System.out.println("Enter ISBN of Book to borrow: ");
        String isbn = scanner.nextLine();
        library.requestBorrow(isbn);
        library.processBorrow();
    }

    // Mehtod to return a borrowed book to the library
    private static void returnBook(){
        System.out.println("\n--- Return Book ---");

        System.out.println("Enter ISBN of book to return: ");
        String isbn = scanner.nextLine();
        library.returnBook(isbn);
        library.processReturn();
    }
}


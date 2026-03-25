package Libray_LMS.model;

import Libray_LMS.ui.ConsoleUI;

import java.util.LinkedList;

public class User {
    private String name; // Stores user name
    private LinkedList<Book> borrowingHistory; // Stores borrowing history as a linked list

    // Constructor to initialize user with a name and an empty borrowing history
    public User(String name) {
        this.name = name;

        // Initialize Borrowing history as an empty linked list
        this.borrowingHistory = new LinkedList<>();

    }

    // Methods to manage borrowing history
    public void addtoHistory(Book book) {
        borrowingHistory.addFirst(book);
    }

    // Method to remove a book from borrowing history by ISBN
    public void removeFromHistory(String isbn) {
        borrowingHistory.removeIf(book -> book.getIsbn().equals(isbn));
    }

    // Method to display the Borrowing History of the User
    public void displayHistory() {
        ConsoleUI.printSectionHeader("📜  Borrowing History ─ " + name);
        if (borrowingHistory.isEmpty()) {
            ConsoleUI.info("No books in borrowing history.");
        } else {
            for (Book book : borrowingHistory) {
                System.out.println(ConsoleUI.WHITE + "    • " + book + ConsoleUI.RESET);
            }
        }
        ConsoleUI.printDivider();
    }

    // Getters
    public String getName() {
        return name;
    }

}

package Libray_LMS.model;

import java.util.LinkedList;

public class User {
    private String name;                        // Stores user name
    private LinkedList<Book> borrowingHistory;  // Stores borrowing history as a linked list

    // Constructor to initialize user with a name and an empty borrowing history
    public User(String name){
        this.name = name;

        // Initialize Borrowing history as an empty linked list
        this.borrowingHistory = new LinkedList<>();

    }

    // Methods to manage borrowing histroy 
    public void addtoHistory(Book book){
            borrowingHistory.addFirst(book);
    }

    // Method to remove a book from borrowing history by ISBN
    public void removeFromHistory(String isbn){
        borrowingHistory.removeIf(book -> book.getIsbn().equals(isbn));
    }

    // Method to display the Borrowing History of the User
    public void displayHistory(){
        System.out.println("Borrowing History for " + name + ":");
        borrowingHistory.forEach(System.out::println);
    }

    // Getters 
    public String getName(){
        return name;
    }



}

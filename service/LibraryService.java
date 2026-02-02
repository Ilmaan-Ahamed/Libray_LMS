package Libray_LMS.service;

import Libray_LMS.model.Book;           // Represents a book in the libaray
import Libray_LMS.model.User;           // Represents a user in the libaray
import Libray_LMS.data.Bookinventory;   // Represents the book inventory using a binary a


import java.util.Queue;                 // Represents the book inventory 
import java.util.LinkedList;            // Represents a queue for borrow requests
import java.util.Stack;                 // Represents a stack for book returns

public class LibraryService {
    private Bookinventory inventory;
    private Queue<Book> borrowRequests;
    private Stack<Book> retunStack;
    private User currentUser;

    // Constructor to initialize the libaray Service
    public LibraryService(){
        inventory       = new Bookinventory();
        borrowRequests = new LinkedList<>();
        retunStack      = new Stack<>();
    }

    // Method to set the current user
    public void setCurrentUser(User user){
        this.currentUser = user;
    }

    // Method to get the current user
    public void addBook(String title, String author, String isbn){
        inventory.insert(new Book(title, author, isbn));
    }

    // Method to remove a book by ISBN
    public void removeBook(String isbn){
        inventory.delete(isbn);
    }

    // Method to display all books in the library sorted by ISBN
    public void displayBooks(){
        System.out.println("Library Books (Sorted By ISBN):");
        inventory.displayInOrder();
    }

    // Method to Search for a book by ISBN
    public void requestBorrow(String isbn){
        Book book = inventory.search(isbn);
        if (book != null && book.isAvailable()) {
            borrowRequests.add(book);
            book.setAvailable(false);
            System.out.println("Borrow request added to queue for : " + book.getTitle());
        } else{
            System.out.println("Book not available for borrowing");
        }
    }

    // Method to Process Borrow Requests 
    public void processBorrow(){
        if (!borrowRequests.isEmpty()) {
            Book book = borrowRequests.poll();
            if (currentUser != null) {
                currentUser.addtoHistory(book);
            }
            System.out.println("Book Issued: " + book.getTitle());
        }else{
            System.out.println("No Pending borrow requests.");
        }
    }

    // Method to return a book by ISBN
    public void returnBook(String isbn){
        Book book = inventory.search(isbn);
        if (book != null && !book.isAvailable()) {
            retunStack.push(book);
            book.setAvailable(true);

            if (currentUser != null) {
                currentUser.removeFromHistory(isbn);
            }
            System.out.println("Book returned: " + book.getTitle());
        
        }else{
            System.out.println("Invalid return request.");
        }
    }

    // Method to process returns from the return stack
    public void processReturn(){
        if (!retunStack.isEmpty()) {
            Book book = retunStack.pop();
            System.out.println("Processed return for: " + book.getTitle());
        
        }else{
            System.out.println("No Books to process in return stack");
        }
    }
}

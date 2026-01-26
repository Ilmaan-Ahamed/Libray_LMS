package Libray_LMS.model;

public class Book {
    // Fields
    private String title;       // Stores Book Title
    private String author;      // Stores Book Auhor
    private String isbn;        // Stores Book ISBN
    private boolean available;  // Track Availabiliy

    // Constructor
    public Book(String title, String author, String isbn){
        this.title  = title;
        this.author = author;
        this.isbn   = isbn;
        this.available = true;  // Default to available
    }

    // Getters & Setters
    public String getTitle()        { return title;}
    public String getAuthor()       { return author;}
    public String getIsbn()         { return isbn;}
    public boolean isAvailable()   { return available;}
    public void setAvailable(boolean available) {this.available = available;}

    // Override equals mehtod to compare books by ISBN
    @Override
    public String toString(){
        return title + " by " + author + " (ISBN: " + isbn + ")";
    }
}

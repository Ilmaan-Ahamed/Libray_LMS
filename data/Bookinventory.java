package Libray_LMS.data;

import Libray_LMS.model.Book;  // Represents a book in the library

// Binary Search Tree class to manage book inventory
public class Bookinventory{
    private BSTNode root;

    // Constructor to initialize the book inventory
    public void insert(Book book){
        root = insertRec(root , book);
    }

    // Recursive method to insert a book into the BST
    private BSTNode insertRec(BSTNode root , Book book){
        if (root == null)
            return new BSTNode(book);
        
        if (book.getIsbn().compareTo(root.book.getIsbn()) < 0)
            {
                root.left = insertRec(root.left, book);

            }   else if (book.getIsbn().compareTo(root.book.getIsbn()) > 0) {
                root.right = insertRec(root.right, book);
            }
                return root;
    }

    // Method to delete a book by ISBN
    public void delete(String isbn){
        root = deleteRec(root, isbn);
    }

    // Recursive method to delete a book from the BST
    private BSTNode deleteRec(BSTNode root, String isbn) {
        if (root == null) return null;

        if (isbn.compareTo(root.book.getIsbn()) < 0) {
            root.left = deleteRec(root.left, isbn);
        } else if (isbn.compareTo(root.book.getIsbn()) > 0) {
            root.right = deleteRec(root.right, isbn);
        } else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;
            
            root.book = minValue(root.right);
            root.right = deleteRec(root.right, root.book.getIsbn());
        }
        return root;
    }


    // Method to find the book with the minimun ISBN in the right subtree
    private Book minValue(BSTNode root){
        Book minv = root.book;
        while (root.left != null) {
            minv = root.left.book;
            root = root.left;
        }
        return minv;
    }

    // Method to display all books in the inventory in order
    public void displayInOrder(){
        inOrderRec(root);
    }

    // Recurisie method to perform in-order traversal of the BST
    private void inOrderRec(BSTNode root){
        if (root != null)
            {
                inOrderRec(root.left);
                System.out.println(root.book);
                inOrderRec(root.right);
            }
    }

    // Method to search for a book by ISBN
    public Book search(String isbn){
            return searchRec(root, isbn);
        }

    // Recursive method to search for a book in the BST
    private Book searchRec(BSTNode root, String isbn) {
        if (root == null || root.book.getIsbn().equals(isbn)) {
            return root != null ? root.book : null;
        }
        return isbn.compareTo(root.book.getIsbn()) < 0 ? 
               searchRec(root.left, isbn) : 
               searchRec(root.right, isbn);
    }
}

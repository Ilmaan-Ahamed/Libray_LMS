package Libray_LMS.data;

import Libray_LMS.model.Book;

// Binary Search Tree Node class to represent each node in the book inventory
public class BSTNode {
    Book book;
    BSTNode left, right;

    // Construtor to initialize a BSTNode with a book
    public BSTNode(Book book){
        this.book = book;
        this.left = this.right = null;
        }

}

package Libray_LMS.Dao;

import Libray_LMS.db.DBConnection;
import Libray_LMS.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookDAO {
    // ADD Book
    public void addBook(Book book) {
        String sql = "INSERT INTO books(title, author, isbn, available) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setBoolean(4, true);

            ps.executeUpdate();
            System.out.println("Book added to Database: " + book.getTitle());

        } catch (Exception e) {
            System.err.println("Failed to add book to Database.");
            e.printStackTrace();
        }
    }

    // View Books
    public void viewBooks() {

        String sql = "SELECT * FROM books";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.println(
                        rs.getInt("book_id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("author") + " | " +
                                rs.getBoolean("available") + " | ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // update
    public void updateBook(int id, String title) {
        String sql = "UPDATE books SET title=? WHERE book_id=?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("Book Updated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE by ID
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE book_id=?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Book Deleted (by ID)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE by ISBN
    public void deleteBookByIsbn(String isbn) {
        String sql = "DELETE FROM books WHERE isbn=?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Book Deleted from Database (ISBN: " + isbn + ")");
            } else {
                System.out.println("No book found in Database with ISBN: " + isbn);
            }

        } catch (Exception e) {
            System.err.println("Failed to delete book from Database.");
            e.printStackTrace();
        }
    }
}

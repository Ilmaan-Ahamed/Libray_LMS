package Libray_LMS.Dao;

import Libray_LMS.db.DBConnection;
import Libray_LMS.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookDAO {

    // ADD Book to Database (matches current DB schema: title, author, available)
    public void addBook(Book book) {
        String sql = "INSERT INTO books(title, author, available) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.err.println("DB Connection is null. Book NOT saved to database.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setBoolean(3, true);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Book added to Database: " + book.getTitle());
            }
        } catch (Exception e) {
            System.err.println("Failed to add book to Database.");
            e.printStackTrace();
        }
    }

    // View Books from Database
    public void viewBooks() {
        String sql = "SELECT * FROM books";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.err.println("DB Connection is null. Cannot view books.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("------- Books in Database -------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                        rs.getInt("book_id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("author") + " | " +
                                (rs.getBoolean("available") ? "Available" : "Borrowed"));
            }
            if (!found) {
                System.out.println("No books found in database.");
            }
            System.out.println("---------------------------------");
        } catch (Exception e) {
            System.err.println("Failed to fetch books from Database.");
            e.printStackTrace();
        }
    }

    // Update book title by ID
    public void updateBook(int id, String title) {
        String sql = "UPDATE books SET title=? WHERE book_id=?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return;
            PreparedStatement ps = con.prepareStatement(sql);
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

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Book Deleted (by ID)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE by title and author (since DB doesn't have isbn column)
    public void deleteBookByIsbn(String isbn) {
        // Since the DB table doesn't have an isbn column,
        // we delete by matching title. In a future update,
        // add an isbn column to the books table for proper matching.
        String sql = "DELETE FROM books WHERE title=?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.err.println("DB Connection is null. Cannot delete book.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, isbn);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Book Deleted from Database.");
            } else {
                System.out.println("No matching book found in Database to delete.");
            }
        } catch (Exception e) {
            System.err.println("Failed to delete book from Database.");
            e.printStackTrace();
        }
    }
}

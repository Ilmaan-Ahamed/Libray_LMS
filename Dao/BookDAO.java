package Libray_LMS.Dao;

import Libray_LMS.db.DBConnection;
import Libray_LMS.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookDAO {

    // ✅ INSERT — now saves ISBN correctly
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, available) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.err.println("DB Connection is null. Book NOT saved.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn()); // ✅ ISBN now persisted
            ps.setBoolean(4, true);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Book saved to DB: " + book.getTitle());
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to add book to DB.");
            e.printStackTrace();
        }
    }

    // ✅ READ — displays isbn column too
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
                System.out.printf("ID: %-4d | %-35s | %-25s | ISBN: %-15s | %s%n",
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"), // ✅ show ISBN
                        rs.getBoolean("available") ? "Available" : "Borrowed");
            }
            if (!found)
                System.out.println("No books found in database.");
            System.out.println("---------------------------------");
        } catch (Exception e) {
            System.err.println("❌ Failed to fetch books from DB.");
            e.printStackTrace();
        }
    }

    // ✅ UPDATE title by book_id
    public void updateBook(int id, String newTitle) {
        String sql = "UPDATE books SET title = ? WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newTitle);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "✅ Book title updated." : "⚠️ No book found with ID: " + id);
        } catch (Exception e) {
            System.err.println("❌ Failed to update book.");
            e.printStackTrace();
        }
    }

    // ✅ UPDATE availability by ISBN — called on borrow & return
    public void updateAvailability(String isbn, boolean available) {
        String sql = "UPDATE books SET available = ? WHERE isbn = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, available);
            ps.setString(2, isbn);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ DB availability updated for ISBN: " + isbn
                        + " → " + (available ? "Available" : "Borrowed"));
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to update availability in DB.");
            e.printStackTrace();
        }
    }

    // ✅ DELETE by book_id
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "✅ Book deleted by ID." : "⚠️ No book found with ID: " + id);
        } catch (Exception e) {
            System.err.println("❌ Failed to delete book by ID.");
            e.printStackTrace();
        }
    }

    // ✅ DELETE by ISBN — now correctly matches isbn column
    public void deleteBookByIsbn(String isbn) {
        String sql = "DELETE FROM books WHERE isbn = ?"; // ✅ was wrongly using title

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.err.println("DB Connection is null. Cannot delete book.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, isbn);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0
                    ? "✅ Book deleted from DB (ISBN: " + isbn + ")"
                    : "⚠️ No book found in DB with ISBN: " + isbn);
        } catch (Exception e) {
            System.err.println("❌ Failed to delete book from DB.");
            e.printStackTrace();
        }
    }
}
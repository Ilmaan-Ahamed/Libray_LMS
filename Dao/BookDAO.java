package Libray_LMS.Dao;

import Libray_LMS.db.DBConnection;
import Libray_LMS.model.Book;
import Libray_LMS.ui.ConsoleUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookDAO {

    // INSERT — saves ISBN correctly
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, available) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                ConsoleUI.error("DB Connection is null. Book NOT saved.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setBoolean(4, true);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ConsoleUI.success("Book saved to DB: " + book.getTitle());
            }
        } catch (Exception e) {
            ConsoleUI.error("Failed to add book to DB.");
            e.printStackTrace();
        }
    }

    // READ — displays books in a styled table
    public void viewBooks() {
        String sql = "SELECT * FROM books";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                ConsoleUI.error("DB Connection is null. Cannot view books.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            ConsoleUI.printSectionHeader("📚  Books in Database");
            ConsoleUI.printTableHeader("ID", "Title", "Author", "ISBN", "Status");

            boolean found = false;
            while (rs.next()) {
                found = true;
                ConsoleUI.printTableRow(
                        String.valueOf(rs.getInt("book_id")),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getBoolean("available") ? "Available" : "Borrowed");
            }
            if (!found) {
                ConsoleUI.info("No books found in database.");
            }
            ConsoleUI.printTableFooterCustom(6, 20, 15, 13, 10);
        } catch (Exception e) {
            ConsoleUI.error("Failed to fetch books from DB.");
            e.printStackTrace();
        }
    }

    // UPDATE title by book_id
    public void updateBook(int id, String newTitle) {
        String sql = "UPDATE books SET title = ? WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newTitle);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ConsoleUI.success("Book title updated.");
            } else {
                ConsoleUI.warning("No book found with ID: " + id);
            }
        } catch (Exception e) {
            ConsoleUI.error("Failed to update book.");
            e.printStackTrace();
        }
    }

    // UPDATE availability by ISBN — called on borrow & return
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
                ConsoleUI.success("DB availability updated for ISBN: " + isbn
                        + " → " + (available ? "Available" : "Borrowed"));
            }
        } catch (Exception e) {
            ConsoleUI.error("Failed to update availability in DB.");
            e.printStackTrace();
        }
    }

    // DELETE by book_id
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ConsoleUI.success("Book deleted by ID.");
            } else {
                ConsoleUI.warning("No book found with ID: " + id);
            }
        } catch (Exception e) {
            ConsoleUI.error("Failed to delete book by ID.");
            e.printStackTrace();
        }
    }

    // DELETE by ISBN
    public void deleteBookByIsbn(String isbn) {
        String sql = "DELETE FROM books WHERE isbn = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                ConsoleUI.error("DB Connection is null. Cannot delete book.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, isbn);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ConsoleUI.success("Book deleted from DB (ISBN: " + isbn + ")");
            } else {
                ConsoleUI.warning("No book found in DB with ISBN: " + isbn);
            }
        } catch (Exception e) {
            ConsoleUI.error("Failed to delete book from DB.");
            e.printStackTrace();
        }
    }
}
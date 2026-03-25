package Libray_LMS.Dao;

import Libray_LMS.db.DBConnection;
import Libray_LMS.ui.ConsoleUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IssuedBookDAO {

    // INSERT — Record a book issue
    public void issueBook(int userId, String isbn) {
        String findBookSql = "SELECT book_id FROM books WHERE isbn = ?";
        String issueSql = "INSERT INTO issued_books (user_id, book_id, issue_date, return_status) VALUES (?, ?, GETDATE(), 0)";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                ConsoleUI.error("DB Connection is null. Cannot issue book.");
                return;
            }

            // Find the book_id
            PreparedStatement findPs = con.prepareStatement(findBookSql);
            findPs.setString(1, isbn);
            ResultSet rs = findPs.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("book_id");

                // Insert the issue record
                PreparedStatement issuePs = con.prepareStatement(issueSql);
                issuePs.setInt(1, userId);
                issuePs.setInt(2, bookId);
                int rows = issuePs.executeUpdate();

                if (rows > 0) {
                    ConsoleUI.success("Book issue recorded in DB (ISBN: " + isbn + ")");
                }
            } else {
                ConsoleUI.warning("Book not found in DB with ISBN: " + isbn);
            }
        } catch (Exception e) {
            ConsoleUI.error("Failed to record book issue in DB.");
            e.printStackTrace();
        }
    }

    // UPDATE — Mark a book as returned
    public void returnBook(int userId, String isbn) {
        String sql = "UPDATE issued_books SET return_status = 1 " +
                "WHERE user_id = ? AND book_id = (SELECT book_id FROM books WHERE isbn = ?) " +
                "AND return_status = 0";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, isbn);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ConsoleUI.success("Return recorded in DB for ISBN: " + isbn);
            }
        } catch (Exception e) {
            ConsoleUI.error("Failed to record return in DB.");
            e.printStackTrace();
        }
    }

    // READ — View all currently issued (not returned) books in a styled table
    public void viewIssuedBooks() {
        String sql = "SELECT ib.issue_id, u.name, b.title, b.isbn, ib.issue_date " +
                "FROM issued_books ib " +
                "JOIN users u ON ib.user_id = u.user_id " +
                "JOIN books b ON ib.book_id = b.book_id " +
                "WHERE ib.return_status = 0 " +
                "ORDER BY ib.issue_date DESC";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                ConsoleUI.error("DB Connection is null.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            ConsoleUI.printSectionHeader("📖  Currently Issued Books");
            ConsoleUI.printTableHeader("Issue#", "User", "Book Title", "ISBN", "Date");

            boolean found = false;
            while (rs.next()) {
                found = true;
                ConsoleUI.printTableRow(
                        String.valueOf(rs.getInt("issue_id")),
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getDate("issue_date").toString());
            }
            if (!found) {
                ConsoleUI.info("No books are currently issued.");
            }
            ConsoleUI.printTableFooterCustom(6, 20, 15, 13, 10);
        } catch (Exception e) {
            ConsoleUI.error("Failed to fetch issued books from DB.");
            e.printStackTrace();
        }
    }

    // READ — View issue history for a specific user in a styled table
    public void viewUserHistory(int userId) {
        String sql = "SELECT b.title, b.isbn, ib.issue_date, " +
                "CASE WHEN ib.return_status = 1 THEN 'Returned' ELSE 'Borrowed' END AS status " +
                "FROM issued_books ib " +
                "JOIN books b ON ib.book_id = b.book_id " +
                "WHERE ib.user_id = ? " +
                "ORDER BY ib.issue_date DESC";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            ConsoleUI.printSectionHeader("📜  Issue History");
            ConsoleUI.printTableHeader("Title", "ISBN", "Date", "Status");

            boolean found = false;
            while (rs.next()) {
                found = true;
                ConsoleUI.printTableRow(
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getDate("issue_date").toString(),
                        rs.getString("status"));
            }
            if (!found) {
                ConsoleUI.info("No borrowing history found.");
            }
            ConsoleUI.printTableFooterCustom(6, 22, 15, 12);
        } catch (Exception e) {
            ConsoleUI.error("Failed to fetch user history from DB.");
            e.printStackTrace();
        }
    }

    // READ — Get statistics: total issued, total returned, currently borrowed
    public void showStats() {
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM books) AS total_books, " +
                "(SELECT COUNT(*) FROM books WHERE available = 1) AS available_books, " +
                "(SELECT COUNT(*) FROM books WHERE available = 0) AS borrowed_books, " +
                "(SELECT COUNT(*) FROM issued_books) AS total_issues, " +
                "(SELECT COUNT(*) FROM issued_books WHERE return_status = 0) AS active_issues, " +
                "(SELECT COUNT(*) FROM users) AS total_users";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return;
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ConsoleUI.printStatsBox("DATABASE STATISTICS", new String[][] {
                        { "📚", "Total Books", String.valueOf(rs.getInt("total_books")) },
                        { "✅", "Available Books", String.valueOf(rs.getInt("available_books")) },
                        { "📖", "Borrowed Books", String.valueOf(rs.getInt("borrowed_books")) },
                        { "📝", "Total Issues", String.valueOf(rs.getInt("total_issues")) },
                        { "🔄", "Active Borrows", String.valueOf(rs.getInt("active_issues")) },
                        { "👤", "Registered Users", String.valueOf(rs.getInt("total_users")) }
                });
            }
        } catch (Exception e) {
            ConsoleUI.error("Failed to fetch library statistics.");
            e.printStackTrace();
        }
    }
}

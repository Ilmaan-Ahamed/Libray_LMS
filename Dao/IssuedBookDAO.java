package Libray_LMS.Dao;

import Libray_LMS.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IssuedBookDAO {

    // ✅ INSERT — Record a book issue
    public void issueBook(int userId, String isbn) {
        // First get book_id from isbn
        String findBookSql = "SELECT book_id FROM books WHERE isbn = ?";
        String issueSql = "INSERT INTO issued_books (user_id, book_id, issue_date, return_status) VALUES (?, ?, GETDATE(), 0)";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.err.println("DB Connection is null. Cannot issue book.");
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
                    System.out.println("✅ Book issue recorded in DB (ISBN: " + isbn + ")");
                }
            } else {
                System.out.println("⚠️ Book not found in DB with ISBN: " + isbn);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to record book issue in DB.");
            e.printStackTrace();
        }
    }

    // ✅ UPDATE — Mark a book as returned
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
                System.out.println("✅ Return recorded in DB for ISBN: " + isbn);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to record return in DB.");
            e.printStackTrace();
        }
    }

    // ✅ READ — View all currently issued (not returned) books
    public void viewIssuedBooks() {
        String sql = "SELECT ib.issue_id, u.name, b.title, b.isbn, ib.issue_date " +
                "FROM issued_books ib " +
                "JOIN users u ON ib.user_id = u.user_id " +
                "JOIN books b ON ib.book_id = b.book_id " +
                "WHERE ib.return_status = 0 " +
                "ORDER BY ib.issue_date DESC";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.err.println("DB Connection is null.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("------- Currently Issued Books -------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("Issue #%-4d | User: %-20s | Book: %-30s | ISBN: %-15s | Date: %s%n",
                        rs.getInt("issue_id"),
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getDate("issue_date").toString());
            }
            if (!found)
                System.out.println("No books are currently issued.");
            System.out.println("--------------------------------------");
        } catch (Exception e) {
            System.err.println("❌ Failed to fetch issued books from DB.");
            e.printStackTrace();
        }
    }

    // ✅ READ — View issue history for a specific user
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

            System.out.println("------- Issue History -------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-30s | ISBN: %-15s | Date: %s | Status: %s%n",
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getDate("issue_date").toString(),
                        rs.getString("status"));
            }
            if (!found)
                System.out.println("No borrowing history found.");
            System.out.println("-----------------------------");
        } catch (Exception e) {
            System.err.println("❌ Failed to fetch user history from DB.");
            e.printStackTrace();
        }
    }

    // ✅ READ — Get statistics: total issued, total returned, currently borrowed
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
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║     📊 LIBRARY STATISTICS            ║");
                System.out.println("╠══════════════════════════════════════╣");
                System.out.printf("║  📚 Total Books       : %-12d ║%n", rs.getInt("total_books"));
                System.out.printf("║  ✅ Available Books    : %-12d ║%n", rs.getInt("available_books"));
                System.out.printf("║  📖 Borrowed Books    : %-12d ║%n", rs.getInt("borrowed_books"));
                System.out.printf("║  📝 Total Issues      : %-12d ║%n", rs.getInt("total_issues"));
                System.out.printf("║  🔄 Active Borrows    : %-12d ║%n", rs.getInt("active_issues"));
                System.out.printf("║  👤 Registered Users  : %-12d ║%n", rs.getInt("total_users"));
                System.out.println("╚══════════════════════════════════════╝");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to fetch library statistics.");
            e.printStackTrace();
        }
    }
}

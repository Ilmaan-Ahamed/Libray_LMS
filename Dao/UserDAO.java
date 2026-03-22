package Libray_LMS.Dao;

import Libray_LMS.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // ✅ INSERT — Register a new user in the database
    public int addUser(String name) {
        String sql = "INSERT INTO users (name) VALUES (?)";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.err.println("DB Connection is null. User NOT saved.");
                return -1;
            }
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    int userId = keys.getInt(1);
                    System.out.println("✅ User registered in DB: " + name + " (ID: " + userId + ")");
                    return userId;
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to add user to DB.");
            e.printStackTrace();
        }
        return -1;
    }

    // ✅ READ — Check if a user already exists by name
    public int findUserByName(String name) {
        String sql = "SELECT user_id FROM users WHERE name = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return -1;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to search user in DB.");
            e.printStackTrace();
        }
        return -1;
    }

    // ✅ READ — Display all registered users
    public void viewUsers() {
        String sql = "SELECT * FROM users";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.err.println("DB Connection is null. Cannot view users.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("------- Registered Users -------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("ID: %-4d | Name: %s%n",
                        rs.getInt("user_id"),
                        rs.getString("name"));
            }
            if (!found)
                System.out.println("No users found in database.");
            System.out.println("--------------------------------");
        } catch (Exception e) {
            System.err.println("❌ Failed to fetch users from DB.");
            e.printStackTrace();
        }
    }

    // ✅ DELETE — Remove a user by ID
    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null)
                return;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "✅ User deleted." : "⚠️ No user found with ID: " + userId);
        } catch (Exception e) {
            System.err.println("❌ Failed to delete user from DB.");
            e.printStackTrace();
        }
    }
}

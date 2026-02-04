package Libray_LMS.Dao;

import Libray_LMS.db.DBConnection;
import Libray_LMS.model.Book;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookDAO {
    // ADD Book
    public void addBook(Book book){
        String sql = "INSERT INTO books(title, author, available) VALUES (?, ?, ?)";
   
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) 
            {
                ps.setString(1, book.getTitle());
                ps.setString(1, book.getAuthor());
                ps.setBoolean(3, true);
                
                ps. executeUpdate();
                System.out.println("Book added to Database");
        
            } 
        catch(Exception e)
            {
                e.printStackTrace();
            }
    }

    // View Books
    public void viewBooks(){

        String sql = "SELECT * FROM books";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
            {
                while (rs.next()) 
                    {
                        System.out.println(
                            rs.getInt("book_id") + " | " +
                            rs.getString("title") + " | " +
                            rs.getString("author") + " | " +
                            rs.getBoolean("available") + " | "  );
                    }
            } 
        catch (Exception e) 
            {
                e.printStackTrace();
            }
    }

    // update 
    public void updateBook(int id, String title){
        String sql = "UPDATE books SET title=? WHERE book_id=?";

        try (Connection con = DBConnection .getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, title);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("Book Updated");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

























}

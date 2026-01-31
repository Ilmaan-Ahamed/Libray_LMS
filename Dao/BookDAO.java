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
    }
}

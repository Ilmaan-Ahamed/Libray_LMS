package Libray_LMS.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    
    private static final String URL =
                "jdbc:sqlserver://localhost:1433;databaseName=Library_LMS_DB";
    
    private static final String USER = "root";
    private static final String PASSWORD = "JavaPRODB1";

    public static Connection getConnection(){
        try {
            //1. Estalish Connection 
            return DriverManager.getConnection(URL, USER, PASSWORD);
            } 

            // 2. Create statement
            
        catch (Exception e){
            e.printStackTrace();
            return null;
            }
    }
}

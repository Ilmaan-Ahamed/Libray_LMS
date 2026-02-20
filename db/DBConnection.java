package Libray_LMS.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Library_LMS_DB";

    private static final String USER = "root";
    private static final String PASSWORD = "JavaPRODB1";

    public static Connection getConnection() {
        try {
            // Load the driver (optional for modern JDBC but good for debugging)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 1. Establish Connection
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            if (con != null) {
                System.out.println("Connection Successful!");
            }
            return con;
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found. Please add the mssql-jdbc JAR to the lib folder.");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }
}

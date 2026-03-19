package Libray_LMS.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // ✅ Added encrypt=false & trustServerCertificate=true to prevent SSL handshake
    // errors
    private static final String URL = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=Library_LMS_DB;" +
            "encrypt=false;" +
            "trustServerCertificate=true;";

    // ⚠️ "root" is a MySQL convention — for SQL Server use "sa" or your actual
    // login
    private static final String USER = "sa";
    private static final String PASSWORD = "JavaPRODB1";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            if (con != null) {
                System.out.println("✅ DB Connection Successful!");
            }
            return con;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver not found. Add mssql-jdbc JAR to /lib.");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("❌ Connection Failed! Check server/credentials.");
            e.printStackTrace();
            return null;
        }
    }
}
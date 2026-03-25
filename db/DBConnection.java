package Libray_LMS.db;

import Libray_LMS.ui.ConsoleUI;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=Library_LMS_DB;" +
            "encrypt=false;" +
            "trustServerCertificate=true;";

    private static final String USER = "sa";
    private static final String PASSWORD = "JavaPRODB1";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            return con;
        } catch (ClassNotFoundException e) {
            ConsoleUI.error("JDBC Driver not found. Add mssql-jdbc JAR to /lib.");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            ConsoleUI.error("Connection Failed! Check server/credentials.");
            e.printStackTrace();
            return null;
        }
    }
}
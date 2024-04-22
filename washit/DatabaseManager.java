package com.example.washit;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:jtds:sqlserver://192.168.0.110:1433/WRRVProject";
    private static final String DB_USERNAME = "Project";
    private static final String DB_PASSWORD = "wrrv";

    public static Connection getConnection() throws SQLException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            return DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found.");
        }
    }
    public static String connectDB(Connection connection, Statement statement,String result){
        Log.d("database","debugging");
        try {
            if (connection != null) {
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                result = "Connection successful!";
            } else {
                result = "Connection failed!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Connection error: " + e.getMessage();
        } finally {
            // Close the resources
//            try {
//                if (statement != null)
//                    statement.close();
//                if (connection != null)
//                    connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        }

        return result;
    }
}

package com.example.project;

import java.sql.*;

public class Database {
    private Connection connection = null;
    private Statement statement = null ;
    Boolean connected = true;

    public void connectToDB(){

        String username = "Project";
        String password = "wrrv";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Connect to MS sql server");
        if(true){
            System.out.println("Locate database to open");
            String connectionString = "jdbc:sqlserver://LAPTOP-S6C78LQN\\demma:1433;database=WRRVProject";
            try {
                connection = DriverManager.getConnection(connectionString,username,password);
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                System.out.println("Connected");
            } catch (SQLException e) {
                System.out.printf("   Unable to connect to DB... '%s'\n", e.getMessage());
                connected = false;
            }
        }
        else{

        }
    }

    public Statement getStatement() {
        return statement;
    }
}

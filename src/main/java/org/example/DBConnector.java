package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final String databaseName = "jdbc:sqlite:src\\main\\resources\\cards.db";
    static Connection connection;

    static Connection connect(){

        if(connection == null){
            try {
                connection = DriverManager.getConnection(databaseName);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return connection;
    }
}

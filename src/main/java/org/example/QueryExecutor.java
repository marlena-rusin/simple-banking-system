package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutor {

    static ResultSet executeSelect(String selectQuery){

        try {
            Connection connection = DBConnector.connect();
            Statement statement = connection.createStatement();
            return statement.executeQuery(selectQuery);
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    static void executeQuery(String query){

        try {
            Connection connection = DBConnector.connect();
            Statement statement = connection.createStatement();
            statement.execute(query);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    static String checkAccountNumber(String number){
        while (true) {
            try {
                ResultSet resultSet = QueryExecutor.executeSelect("SELECT * from cards WHERE account_number = " + number);
                resultSet.next();
                return resultSet.getString(2);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static String checkBalance(String number){
        while (true) {
            try {
                ResultSet resultSet = QueryExecutor.executeSelect("SELECT * from cards WHERE account_number = " + number);
                resultSet.next();
                return resultSet.getString(4);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

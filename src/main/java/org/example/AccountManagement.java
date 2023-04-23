package org.example;

import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

public class AccountManagement {
    static Scanner scanner = new Scanner(System.in);
    private static final HashMap<String,  Account>  accountsList = new HashMap<>();


    // Loading data from database
    static void loadData(){
        try{
            // Retrieve all records
            String sql = "SELECT id, account_number, pin FROM cards ";
            ResultSet resultSet = QueryExecutor.executeSelect(sql);

            // Process the result set
            while (resultSet.next()) {
                String number = resultSet.getString("account_number");
                String pin = resultSet.getString("pin");

                // Converting each character to its numeric value

                int[] arrNUMBER = new int[number.length()];
                for (int i = 0; i < number.length(); i++) {
                    arrNUMBER[i] = Character.getNumericValue(number.charAt(i));
                }

                int[] arrPIN = new int[pin.length()];
                for (int i = 0; i < pin.length(); i++) {
                    arrPIN[i] = Character.getNumericValue(pin.charAt(i));
                }

                Account account = new Account();
                account.setNUMBER(arrNUMBER);
                account.setPIN(arrPIN);

                accountsList.put(number, account);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Creating account
    static void createAccount(){
        Account account = new Account();

        // Generating data
        account.generatePIN();
        account.generateNUMBER();

        // Checking if the generated number is available
        while (true) {
            String check = QueryExecutor.checkAccountNumber(account.getNUMBER());
            if(check == null){
                break;
            }
            account.generateNUMBER();
        }

        // Adding new account to the database
        QueryExecutor.executeQuery("INSERT INTO cards (account_number, pin) VALUES ( " + account.getNUMBER() + ", " + account.getPIN() + " )");
        System.out.println("Your account was successfully created.");
        System.out.println("Your account number: " + account.getNUMBER());
        System.out.println("Your account pin: " + account.getPIN());
        accountsList.put(account.getNUMBER(), account);
    }

    // User login
    static Account login() {

        // Getting data from client to log into account
        System.out.println("\nEnter your card number:");
        String enteredNumber = scanner.next();
        System.out.println("\nEnter your PIN");
        String enteredPIN = scanner.next();

        String result = null;

        // Getting client's card from database
        try {
            ResultSet resultSet = QueryExecutor.executeSelect("SELECT * from cards " +
                    "WHERE account_number = " + enteredNumber +
                    " AND PIN = " + enteredPIN);
            resultSet.next();
            result = resultSet.getString(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (result != null) {
            System.out.println("\nYou have successfully logged in!");
            return accountsList.get(enteredNumber);
        } else {
            System.out.println("\nSorry, wrong card number or PIN!");
            return null;
        }
    }

    // Showing balance
    static void showBalance(Account account){
        System.out.println("Account balance: " + QueryExecutor.checkBalance(account.getNUMBER()));
    }

    // Adding income
    static void addIncome(Account account, double income){
        double newBalance = Double.parseDouble(QueryExecutor.checkBalance(account.getNUMBER())) + income;

        String query = "UPDATE cards SET balance = " + newBalance + " WHERE account_number = " + account.getNUMBER();
        QueryExecutor.executeQuery(query);
        System.out.println("Account balance was successfully updated.");
    }

    // Transferring money to the another account
    static void transferMoney(Account senderAccount) throws SQLException {
        System.out.println("Enter the account number to which you want to transfer money: ");
        String receiverAccount = scanner.next();
        System.out.println("Enter the amount of money: ");
        double amount = scanner.nextDouble();
        String query;
        Connection connection = DBConnector.connect();

        // Checking if the given amount is available
        if (QueryExecutor.checkAccountNumber(receiverAccount) == null){
            System.out.println("There is no customer with such account number.");
            return;
        }

        // Checking if the given account exists
        if (Double.parseDouble(QueryExecutor.checkBalance(senderAccount.getNUMBER())) < amount ){
            System.out.println("Insufficient Balance!");
            return;
        }

        try{
            connection.setAutoCommit(false);

            double senderBalance = Double.parseDouble(QueryExecutor.checkBalance(senderAccount.getNUMBER())) - amount;
            double receiverBalance = Double.parseDouble(QueryExecutor.checkBalance(receiverAccount)) + amount;

            query = "UPDATE cards SET balance = " + senderBalance + " WHERE account_number = " + senderAccount.getNUMBER();
            QueryExecutor.executeQuery(query);

            query = "UPDATE cards SET balance = " + receiverBalance + " WHERE account_number = " + receiverAccount;
            QueryExecutor.executeQuery(query);

            connection.commit();

        }catch (SQLException e){
            connection.rollback();
            System.out.println(e.getMessage());
        }
    }

    // Deleting account
    static void deleteAccount(Account account){
        String query = "DELETE FROM cards WHERE account_number = " + account.getNUMBER();
        QueryExecutor.executeQuery(query);
        System.out.println("Your account was successfully closed.");
    }
}

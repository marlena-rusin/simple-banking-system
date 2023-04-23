package org.example;

import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    static Scanner scanner = new Scanner(System.in);

    static void launchMenu(){
        while (true){

            // Showing menu
            System.out.println("""

                    Main menu:
                    1. Create an account
                    2. Log into account
                    0. Exit
                    """);

            switch (scanner.nextInt()) {
                case 1 -> AccountManagement.createAccount();
                case 2 -> {
                        try {
                            launchCustomerMenu(AccountManagement.login());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                }
                case 0 -> {
                    System.out.println("Bye!");
                    return;
                }
            }
        }
    }

    static void launchCustomerMenu(Account account) throws SQLException {

        // Checking if account is null
        if (account == null){
            return;
        }

        while (true){

            // Showing client's menu
            System.out.println("""

                    Customer menu:
                    1. Show balance
                    2. Add income
                    3. Do transfer
                    4. Close account
                    5. Log out
                    """);

            switch (scanner.nextInt()) {
                case 1 -> AccountManagement.showBalance(account);
                case 2 -> {
                    System.out.println("How much money do you want to add to your account?");
                    double income = scanner.nextDouble();
                    AccountManagement.addIncome(account, income);
                }
                case 3 -> AccountManagement.transferMoney(account);
                case 4 -> {
                    AccountManagement.deleteAccount(account);
                    return;
                }
                case 5 -> {
                    System.out.println("You were successfully log out.");
                    return;
                }
            }
        }
    }
}


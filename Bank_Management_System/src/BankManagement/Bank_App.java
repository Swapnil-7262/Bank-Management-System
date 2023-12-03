package BankManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Bank_App {
	private static final String url = "jdbc:mysql://localhost:3306/bank_db";
	private static final String username = "root";
	private static final String password = "Swap7262@";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Scanner scanner = new Scanner(System.in);
			User user = new User(connection, scanner);
			Accounts accounts = new Accounts(connection, scanner);
			AccountManagements accountManagement = new AccountManagements(connection, scanner);
			String e_mail;
			long account_number;

			while (true) {
				System.out.println("--WELCOME TO BANKING SYSTEM----");
				System.out.println();
				System.out.println("1.Register");
				System.out.println("2.Login");
				System.out.println("3.Exit");
				System.out.println("Enter your Choice: ");
				int choice = scanner.nextInt();
				switch (choice) {
				case 1:
					user.register();
					break;
				case 2:
					e_mail = user.login();
					if (e_mail != null) {
						System.out.println();
						System.out.println("User Log In");
						if (!accounts.account_exist(e_mail)) {
							System.out.println();
							System.out.println("1.Open a new bank account. ");
							System.out.println("2.exit");
							if (scanner.nextInt() == 1) {
								account_number = accounts.open_account(e_mail);
								System.out.println("Account opening successfully");
								System.out.println("Your account number =" + account_number);
							} else {
								break;
							}

						}

						account_number = accounts.getAccount_number(e_mail);
						int choice2 = 0;
						while (choice2 != 5) {
							System.out.println("");
							System.out.println("1.Credit money");
							System.out.println("2.Debit money");
							System.out.println("3.Transfer money");
							System.out.println("4.Check balance");
							System.out.println("5.Log out");
							System.out.println("Enter your choice: ");
							choice2 = scanner.nextInt();
							switch (choice2) {
							case 1:
								accountManagement.credit_money(account_number);
								break;
							case 2:
								accountManagement.debit_amount(account_number);
								break;
							case 3:
								accountManagement.transfar_money(account_number);
								break;
							case 4:
								accountManagement.getBalance(account_number);
								break;
							case 5:
								break;
							default:
								System.out.println("Enter vaild choice.");
								break;
							}

						}
					}
					else {
						System.out.println("Invalid E_mail and Password.");
					}
					break;
				case 3:
					System.out.println("Thank you for using banking system.");
					System.out.println("Exiting system....");
					return;
				default:
					System.out.println("Enter vaild choice.");
					break;
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}

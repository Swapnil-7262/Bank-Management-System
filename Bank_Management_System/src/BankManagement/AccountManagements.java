package BankManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManagements {

	private Connection connection;
	private Scanner scanner;

	public AccountManagements(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void credit_money(long account_number) throws SQLException {

		scanner.nextLine();
		System.out.println("Enter amount: ");
		double amount = scanner.nextDouble();
		System.out.println("Enter security pin: ");
		scanner.nextLine();		
		String security_pin = scanner.nextLine();

		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_number =? and security_pin= ?;");
				preparedStatement.setDouble(1, account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					String cerditMoneyQuery = "update accounts set balance = balance + ? where account_number = ?;";
					PreparedStatement preparedStatement1 = connection.prepareStatement(cerditMoneyQuery);
					preparedStatement1.setDouble(1, amount);
					preparedStatement1.setLong(2, account_number);
					int affectedRows = preparedStatement1.executeUpdate();
					if (affectedRows > 0) {
						System.out.println("Rs" + amount + "Credit successfully.");
						connection.commit();
						connection.setAutoCommit(true);
						return;
					} else {
						System.out.println("Transaction fail.");
						connection.rollback();
						connection.setAutoCommit(true);
					}
				} else {
					System.out.println("Invalid security pin.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}

	public void debit_amount(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter amount: ");
		double amount = scanner.nextDouble();
		System.out.println("Enter security pin: ");
		scanner.nextLine();
		String security_pin = scanner.nextLine();

		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedStatement = connection
						.prepareStatement("select * from accounts where account_number = ?  and  security_pin = ?;");
				preparedStatement.setDouble(1, account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					double current_balance = resultSet.getDouble("balance");
					if (amount < current_balance) {
						String cerditMoneyQuery = "update accounts set balance = balance - ? where account_number = ?;";
						PreparedStatement preparedStatement1 = connection.prepareStatement(cerditMoneyQuery);
						preparedStatement1.setDouble(1, amount);
						preparedStatement1.setLong(2, account_number);
						int affectedRows = preparedStatement1.executeUpdate();
						if (affectedRows > 0) {
							System.out.println("Rs" + amount + "Debited successfully.");
							connection.commit();
							connection.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction fail.");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient balance.");
					}
					System.out.println("Invalid security pin.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}

	public void transfar_money(long sender_account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter receiver account number: ");
		long receiver_account_number = scanner.nextLong();
		System.out.println("Enter amount: ");
		double amount = scanner.nextDouble();
		System.out.println("Enter seucrity pin: ");
		scanner.nextLine();	
		String security_pin = scanner.nextLine();

		try {
			connection.setAutoCommit(false);
			if (sender_account_number != 0 && receiver_account_number != 0) {
				PreparedStatement preparedStatement = connection
						.prepareStatement("select * from accounts where account_number =?   and  security_pin=?;");
				preparedStatement.setLong(1, sender_account_number);
				preparedStatement.setString(2, security_pin);

				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					double current_balance = resultSet.getDouble("balance");
					if (amount < current_balance) {
						String creditQuery = "update accounts set balance = balance + ? where account_number = ?;";
						String debitQuery = "update accounts set balance = balance - ? where account_number = ?;";

						PreparedStatement creditprePreparedStatement = connection.prepareStatement(creditQuery);
						PreparedStatement debitprePreparedStatement = connection.prepareStatement(debitQuery);

						creditprePreparedStatement.setDouble(1, amount);
						creditprePreparedStatement.setLong(2, receiver_account_number);

						debitprePreparedStatement.setDouble(1, amount);
						debitprePreparedStatement.setLong(2, sender_account_number);

						int affectedRows1 = creditprePreparedStatement.executeUpdate();
						int affectedRows2 = debitprePreparedStatement.executeUpdate();

						if (affectedRows1 > 0 && affectedRows2 > 0) {
							System.out.println("Transaction successfull.");
							System.out.println("Rs" + amount + "Transfer successfull.");
							connection.commit();
							connection.setAutoCommit(true);
						} else {
							System.out.println("Transaction fail");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient balance.");
					}

				} else {
					System.out.println("Invalid security pin.");
				}
			} else {
				System.err.println("Invalid balance");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}

	public void getBalance(long account_number) {
		scanner.nextLine();
		System.out.println("Enter security pin: ");
		String security_pin = scanner.nextLine();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("select * from accounts where account_number =? and security_pin = ?;");
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, security_pin);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				double balance = resultSet.getDouble("balance");
				System.out.println("Balance= " + balance);
			} else {
				System.out.println("Invalid security pin.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}

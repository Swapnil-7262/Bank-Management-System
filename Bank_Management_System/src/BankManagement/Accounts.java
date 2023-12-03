package BankManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {

	private Connection connection;
	private Scanner scanner;

	public Accounts(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public long open_account(String e_mail) {
		if (!account_exist(e_mail)) {
			String open_account_query = "insert into accounts (account_number, full_name, e_mail, balance, security_pin) values(?,?,?,?,?) ";
			scanner.nextLine();
			System.out.println("Full name: ");
			String full_name = scanner.nextLine();
			System.out.println("Enter initial account: ");
			double balance = scanner.nextDouble();
			scanner.nextLine();
			System.out.println("Security pin: ");
			String security_pin = scanner.nextLine();
			try {
				long account_number = generateAccountNumber();
				PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, full_name);
				preparedStatement.setString(3, e_mail);
				preparedStatement.setDouble(4, balance);
				preparedStatement.setString(5, security_pin);

				int affectedRows = preparedStatement.executeUpdate();
				if (affectedRows > 0) {
					return account_number;
				} else {
					throw new RuntimeException("Account creation fail.");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		throw new RuntimeException("Account already exist.");
	}

	public long getAccount_number(String e_mail) {
		String query = "select account_number from accounts where e_mail =?;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, e_mail);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong("account_number");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Account number doesn't exist.");

	}

	private long generateAccountNumber() {
		String query = "select * from accounts order by account_number desc limit 1;";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				long last_account_number = resultSet.getLong("account_number");
				return last_account_number + 1;
			} else {
				return 10000100;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 10000100;
	}

	public boolean account_exist(String e_mail) {
		String query = "select account_number from accounts  where e_mail=?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, e_mail);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}

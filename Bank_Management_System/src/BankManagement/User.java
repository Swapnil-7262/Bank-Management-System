package BankManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

	private Connection connection;
	private Scanner scanner;

	public User(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void register() {
		scanner.nextLine();
		System.out.println("Full name: ");
		String full_name = scanner.nextLine();
		System.out.println("E-mail: ");
		String e_mail = scanner.nextLine();
		System.out.println("Password: ");
		String password = scanner.nextLine();
		if (user_exist(e_mail)) {
			System.out.println("User is already exists for this E-mail.");
		}

		String register_query = "insert into user (full_name,e_mail,password)values (?,?,?);";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(register_query);
			preparedStatement.setString(1, full_name);
			preparedStatement.setString(2, e_mail);
			preparedStatement.setString(3, password);
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Registration successfull.");
			} else {
				System.out.println("Registration fail.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public String login() {
		scanner.nextLine();
		System.out.println("E-mail: ");
		String e_mail = scanner.nextLine();
		System.out.println("Password: ");
		String password = scanner.nextLine();
		String login_query = "select * from user where e_mail=? and password=?;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(login_query);
			preparedStatement.setString(1, e_mail);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return e_mail;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean user_exist(String e_mail) {
		String query = "select * from user where e_mail =?";
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

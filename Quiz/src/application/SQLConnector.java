package application;
import java.sql.*;

public class SQLConnector {
	
	//private static Connection connection = null;
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz","root","Password@123");
			if(connection != null) {
				System.out.println("connected to database");
			}else {
				System.out.println("not connected to database");
			}
			return connection;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}

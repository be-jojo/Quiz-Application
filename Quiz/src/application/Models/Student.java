package application.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.SQLConnector;

public class Student {
	private Integer id;
	private String firstName;
	private String lastName;
	private String mobile;
	private Character gender;
	private String email;
	private String password;
	
	public Student(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	
	public Student(String firstName, String lastName, String mobile, Character gender, String email,
			String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.gender = gender;
		this.email = email;
		this.password = password;
	}

	public Student(Integer id, String firstName, String lastName, String mobile, Character gender, String email,
			String password) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.gender = gender;
		this.email = email;
		this.password = password;
	}
	
	public Student() { }

	public static class MetaData{// MAKE TABLE ON MY SQL
		public static final String TABLE_NAME = "students";
		public static final String SID = "id";
		public static final String FIRST_NAME = "firstName";
		public static final String LAST_NAME = "lastName";
		public static final String MOBILE = "mobileNumber";
		public static final String GENDER = "gender";
		public static final String EMAIL = "email";
		public static final String PASSWORD = "password";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isExists() {
		String raw = "SELECT * FROM %s WHERE %s = ?";
		String queryString = String.format(raw, MetaData.TABLE_NAME, MetaData.EMAIL);
		try {
			Connection con = SQLConnector.getConnection();
			PreparedStatement pStatement = con.prepareStatement(queryString);
			pStatement.setString(1, this.email);
			ResultSet rSet = pStatement.executeQuery();
			if(rSet .next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Student saveToDatabase() {
		String rawString = "INSERT INTO %s (%s, %s, %s, %s, %s, %s)\r\n"
				+ "VALUES(?, ?, ?, ?, ?, ?)";
		String query = String.format(rawString, MetaData.TABLE_NAME, MetaData.FIRST_NAME,
				MetaData.LAST_NAME, MetaData.MOBILE, MetaData.EMAIL, MetaData.PASSWORD, MetaData.GENDER);
		try {
			Connection connection = SQLConnector.getConnection();
			PreparedStatement pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pStatement.setString(1, this.firstName);
			pStatement.setString(2, this.lastName);
			pStatement.setString(3, this.mobile);
			pStatement.setString(4, this.email);
			pStatement.setString(5, this.password);
			pStatement.setString(6, String.valueOf(this.gender));
			int uRaw = pStatement.executeUpdate();
			ResultSet rSet = pStatement.getGeneratedKeys();
			if(rSet.next()) {
				this.id = rSet.getInt(uRaw);
			}
			return this;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("data not saved");
		}
		return null;
	}
	
	public boolean logIn() {
		String raw = "SELECT * FROM %s WHERE %s = ? AND %s = ?";
		String queryString = String.format(raw, MetaData.TABLE_NAME, MetaData.EMAIL, MetaData.PASSWORD);
		try {
			Connection con = SQLConnector.getConnection();
			PreparedStatement pStatement = con.prepareStatement(queryString);
			pStatement.setString(1, this.email);
			pStatement.setString(2, this.password);
			ResultSet rSet = pStatement.executeQuery();
			if(rSet .next()) {
				this.setId(rSet.getInt(1));
				this.setFirstName(rSet.getString(2));
				this.setLastName(rSet.getString(3));
				this.setMobile(rSet.getString(4));
				this.setGender(rSet.getString(7).charAt(0));
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
}

package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import application.Models.Question;
import application.Models.Quiz;
import application.Models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;

import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class AdminStudentTabController implements Initializable {
	// Form Entries
	@FXML private Label updateErrorLabel;
	@FXML private TextField firstNameTF;
	@FXML private TextField lastNameTF;
	@FXML private TextField mobileNumberTF;
	@FXML private PasswordField passwordTF;
	@FXML private TextField emailTF;
	@FXML private RadioButton maleRB;
	@FXML private RadioButton femaleRB;
	@FXML private RadioButton othersRB;
	@FXML private Button saveBtn;
	
	// Table view
	@FXML private TableView<Student> studentTable;
	@FXML private TableColumn<Student, Integer> sIdColumn;
	@FXML private TableColumn<Student, String> fnColumn;
	@FXML private TableColumn<Student, String> lnColumn;
	@FXML private TableColumn<Student, String> mobileNumColumn;
	@FXML private TableColumn<Student, String> genderColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> passwordColumn;
    
    // List of Students in the database
    ObservableList<Student> studentList = FXCollections.observableArrayList();

	// My Variables
	private ToggleGroup genderGroup;
	Character genderChar;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setUpRadioButton();
	}
	
	public void resetFields() {
		// clear all the fields in the form
		firstNameTF.clear();
		lastNameTF.clear();
		mobileNumberTF.clear();
		passwordTF.clear();
		emailTF.clear();
	}
	
	public void renderStudents() {
		// fill the table with all available students in database
		try {
			String query = "SELECT * FROM `quiz`.`students`";
			Connection connection = SQLConnector.getConnection();
			PreparedStatement pStatement = connection.prepareStatement(query);
			ResultSet rSet = pStatement.executeQuery();
			while(rSet.next()) {
				studentList.add(new Student(rSet.getInt(1), rSet.getString(2), rSet.getString(3),
						rSet.getString(4), rSet.getString(7).charAt(0), rSet.getString(6) ,rSet.getString(5)));
				studentTable.setItems(studentList);
			}
			pStatement.close();
			rSet.close();
		}catch (Exception e) {
			System.out.println("Error: "+e);
		}
		sIdColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
		fnColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
		lnColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
		mobileNumColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("mobile"));
		genderColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("gender"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
		passwordColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("password"));
		
	}
	
	public boolean validateStudentInformation() {
		// validate all values entered in javafx fields
		String firstName = this.firstNameTF.getText();
		String lastName = this.lastNameTF.getText();
		String mobileNum = this.mobileNumberTF.getText();
		String passwordStr = this.passwordTF.getText();
		String eMailStr = this.emailTF.getText();
		genderChar = 'M';
		
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");
		
		Toggle genderToggle = genderGroup.getSelectedToggle();
		if(genderToggle != null && genderToggle == femaleRB) {
			genderChar = 'F';
		}else if(genderToggle == null) {
			updateErrorLabel.setText("Gender");
			return false;
		}
		if(firstName.trim().isEmpty() || lastName.trim().isEmpty()
				|| mobileNum.trim().isEmpty()) {
			updateErrorLabel.setText("All fields Necessary ");
			return false;
		}
		if(mobileNum.trim().length() < 10) {
			updateErrorLabel.setText("Enter Valid Mobile Number");
			return false;
		}else if(eMailStr.length() < 5) {
			updateErrorLabel.setText("Enter Valid Email");
			return false;
		}else if(passwordStr.length() < 4) {
			updateErrorLabel.setText("Enter valid password");
			return false;
		}else if(!p.matcher(eMailStr).matches()) {
			updateErrorLabel.setText("Enter correct Email");
			return false;
		}
			
		return true;
	}
	
	@FXML
	public void addStudent(ActionEvent event) {
		// save the student information in database
		studentList.clear();
		boolean valid = validateStudentInformation();
		if(valid) {
			String firstName = this.firstNameTF.getText();
			String lastName = this.lastNameTF.getText();
			String mobileNum = this.mobileNumberTF.getText();
			String passwordStr = this.passwordTF.getText();
			String eMailStr = this.emailTF.getText();
			Student student = new Student(firstName, lastName, mobileNum, genderChar, eMailStr, passwordStr);
			if(student.isExists()) {
				updateErrorLabel.setText("You are already registered");
				return;
			}
			student = student.saveToDatabase();
			if(student != null) {
				updateErrorLabel.setText("Student Added");
				this.resetFields();
			}
		}else {
			System.out.println("reer");
		}
		renderStudents();
	}

	private void setUpRadioButton() {
		// create a toggle group of radio buttons
		genderGroup = new ToggleGroup();
		this.maleRB.setSelected(true);
		maleRB.setToggleGroup(genderGroup);
		femaleRB.setToggleGroup(genderGroup);
		othersRB.setToggleGroup(genderGroup);
	}
}

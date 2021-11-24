package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import application.Models.Student;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class LogInController {
	// FXML Objects
    @FXML private TabPane loginTabPane;
    @FXML private Tab studentPane;
    @FXML private Label studentLabel;
    @FXML private TextField studentEmailTF;
    @FXML private PasswordField studentPasswordTF;
    @FXML private Button studentLogInButton;
    @FXML private Tab adminPane;
    @FXML private Label adminLabel;
    @FXML private TextField adminEmailTF;
    @FXML private PasswordField adminPasswordTF;
    @FXML private Button adminLogInButton;

    
	@FXML public void hanndleLogIn(ActionEvent event) {
		// fetching data provided by user
		String userEmailId = adminEmailTF.getText();
		String userPassword = adminPasswordTF.getText();
				
		if(userEmailId.equals("abc@gmail.com") && userPassword.equals("123")) {
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("FXMLFiles/AdminHomeScreenPage.fxml"));
				Stage stage = (Stage)adminEmailTF.getScene().getWindow();
				stage.setTitle("Admin Home Page");
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Checking student info and if correct direct to home page
	@FXML
	public void studentLogIn(ActionEvent event) {
		String studentEmail = this.studentEmailTF.getText();
		String studentPassword = this.studentPasswordTF.getText();
		Student student = new Student(studentEmail, studentPassword);
		if(student.logIn()) {
			Parent root;
			try {
				FXMLLoader fLoader = new FXMLLoader(getClass().getResource("FXMLFiles/StudentHomePage.fxml"));
				root = fLoader.load();
				StudentHomePageController sHPController = fLoader.getController();
				sHPController.setStudent(student);
				Stage stage = (Stage)adminEmailTF.getScene().getWindow();
				stage.setTitle("Student Home Page");
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			studentLabel.setText("Log In Failed");
		}
	}
	// Checking admin info and if correct direct to home page
	@FXML
	public void adminLogIn(ActionEvent event) {
		
	}
}

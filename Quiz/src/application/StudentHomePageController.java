package application;

import java.net.URL;
import java.util.ResourceBundle;

import application.Listeners.NewScreenListener;
import application.Models.Student;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;

import javafx.scene.layout.StackPane;

public class StudentHomePageController implements Initializable {
	
	@FXML private StackPane stackPane;
	@FXML private Button backButton;
	
	// My variables
	private Student student;
	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
		addQuizListScreen();
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	
	private void addScreenToStackPane(Node node) {
		this.stackPane.getChildren().add(node);
	}
	//
	private void addQuizListScreen() {
		FXMLLoader fLoader = new FXMLLoader(getClass().getResource("FXMLFiles/QuizList.fxml"));
		
		try {
			Node node = fLoader.load();
			QuizListController quizListController = fLoader.getController();
			quizListController.setStudent(this.student);
			quizListController.setScreenListener(new NewScreenListener() {

				@Override
				public void handle(Event arg0) {
					
					
				}

				@Override
				public void changeScreen(Node node) {
					addScreenToStackPane(node);
				}
				@Override
				public void removeTopScreen() {
					stackPane.getChildren().remove(stackPane.getChildren().size() - 1);
				}
				
			});
			quizListController.setCards();
			stackPane.getChildren().add(node);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void backScreen() {
		int totalChildScreen = this.stackPane.getChildren().size();
		if(totalChildScreen > 1) {
			this.stackPane.getChildren().remove(totalChildScreen-1);
		}
	}

}
